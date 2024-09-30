package com.semonemo.presentation.screen.frame

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.NFTRepository
import com.semonemo.domain.request.UploadFrameRequest
import com.semonemo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FrameViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
        private val nftRepository: NFTRepository,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(FrameUiState())
        val uiState = _uiState.asStateFlow()
        var frame = mutableStateOf<Bitmap?>(null)
            private set
        private val _uiEvent = MutableSharedFlow<FrameUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        fun updateFrame(bitmap: Bitmap) {
            _uiState.update { it.copy(bitmap = bitmap) }
        }

        fun uploadImage(file: File) {
            viewModelScope.launch {
                nftRepository
                    .uploadImage(file)
                    .onStart {
                        _uiState.update { it.copy(isLoading = true) }
                    }.onCompletion {
                        file.deleteOnExit()
                        _uiState.update { it.copy(isLoading = false) }
                    }.collectLatest { response ->
                        Log.d("jaehan", "uploadImage : $response")
                        when (response) {
                            is ApiResponse.Success -> uploadFrame(response.data.hash)
                            is ApiResponse.Error -> {
                                _uiEvent.emit(FrameUiEvent.Error(response.errorMessage))
                            }
                        }
                    }
            }
        }

        private suspend fun uploadFrame(imageHash: String) {
            nftRepository
                .uploadFrame(
                    request =
                        UploadFrameRequest(
                            title = uiState.value.title,
                            content = uiState.value.content,
                            image = imageHash,
                        ),
                ).onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }.onCompletion {
                    _uiState.update { it.copy(isLoading = false) }
                }.collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(FrameUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {}
                    }
                }
        }

        fun updateTitle(title: String) {
            _uiState.update { it.copy(title = title) }
        }

        fun updateContent(content: String) {
            _uiState.update { it.copy(content = content) }
        }

        fun addTag(tag: String) {
            _uiState.update {
                val newList =
                    it.tags.toMutableStateList().apply {
                        add(tag)
                    }
                it.copy(
                    tags = newList,
                )
            }
        }

        fun deleteTag(tag: String) {
            _uiState.update {
                val newList =
                    it.tags.filter { t ->
                        t != tag
                    }
                it.copy(
                    tags = newList,
                )
            }
        }
    }
