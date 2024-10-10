package com.example.podcastapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.podcastapp.data.local.entity.EpisodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {
    @Query("UPDATE episodes SET isFavorite = :isFavorite WHERE guid = :episodeGuid")
    suspend fun updateFavoriteStatus(episodeGuid: String, isFavorite: Boolean)

    @Query("SELECT * FROM episodes WHERE isFavorite = 1")
    fun observeFavorites(): Flow<List<EpisodeEntity>>

    @Query("SELECT * FROM episodes WHERE podcastUrl = :podcastUrl ORDER BY publishDate DESC")
    fun getEpisodesByPodcast(podcastUrl: String): Flow<List<EpisodeEntity>>

    @Query("SELECT * FROM episodes WHERE guid = :guid")
    suspend fun getEpisodeByGuid(guid: String): EpisodeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisodes(episodes: List<EpisodeEntity>)

    @Update
    suspend fun updateEpisode(episode: EpisodeEntity)

    @Query("UPDATE episodes SET isDownloaded = :isDownloaded WHERE guid = :guid")
    suspend fun updateDownloadStatus(guid: String, isDownloaded: Boolean)

    @Query("SELECT * FROM episodes WHERE isFavorite = 1")
    fun getFavoriteEpisodes(): Flow<List<EpisodeEntity>>

    @Query("SELECT * FROM episodes WHERE isDownloaded = 1")
    fun getDownloadedEpisodes(): Flow<List<EpisodeEntity>>
}