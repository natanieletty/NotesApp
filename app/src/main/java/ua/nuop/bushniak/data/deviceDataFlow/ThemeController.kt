package ua.nuop.bushniak.data.deviceDataFlow

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ThemeController @Inject constructor() {
    private val _isDarkEnabled = MutableStateFlow(true)
    val isDarkEnabled = _isDarkEnabled.asStateFlow()

    fun toggle() {
        _isDarkEnabled.value = !_isDarkEnabled.value
    }

    fun setScheme(isDark: Boolean) {
        _isDarkEnabled.update {
            isDark
        }
    }
}