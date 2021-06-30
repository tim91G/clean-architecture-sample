package com.timgortworst.cleanarchitecture.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timgortworst.cleanarchitecture.data.model.DbMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDataSourceMovie {

    @Query("SELECT * FROM DbMovie")
    fun getMovies(): Flow<List<DbMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(dbMovies: List<DbMovie>)

    @Query("SELECT * FROM DbMovie")
    fun getMoviePages(): PagingSource<Int, DbMovie>

    @Query("DELETE FROM DbMovie")
    suspend fun clearAll()
}
