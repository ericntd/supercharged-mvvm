package com.example.superchargedmvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel(val interactor: GetProfileInteractor) : ViewModel() {
    val viewState = MutableLiveData<ViewState>()
    val actionState = MutableLiveData<ActionState>()

    fun fetchProfileAndRender() {
        viewState.value = ViewState.Loading
        interactor.getProfile().let { getProfileState ->
            when (getProfileState) {
                is GetProfileInteractor.State.Success -> {
                    // render updates on UI
                    viewState.value = ViewState.ProfileLoaded(getProfileState.profile)
                }
                is GetProfileInteractor.State.Failure -> {
                    viewState.value = ViewState.ProfileLoadFailure(getProfileState.errorMessage)
                }
                GetProfileInteractor.State.VerificationRequired -> {
                    actionState.value = ActionState.NeedsVerification
                }
            }
        }
    }

    sealed class ViewState() {
        object Loading: ViewState()
        data class ProfileLoaded(val profile: Profile): ViewState()
        data class ProfileLoadFailure(val errorMessage: String): ViewState()
    }

    sealed class ActionState {
        object NeedsVerification : ActionState()
        object NeedsLogOut: ActionState()
    }
}