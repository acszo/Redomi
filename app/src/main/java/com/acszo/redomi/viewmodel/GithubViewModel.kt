package com.acszo.redomi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acszo.redomi.model.Release
import com.acszo.redomi.repository.GithubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class GithubViewModel @Inject constructor(
    private val githubRepository: GithubRepository
): ViewModel() {

    private val _latestRelease: MutableStateFlow<Release?> = MutableStateFlow(null)
    val latestRelease: StateFlow<Release?> = _latestRelease

    private val _isNotLatest: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isNotLatest: StateFlow<Boolean> = _isNotLatest

    fun getLatestRelease(currentVersion: String) = viewModelScope.launch {
        try {
            _latestRelease.update {
                githubRepository.getLatest()
            }
            _isNotLatest.update {
                currentVersion != _latestRelease.value?.tag_name
            }
        }  catch (e: Exception) {
            print(e.message)
        }
    }

}