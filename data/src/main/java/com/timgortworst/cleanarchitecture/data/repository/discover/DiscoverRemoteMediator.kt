package com.timgortworst.cleanarchitecture.data.repository.discover

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.timgortworst.cleanarchitecture.data.database.AppDatabase
import com.timgortworst.cleanarchitecture.data.mapper.asDatabaseModel
import com.timgortworst.cleanarchitecture.data.model.DbMovie
import com.timgortworst.cleanarchitecture.data.model.NetworkMovies
import com.timgortworst.cleanarchitecture.data.network.RemoteDataSourceMovie
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class DiscoverRemoteMediator(
    private val networkService: RemoteDataSourceMovie,
    private val database: AppDatabase,
) : RemoteMediator<Int, DbMovie>() {
    private val movieDao = database.movieDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DbMovie>
    ): MediatorResult {

        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(true)

                    lastItem.page
                }
            }

            val response = networkService.getPagedMovies(loadKey ?: 1).body() ?: run {
                return MediatorResult.Error(Throwable())
            }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) movieDao.clearAll()

                movieDao.insertMovies(response.asDatabaseModel())
            }

            MediatorResult.Success(response.page == response.totalPages)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}