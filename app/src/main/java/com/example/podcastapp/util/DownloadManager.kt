package com.example.podcastapp.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.example.podcastapp.data.local.dao.EpisodeDao
import com.example.podcastapp.data.local.dao.PodcastDao
import com.example.podcastapp.domain.model.Episode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class PodcastDownloadManager(
    private val context: Context,
    private val podcastDao: PodcastDao,
    private val episodeDao: EpisodeDao
) {
    private val downloadManager =
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    private val downloadRequests = mutableMapOf<Long, String>() // DownloadId to EpisodeGuid

    fun downloadEpisode(episode: Episode): Long {
        val request = DownloadManager.Request(Uri.parse(episode.audioUrl))
            .setTitle(episode.title)
            .setDescription("Downloading podcast episode")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_PODCASTS,
                "${episode.guid}.mp3"
            )

        val downloadId = downloadManager.enqueue(request)
        downloadRequests[downloadId] = episode.guid

        return downloadId
    }

    fun observeDownloads(): Flow<DownloadStatus> = flow {
        while (true) {
            val cursor = downloadManager.query(DownloadManager.Query())

            cursor.use {
                if (it.moveToFirst()) {
                    do {
                        val id = it.getLong(it.getColumnIndexOrThrow(DownloadManager.COLUMN_ID))
                        val status = it.getInt(it.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
                        val episodeGuid = downloadRequests[id] ?: continue

                        when (status) {
                            DownloadManager.STATUS_SUCCESSFUL -> {
                                episodeDao.updateDownloadStatus(episodeGuid, true)
                                emit(DownloadStatus.Completed(episodeGuid))
                                downloadRequests.remove(id)
                            }
                            DownloadManager.STATUS_FAILED -> {
                                emit(DownloadStatus.Failed(episodeGuid))
                                downloadRequests.remove(id)
                            }
                            DownloadManager.STATUS_RUNNING -> {
                                val bytesDownloaded = it.getLong(it.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                                val bytesTotal = it.getLong(it.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                                val progress = if (bytesTotal > 0) (bytesDownloaded * 100 / bytesTotal).toInt() else 0
                                emit(DownloadStatus.Progress(episodeGuid, progress))
                            }
                        }
                    } while (it.moveToNext())
                }
            }
            delay(1000) // Check every second
        }
    }


    fun getDownloadedEpisodeFile(episodeGuid: String): File? {
        return context.getExternalFilesDir(Environment.DIRECTORY_PODCASTS)?.let { dir ->
            File(dir, "$episodeGuid.mp3").takeIf { it.exists() }
        }
    }

}

sealed class DownloadStatus {
    data class Progress(val episodeGuid: String, val progress: Int) : DownloadStatus()
    data class Completed(val episodeGuid: String) : DownloadStatus()
    data class Failed(val episodeGuid: String) : DownloadStatus()
}