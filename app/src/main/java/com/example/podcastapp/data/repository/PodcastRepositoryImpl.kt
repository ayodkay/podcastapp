package com.example.podcastapp.data.repository

import android.util.Xml
import com.example.podcastapp.data.local.dao.EpisodeDao
import com.example.podcastapp.data.local.dao.PodcastDao
import com.example.podcastapp.data.mapper.toDomainModel
import com.example.podcastapp.data.mapper.toEntity
import com.example.podcastapp.data.remote.PodcastService
import com.example.podcastapp.domain.model.Episode
import com.example.podcastapp.domain.model.Podcast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.xmlpull.v1.XmlPullParser
import java.io.StringReader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PodcastRepositoryImpl(
    private val podcastDao: PodcastDao,
    private val episodeDao: EpisodeDao,
    private val podcastService: PodcastService
) : PodcastRepository {

    override fun getAllPodcasts(): Flow<List<Podcast>> {
        return podcastDao.getAllPodcasts().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getPodcastWithEpisodes(url: String): Podcast? {
        val podcast = podcastDao.getPodcast(url)?.toDomainModel() ?: return null
        val episodes = episodeDao.getEpisodesByPodcast(url).map { it.map { episodeEntity -> episodeEntity.toDomainModel() } }
        return podcast.copy(episodes = episodes.first())
    }

    override suspend fun insertPodcast(podcast: Podcast) {
        podcastDao.insertPodcast(podcast.toEntity())
        episodeDao.insertEpisodes(podcast.episodes.map { it.toEntity() })
    }

    override suspend fun updateEpisode(episode: Episode) {
        episodeDao.updateEpisode(episode.toEntity())
    }

    override suspend fun toggleFavorite(episodeGuid: String, isFavorite: Boolean) {
        episodeDao.updateFavoriteStatus(episodeGuid, isFavorite)
    }

    override fun getFavoriteEpisodes(): Flow<List<Episode>> {
        return episodeDao.getFavoriteEpisodes().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun searchPodcasts(query: String): Flow<List<Podcast>> {
        return podcastDao.searchPodcasts(query).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getPodcastDetails(podcastUrl: String): Flow<Podcast> {
        return flow { Podcast("", "", "", "", "", Date(), emptyList()) }
    }

    override suspend fun fetchAndSavePodcast(url: String): Podcast {
        val response = podcastService.getFeed(url)
        if (response.isSuccessful) {
            val feedContent  = response.body()?.string() ?: ""
            val podcast = parsePodcastFeed(url, feedContent)
            podcastDao.insertPodcast(podcast.toEntity())
            episodeDao.insertEpisodes(podcast.episodes.map { it.toEntity() })
            return podcast
        } else {
            throw Exception("Failed to fetch podcast: ${response.errorBody()?.string()}")
        }
    }

    override fun getEpisodes(podcastUrl: String): Flow<List<Episode>> {
        return episodeDao.getEpisodesByPodcast(podcastUrl).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }


    private fun parsePodcastFeed(feedUrl: String, feedContent: String): Podcast {
        val parser: XmlPullParser = Xml.newPullParser()
        parser.setInput(StringReader(feedContent))
        parser.nextTag()

        var title = ""
        var description = ""
        var imageUrl = ""
        var author = ""
        var lastUpdated: Long = System.currentTimeMillis()
        val episodes = mutableListOf<Episode>()


        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "title" -> title = readTextFromTag(parser, "title")
                "description" -> description = readTextFromTag(parser, "description")
                "itunes:image" -> imageUrl = parser.getAttributeValue(null, "href")
                "itunes:author" -> author = readTextFromTag(parser, "itunes:author")
                "lastBuildDate" -> lastUpdated = parseDate(readTextFromTag(parser, "lastBuildDate"))
                "pubDate" -> {
                    // Use pubDate as a fallback if lastBuildDate is not available
                    if (lastUpdated == System.currentTimeMillis()) {
                        lastUpdated = parseDate(readTextFromTag(parser, "pubDate"))
                    }
                }
                "item" -> episodes.add(parseEpisode(parser, feedUrl))
            }
        }

        return Podcast(
            url = feedUrl,
            title = title,
            description = description,
            imageUrl = imageUrl,
            author = author,
            lastUpdated = Date(lastUpdated),
            episodes = episodes
        )
    }

    private fun parseEpisode(parser: XmlPullParser, podcastUrl: String): Episode {
        var guid = ""
        var title = ""
        var description = ""
        var audioUrl = ""
        var publicationDate: Long = 0
        var duration: Long = 0

        while (parser.next() != XmlPullParser.END_TAG || parser.name != "item") {
            if (parser.eventType != XmlPullParser.START_TAG) {
                continue
            }

            when (parser.name) {
                "guid" -> guid = readTextFromTag(parser, "guid")
                "title" -> title = readTextFromTag(parser, "title")
                "description" -> description = readTextFromTag(parser, "description")
                "enclosure" -> audioUrl = parser.getAttributeValue(null, "url")
                "pubDate" -> publicationDate = parseDate(readTextFromTag(parser, "pubDate"))
                "itunes:duration" -> duration = parseDuration(readTextFromTag(parser, "itunes:duration"))
            }
        }

        return Episode(
            guid = guid,
            title = title,
            description = description,
            audioUrl = audioUrl,
            publicationDate = Date(publicationDate),
            duration = duration,
            podcastUrl = podcastUrl
        )
    }

    private fun readTextFromTag(parser: XmlPullParser, tagName: String): String {
        parser.require(XmlPullParser.START_TAG, null, tagName)
        var result = ""
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.text
            parser.nextTag()
        }
        parser.require(XmlPullParser.END_TAG, null, tagName)
        return result
    }

    private fun parseDate(dateString: String): Long {
        val format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
        return try {
            format.parse(dateString)?.time ?: System.currentTimeMillis()
        } catch (e: ParseException) {
            System.currentTimeMillis()
        }
    }

    private fun parseDuration(durationString: String): Long {
        return try {
            val parts = durationString.split(":").map { it.toLong() }
            when (parts.size) {
                3 -> parts[0] * 3600 + parts[1] * 60 + parts[2]
                2 -> parts[0] * 60 + parts[1]
                1 -> parts[0]
                else -> 0
            }
        } catch (e: NumberFormatException) {
            0
        }
    }
}
