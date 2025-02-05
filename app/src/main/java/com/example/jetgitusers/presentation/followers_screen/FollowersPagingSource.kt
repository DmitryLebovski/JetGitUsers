package com.example.jetgitusers.presentation.followers_screen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.usecase.GetUserFollowersUseCase

class FollowersPagingSource (
    private val getUserFollowersUseCase: GetUserFollowersUseCase,
    private val username: String
    ): PagingSource<Int, User>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: 1
        return try {
            val result = getUserFollowersUseCase(
                username = username,
                page = page
            )

            result.fold(
                onSuccess = { followers ->
                    val nextKey = if (followers.isNotEmpty()) page + 1 else null
                    LoadResult.Page(
                        data = followers,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = nextKey
                    )
                },
                onFailure =  { throwable ->
                    LoadResult.Error(throwable)
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
        }
    }
    //state.anchorPosition определяе видимую позицию,
    // closestPageToPosition оперделяет к какой "странице" относится элемент на экране
    // добавляем элемент ключа когда подходим к концу
}