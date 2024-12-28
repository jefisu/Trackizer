package com.jefisu.user.presentation.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jefisu.domain.model.User
import com.jefisu.domain.repository.UserRepository
import com.jefisu.domain.util.onError
import com.jefisu.domain.util.onSuccess
import com.jefisu.ui.MessageController
import com.jefisu.ui.navigation.Navigator
import com.jefisu.ui.util.asMessageText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val navigator: Navigator,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(EditProfileState())
    val state = _state
        .onStart { loadUser() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            EditProfileState(),
        )

    private var _currentUser: User? = null

    fun onAction(action: EditProfileAction) {
        when (action) {
            is EditProfileAction.NameChanged -> updateName(action.name)
            is EditProfileAction.PhotoPicked -> {
                _state.update {
                    it.copy(
                        user = it.user?.copy(pictureUrl = action.photoUri.toString()),
                    )
                }
            }
            EditProfileAction.NavigateBack -> viewModelScope.launch {
                navigator.navigateUp()
            }
            EditProfileAction.SaveChanges -> saveChanges()
        }
    }

    private fun updateName(name: String) {
        if (name.length > User.NAME_MAX_LENGTH) return
        _state.update {
            it.copy(user = it.user?.copy(name = name))
        }
    }

    private fun loadUser() = viewModelScope.launch {
        val user = userRepository.user.firstOrNull()
        _state.update { it.copy(user = user) }
        _currentUser = user
    }

    private fun saveChanges() {
        viewModelScope.launch {
            var (_, name, _, pictureUrl) = _state.value.user ?: return@launch

            if (name == _currentUser?.name) name = null
            if (pictureUrl == _currentUser?.pictureUrl) pictureUrl = null
            if (name == null && pictureUrl == null) {
                navigator.navigateUp()
                return@launch
            }

            _state.update { it.copy(isUpdating = true) }
            userRepository.updateProfile(name, pictureUrl)
                .onSuccess {
                    navigator.navigateUp()
                }
                .onError { error ->
                    MessageController.sendMessage(error.asMessageText())
                }
            _state.update { it.copy(isUpdating = false) }
        }
    }
}