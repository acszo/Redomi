package com.acszo.redomi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acszo.redomi.data.DataStoreConst.FIRST_TIME
import com.acszo.redomi.data.DataStoreConst.HORIZONTAL_LIST
import com.acszo.redomi.data.DataStoreConst.ICON_SHAPE
import com.acszo.redomi.data.DataStoreConst.LAYOUT_GRID_SIZE
import com.acszo.redomi.data.DataStoreConst.LAYOUT_LIST_TYPE
import com.acszo.redomi.data.DataStoreConst.MEDIUM_GRID
import com.acszo.redomi.data.DataStoreConst.THEME_MODE
import com.acszo.redomi.data.IconShape
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.data.Theme
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

    private val _openingApps: MutableStateFlow<List<AppDetails>> = MutableStateFlow(emptyList())
    val openingApps: StateFlow<List<AppDetails>> = _openingApps.asStateFlow()

    private val _sharingApps: MutableStateFlow<List<AppDetails>> = MutableStateFlow(emptyList())
    val sharingApps: StateFlow<List<AppDetails>> = _sharingApps.asStateFlow()

    private val _isFirstTime: MutableStateFlow<Boolean?> = MutableStateFlow(false)
    val isFirstTime: StateFlow<Boolean?> = _isFirstTime.asStateFlow()

    private val _layoutListType: MutableStateFlow<Int> = MutableStateFlow(HORIZONTAL_LIST)
    val layoutListType: StateFlow<Int> = _layoutListType.asStateFlow()

    private val _layoutGridSize: MutableStateFlow<Int> = MutableStateFlow(MEDIUM_GRID)
    val layoutGridSize: StateFlow<Int> = _layoutGridSize.asStateFlow()

    private val _iconShape: MutableStateFlow<Int> = MutableStateFlow(IconShape.SQUIRCLE.ordinal)
    val iconShape: StateFlow<Int> = _iconShape.asStateFlow()

    private val _themeMode: MutableStateFlow<Int> = MutableStateFlow(Theme.SYSTEM_THEME.ordinal)
    val themeMode: StateFlow<Int> = _themeMode.asStateFlow()

    fun getOpeningApps() = viewModelScope.launch {
        dataStoreRepository.readDataStore().collectLatest {
            _openingApps.value = it.openingAppsSelection
        }
    }

    fun setOpeningApps(openingApps: List<AppDetails>) = viewModelScope.launch {
        dataStoreRepository.saveOpeningApps(openingApps)
    }

    fun getSharingApps() = viewModelScope.launch {
        dataStoreRepository.readDataStore().collectLatest {
            _sharingApps.value = it.sharingAppsSelection
        }
    }

    fun setSharingApps(sharingApps: List<AppDetails>) = viewModelScope.launch {
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

    fun getLayoutListType() = viewModelScope.launch {
        settingsDataStore.getInt(LAYOUT_LIST_TYPE).collectLatest {
            _layoutListType.value = it ?: HORIZONTAL_LIST
        }
    }

    fun setLayoutListType(layoutListType: Int) = viewModelScope.launch {
        settingsDataStore.setInt(LAYOUT_LIST_TYPE, layoutListType)
    }

    fun getLayoutGridSize() = viewModelScope.launch {
        settingsDataStore.getInt(LAYOUT_GRID_SIZE).collectLatest {
            _layoutGridSize.value = it ?: MEDIUM_GRID
        }
    }

    fun setLayoutGridSize(layoutGridSize: Int) = viewModelScope.launch {
        settingsDataStore.setInt(LAYOUT_GRID_SIZE, layoutGridSize)
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