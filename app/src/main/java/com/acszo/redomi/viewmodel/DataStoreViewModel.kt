package com.acszo.redomi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acszo.redomi.data.DataStoreConst.HORIZONTAL_LIST
import com.acszo.redomi.data.DataStoreConst.MEDIUM_GRID
import com.acszo.redomi.data.DataStoreConst.SYSTEM_THEME
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.model.AppDetails
import com.acszo.redomi.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val settingsDataStore: SettingsDataStore
): ViewModel() {

    private val _installedApps: MutableStateFlow<List<AppDetails>> = MutableStateFlow(emptyList())
    val installedApps: StateFlow<List<AppDetails>> = _installedApps.asStateFlow()

    private val _allApps: MutableStateFlow<List<AppDetails>> = MutableStateFlow(emptyList())
    val allApps: StateFlow<List<AppDetails>> = _allApps.asStateFlow()

    private val _isFirstTime: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFirstTime: StateFlow<Boolean> = _isFirstTime.asStateFlow()

    private val _layoutListType: MutableStateFlow<Int> = MutableStateFlow(HORIZONTAL_LIST)
    val layoutListType: StateFlow<Int> = _layoutListType.asStateFlow()

    private val _layoutGridSize: MutableStateFlow<Int> = MutableStateFlow(MEDIUM_GRID)
    val layoutGridSize: StateFlow<Int> = _layoutGridSize.asStateFlow()

    private val _themeMode: MutableStateFlow<Int> = MutableStateFlow(SYSTEM_THEME)
    val themeMode: StateFlow<Int> = _themeMode.asStateFlow()

    fun getInstalledApps() = viewModelScope.launch {
        dataStoreRepository.readDataStore().collectLatest {
            _installedApps.value = it.installedApps
        }
    }

    fun setInstalledApps(installedApps: List<AppDetails>) = viewModelScope.launch {
        dataStoreRepository.saveInstalledApps(installedApps)
    }

    fun getAllApps() = viewModelScope.launch {
        dataStoreRepository.readDataStore().collectLatest {
            _allApps.value = it.allApps
        }
    }

    fun setAllApps(allApps: List<AppDetails>) = viewModelScope.launch {
        dataStoreRepository.saveAllApps(allApps)
    }

    fun getIsFirstTime() = viewModelScope.launch {
        settingsDataStore.getIsFirstTime.collectLatest {
            _isFirstTime.value = it!!
        }
    }

    fun setIsFirstTime() = viewModelScope.launch {
        settingsDataStore.saveIsFirstTime()
    }

    fun getLayoutListType() = viewModelScope.launch {
        settingsDataStore.getLayoutListType.collectLatest {
            _layoutListType.value = it!!
        }
    }

    fun setLayoutListType(layoutListType: Int) = viewModelScope.launch {
        settingsDataStore.saveLayoutListType(layoutListType)
    }
    fun getLayoutGridSize() = viewModelScope.launch {
        settingsDataStore.getLayoutGridSize.collectLatest {
            _layoutGridSize.value = it!!
        }
    }

    fun setLayoutGridSize(layoutGridSize: Int) = viewModelScope.launch {
        settingsDataStore.saveLayoutGridSize(layoutGridSize)
    }
    fun getThemeMode() = viewModelScope.launch {
        settingsDataStore.getThemeMode.collectLatest {
            _themeMode.value = it!!
        }
    }

    fun setThemeMode(themeMode: Int) = viewModelScope.launch {
        settingsDataStore.saveThemeMode(themeMode)
    }
}