package com.example.jetgitusers.presentation.users_screen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.usecase.GetUsersUseCase

class UsersPagingSource (
    private val getUsersUseCase: GetUsersUseCase
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val since = params.key ?: 0
        return try {
            val result = getUsersUseCase(since)
            result.fold(
                onSuccess = { users ->
                    val nextKey = if (users.isNotEmpty()) users.last().id else null
                    LoadResult.Page(
                        data = users,
                        prevKey = null,
                        nextKey = nextKey
                    )
                },
                onFailure = { throwable ->
                    LoadResult.Error(throwable)
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(20)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(20)
        }
    }
}