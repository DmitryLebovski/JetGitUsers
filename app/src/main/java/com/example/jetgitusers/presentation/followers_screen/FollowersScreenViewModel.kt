package com.example.jetgitusers.presentation.followers_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.jetgitusers.domain.model.User
import com.example.jetgitusers.domain.usecase.GetUserFollowersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FollowersScreenViewModel @Inject constructor(
    private val getUserFollowersUseCase: GetUserFollowersUseCase,
) : ViewModel() {

   fun getFollowers(username: String): Flow<PagingData<User>> {
       return Pager(
           config = PagingConfig(pageSize = 20, enablePlaceholders = false),
           pagingSourceFactory = { FollowersPagingSource(
               getUserFollowersUseCase = getUserFollowersUseCase,
               username = username
           ) }
       ).flow.cachedIn(viewModelScope)
   }
}