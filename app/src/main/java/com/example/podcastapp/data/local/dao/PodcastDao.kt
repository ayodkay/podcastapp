package com.example.podcastapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.podcastapp.data.local.entity.PodcastEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PodcastDao {
    @Query("SELECT * FROM podcasts")
    fun getAllPodcasts(): Flow<List<PodcastEntity>>

    @Query("SELECT * FROM podcasts WHERE url = :url")
    suspend fun getPodcast(url: String): PodcastEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPodcast(podcast: PodcastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPodcasts(podcasts: List<PodcastEntity>)

    @Update
    suspend fun updatePodcast(podcast: PodcastEntity)

    @Delete
    suspend fun deletePodcast(podcast: PodcastEntity)

    @Query("DELETE FROM podcasts WHERE url = :url")
    suspend fun deletePodcastByUrl(url: String)

    @Query("SELECT * FROM podcasts WHERE title LIKE '%' || :query || '%' OR author LIKE '%' || :query || '%'")
    fun searchPodcasts(query: String): Flow<List<PodcastEntity>>
}