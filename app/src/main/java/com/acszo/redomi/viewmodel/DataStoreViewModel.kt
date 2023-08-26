package com.acszo.redomi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {

    private val _installedApps: MutableStateFlow<List<AppDetails>> = MutableStateFlow(emptyList())
    val installedApps: StateFlow<List<AppDetails>> = _installedApps.asStateFlow()

    private val _allApps: MutableStateFlow<List<AppDetails>> = MutableStateFlow(emptyList())
    val allApps: StateFlow<List<AppDetails>> = _allApps.asStateFlow()

    fun getInstalledApps() = viewModelScope.launch {
        _installedApps.update {
            dataStoreRepository.readInstalledApps()
        }
    }

    fun setInstalledApps(installedApps: List<AppDetails>) = viewModelScope.launch {
        dataStoreRepository.saveInstalledApps(installedApps)
    }

    fun getAllApps() = viewModelScope.launch {
        _allApps.update {
            dataStoreRepository.readAllApps()
        }
    }

    fun setAllApps(allApps: List<AppDetails>) = viewModelScope.launch {
        dataStoreRepository.saveAllApps(allApps)
    }

}