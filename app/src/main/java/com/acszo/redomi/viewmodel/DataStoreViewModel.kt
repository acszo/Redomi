package com.acszo.redomi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acszo.redomi.data.DataStoreConst.COUNTRY_CODE
import com.acszo.redomi.data.DataStoreConst.DEFAULT_COUNTRY_CODE
import com.acszo.redomi.data.DataStoreConst.FIRST_TIME
import com.acszo.redomi.data.DataStoreConst.GRID_SIZE
import com.acszo.redomi.data.DataStoreConst.ICON_SHAPE
import com.acszo.redomi.data.DataStoreConst.LIST_ORIENTATION
import com.acszo.redomi.data.DataStoreConst.MEDIUM_GRID
import com.acszo.redomi.data.DataStoreConst.OPENING_APPS
import com.acszo.redomi.data.DataStoreConst.SHARING_APPS
import com.acszo.redomi.data.DataStoreConst.THEME_MODE
import com.acszo.redomi.data.IconShape
import com.acszo.redomi.data.ListOrientation
import com.acszo.redomi.data.SettingsDataStore
import com.acszo.redomi.data.Theme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore
): ViewModel() {

    private val _openingApps = MutableStateFlow<Set<String>>(emptySet())
    val openingApps = _openingApps.asStateFlow()

    private val _sharingApps = MutableStateFlow<Set<String>>(emptySet())
    val sharingApps = _sharingApps.asStateFlow()

    private val _isFirstTime = MutableStateFlow(false)
    val isFirstTime = _isFirstTime.asStateFlow()

    private val _listOrientation = MutableStateFlow(ListOrientation.HORIZONTAL.ordinal)
    val listOrientation = _listOrientation.asStateFlow()

    private val _gridSize = MutableStateFlow(MEDIUM_GRID)
    val gridSize = _gridSize.asStateFlow()

    private val _iconShape = MutableStateFlow(IconShape.SQUIRCLE.ordinal)
    val iconShape = _iconShape.asStateFlow()

    private val _themeMode = MutableStateFlow(Theme.SYSTEM_THEME.ordinal)
    val themeMode = _themeMode.asStateFlow()

    private val _countryCode = MutableStateFlow(DEFAULT_COUNTRY_CODE)
    val countryCode = _countryCode.asStateFlow()

    init {
        viewModelScope.launch {
            settingsDataStore.getInt(LIST_ORIENTATION).collectLatest {
                _listOrientation.value = it ?: ListOrientation.HORIZONTAL.ordinal
            }
        }

        viewModelScope.launch {
            settingsDataStore.getInt(GRID_SIZE).collectLatest {
                _gridSize.value = it ?: MEDIUM_GRID
            }
        }

        viewModelScope.launch {
            settingsDataStore.getInt(ICON_SHAPE).collectLatest {
                _iconShape.value = it ?: IconShape.SQUIRCLE.ordinal
            }
        }

        viewModelScope.launch {
            settingsDataStore.getInt(THEME_MODE).collectLatest {
                _themeMode.value = it ?: Theme.SYSTEM_THEME.ordinal
            }
        }

        viewModelScope.launch {
            settingsDataStore.getString(COUNTRY_CODE).collectLatest {
                _countryCode.value = it ?: DEFAULT_COUNTRY_CODE
            }
        }
    }

    fun getCountryCodeBlocking(): String = runBlocking {
        settingsDataStore.getString(COUNTRY_CODE).first() ?: DEFAULT_COUNTRY_CODE
    }

    fun getOpeningApps() = viewModelScope.launch {
        settingsDataStore.getSetOfStrings(OPENING_APPS).collectLatest {
            _openingApps.value = it
        }
    }

    fun setOpeningApps(openingApps: Set<String>) = viewModelScope.launch {
        settingsDataStore.setSetOfStrings(OPENING_APPS, openingApps)
    }

    fun getSharingApps() = viewModelScope.launch {
        settingsDataStore.getSetOfStrings(SHARING_APPS).collectLatest {
            _sharingApps.value = it
        }
    }

    fun setSharingApps(sharingApps: Set<String>) = viewModelScope.launch {
        settingsDataStore.setSetOfStrings(SHARING_APPS, sharingApps)
    }

    fun getIsFirstTime() = viewModelScope.launch {
        settingsDataStore.getBoolean(FIRST_TIME).collectLatest {
            _isFirstTime.value = it != false
        }
    }

    fun setIsFirstTime() = viewModelScope.launch {
        settingsDataStore.setBoolean(FIRST_TIME, false)
    }

    fun setListOrientation(listOrientation: Int) = viewModelScope.launch {
        settingsDataStore.setInt(LIST_ORIENTATION, listOrientation)
    }

    fun setGridSize(gridSize: Int) = viewModelScope.launch {
        settingsDataStore.setInt(GRID_SIZE, gridSize)
    }

    fun setIconShape(iconShape: Int) = viewModelScope.launch {
        settingsDataStore.setInt(ICON_SHAPE, iconShape)
    }

    fun setThemeMode(themeMode: Int) = viewModelScope.launch {
        settingsDataStore.setInt(THEME_MODE, themeMode)
    }

    fun setCountryCode(countryCode: String) = viewModelScope.launch {
        settingsDataStore.setString(COUNTRY_CODE, countryCode)
    }

}