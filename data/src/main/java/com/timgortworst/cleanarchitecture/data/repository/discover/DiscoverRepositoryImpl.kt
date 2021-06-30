package com.timgortworst.cleanarchitecture.data.repository.discover

import androidx.paging.*
import com.timgortworst.cleanarchitecture.data.database.AppDatabase
import com.timgortworst.cleanarchitecture.data.mapper.asDomainModel
import com.timgortworst.cleanarchitecture.data.network.RemoteDataSourceMovie
import com.timgortworst.cleanarchitecture.domain.model.movie.Movie
import com.timgortworst.cleanarchitecture.domain.repository.DiscoverRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DiscoverRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSourceMovie,
    private val local: AppDatabase
) : DiscoverRepository {

    @ExperimentalPagingApi
    override fun getPagedMovies(): Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 2),
        remoteMediator = DiscoverRemoteMediator(remote, local)
    ) {
        local.movieDao().getMoviePages()
    }.flow.map { pagingData -> pagingData.map { dbMovie -> dbMovie.asDomainModel() } }


//    override suspend fun getPagedMovies(page: Int) = object : NetworkBoundRepository<NetworkMovies, List<Movie>>() {
//
//        override suspend fun saveRemoteData(response: NetworkMovies) =
//            localDataSourceMovie.insertMovies(response.asDatabaseModel())
//
//        override fun fetchFromLocal() = localDataSourceMovie.getMovies().map { dbMovieList ->
//            dbMovieList.map { it.asDomainModel() }
//        }
//
//        override suspend fun fetchFromRemote() = remoteDataSourceMovie.getPagedMovies(page)
//
//        override suspend fun getErrorHandler() = errorHandler
//
//    }.asFlow().flowOn(Dispatchers.IO)
}