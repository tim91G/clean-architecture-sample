package com.timgortworst.cleanarchitecture.data.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.timgortworst.cleanarchitecture.domain.model.state.ErrorHandler
import com.timgortworst.cleanarchitecture.domain.model.state.Resource
import kotlinx.coroutines.flow.*
import retrofit2.Response


/**
 * A repository which provides resource from local database as well as remote end point.
 *
 * [NETWORK] represents the type for network.
 * [DOMAIN] represents the type for domain.
 */
abstract class NetworkBoundRepository<NETWORK, DOMAIN> {

    fun asFlow() = flow {
        emit(Resource.Loading)

        try {
            emit(Resource.Success(fetchFromLocal().first()))

            val apiResponse = fetchFromRemote()
            val remoteResponse = apiResponse.body()

            if (apiResponse.isSuccessful && remoteResponse != null) {
                saveRemoteData(remoteResponse)
            } else {
                val error = getErrorHandler().getApiError(
                    statusCode = apiResponse.code(),
                    message = apiResponse.message()
                )
                emit(Resource.Error(error))
            }
        } catch (e: Exception) {
            emit(Resource.Error(getErrorHandler().getError(e)))
        }

        emitAll(fetchFromLocal().map { Resource.Success(it) })
    }

    protected abstract suspend fun getErrorHandler() : ErrorHandler

    @WorkerThread
    protected abstract suspend fun saveRemoteData(response: NETWORK)

    @MainThread
    protected abstract fun fetchFromLocal(): Flow<DOMAIN>

    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<NETWORK>
}
