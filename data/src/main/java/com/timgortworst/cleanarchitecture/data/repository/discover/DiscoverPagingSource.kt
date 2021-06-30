//package com.timgortworst.cleanarchitecture.data.repository.discover
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.timgortworst.cleanarchitecture.data.mapper.asDomainModel
//import com.timgortworst.cleanarchitecture.data.network.RemoteDataSourceMovie
//import com.timgortworst.cleanarchitecture.domain.model.movie.Movie
//import retrofit2.HttpException
//import java.io.IOException
//import javax.inject.Inject
//
//class DiscoverPagingSource @Inject constructor(
//    private val remoteDataSourceMovie: RemoteDataSourceMovie,
//) : PagingSource<Int, Movie>() {
//
//    override suspend fun load(
//        params: LoadParams<Int>
//    ): LoadResult<Int, Movie> {
//        return try {
//            val nextPageNumber = params.key ?: 1
//            val response = remoteDataSourceMovie.getPagedMovies(nextPageNumber).body()!!
//            val nextPage = if (response.page < response.totalPages) response.page + 1 else null
//
//            LoadResult.Page(
//                data = response.asDomainModel(),
//                prevKey = null, // Only paging forward.
//                nextKey = nextPage
//            )
//        } catch (e: IOException) {
//            // IOException for network failures.
//            return LoadResult.Error(e)
//        } catch (e: HttpException) {
//            // HttpException for any non-2xx HTTP status codes.
//            return LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
//        // Try to find the page key of the closest page to anchorPosition, from
//        // either the prevKey or the nextKey, but you need to handle nullability
//        // here:
//        //  * prevKey == null -> anchorPage is the first page.
//        //  * nextKey == null -> anchorPage is the last page.
//        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
//        //    just return null.
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//}