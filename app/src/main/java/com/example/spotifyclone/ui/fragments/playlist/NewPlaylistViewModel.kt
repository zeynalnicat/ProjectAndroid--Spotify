package com.example.spotifyclone.ui.fragments.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.spotifyclone.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class NewPlaylistViewModel() : ViewModel() {

    val isSuccessful = MutableLiveData<Resource<Boolean>>()

//    fun insert(name: String) {
//        isSuccessful.postValue(Resource.Loading)
//
//        viewModelScope.launch(Dispatchers.IO) {
//            val insertion = playlistDao.insert(PlaylistEntity(playlistName = name))
//            if (insertion != -1L) {
//                isSuccessful.postValue(Resource.Success(true))
//            } else {
//                isSuccessful.postValue(Resource.Error(Exception("There was an error while handling the request")))
//            }
//        }
//    }
}