package com.acszo.redomi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acszo.redomi.data.DataStoreConst.FIRST_TIME
import com.acszo.redomi.data.DataStoreConst.GRID_SIZE
import com.acszo.redomi.data.DataStoreConst.ICON_SHAPE
import com.acszo.redomi.data.DataStoreConst.LIST_ORIENTATION
import com.acszo.redomi.data.DataStoreConst.MEDIUM_GRID
import com.acszo.redomi.data.DataStoreConst.THEME_MODE
import com.acszo.redomi.data.IconShape
import com.acszo.redomi.data.ListOrientation
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.data.Theme
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

    private val _openingApps: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val openingApps: StateFlow<List<String>> = _openingApps.asStateFlow()

    private val _sharingApps: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val sharingApps: StateFlow<List<String>> = _sharingApps.asStateFlow()

    private val _isFirstTime: MutableStateFlow<Boolean?> = MutableStateFlow(false)
    val isFirstTime: StateFlow<Boolean?> = _isFirstTime.asStateFlow()

    private val _listOrientation: MutableStateFlow<Int> = MutableStateFlow(ListOrientation.HORIZONTAL.ordinal)
    val listOrientation: StateFlow<Int> = _listOrientation.asStateFlow()

    private val _gridSize: MutableStateFlow<Int> = MutableStateFlow(MEDIUM_GRID)
    val gridSize: StateFlow<Int> = _gridSize.asStateFlow()

    private val _iconShape: MutableStateFlow<Int> = MutableStateFlow(IconShape.SQUIRCLE.ordinal)
    val iconShape: StateFlow<Int> = _iconShape.asStateFlow()

    private val _themeMode: MutableStateFlow<Int> = MutableStateFlow(Theme.SYSTEM_THEME.ordinal)
    val themeMode: StateFlow<Int> = _themeMode.asStateFlow()

    fun getOpeningApps() = viewModelScope.launch {
        dataStoreRepository.readDataStore().collectLatest {
            _openingApps.value = it.openingAppsSelection
        }
    }

    fun setOpeningApps(openingApps: List<String>) = viewModelScope.launch {
        dataStoreRepository.saveOpeningApps(openingApps)
    }

    fun getSharingApps() = viewModelScope.launch {
        dataStoreRepository.readDataStore().collectLatest {
            _sharingApps.value = it.sharingAppsSelection
        }
    }

    fun setSharingApps(sharingApps: List<String>) = viewModelScope.launch {
        dataStoreRepository.saveSharingApps(sharingApps)
    }

    fun getIsFirstTime() = viewModelScope.launch {
        settingsDataStore.getBoolean(FIRST_TIME).collectLatest {
            _isFirstTime.value = it ?: true
        }
    }

    fun setIsFirstTime() = viewModelScope.launch {
        settingsDataStore.setBoolean(FIRST_TIME, false)
    }

    fun getListOrientation() = viewModelScope.launch {
        settingsDataStore.getInt(LIST_ORIENTATION).collectLatest {
            _listOrientation.value = it ?: ListOrientation.HORIZONTAL.ordinal
        }
    }

    fun setListOrientation(listOrientation: Int) = viewModelScope.launch {
        settingsDataStore.setInt(LIST_ORIENTATION, listOrientation)
    }

    fun getGridSize() = viewModelScope.launch {
        settingsDataStore.getInt(GRID_SIZE).collectLatest {
            _gridSize.value = it ?: MEDIUM_GRID
        }
    }

    fun setGridSize(gridSize: Int) = viewModelScope.launch {
        settingsDataStore.setInt(GRID_SIZE, gridSize)
    }

    fun getIconShape() = viewModelScope.launch {
        settingsDataStore.getInt(ICON_SHAPE).collectLatest {
            _iconShape.value = it ?: IconShape.SQUIRCLE.ordinal
        }
    }

    fun setIconShape(iconShape: Int) = viewModelScope.launch {
        settingsDataStore.setInt(ICON_SHAPE, iconShape)
    }

    fun getThemeMode() = viewModelScope.launch {
        settingsDataStore.getInt(THEME_MODE).collectLatest {
            _themeMode.value = it ?: Theme.SYSTEM_THEME.ordinal
        }
    }

    fun setThemeMode(themeMode: Int) = viewModelScope.launch {
        settingsDataStore.setInt(THEME_MODE, themeMode)
    }

}