package ua.nuop.bushniak.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.nuop.bushniak.repo.ThemeRepo
import javax.inject.Inject

@HiltViewModel(assistedFactory = MainViewModel.Factory::class)
class MainViewModel @AssistedInject constructor(
    private val themeRepo: ThemeRepo,
    @Assisted private val defaultScheme: Boolean
) : ViewModel() {



    init {
        viewModelScope.launch {
            themeRepo.init(defaultScheme)

        }
    }

    @AssistedFactory
    interface Factory {
        fun create(defaultScheme: Boolean): MainViewModel
    }
}