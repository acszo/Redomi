package com.acszo.redomi

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application()

val isGithubBuild: Boolean get() = BuildConfig.FLAVOR == "github"

val versionName: String get() = BuildConfig.VERSION_NAME