package com.example.superchargedmvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.random.Random

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
                    viewState.value = ViewState.ProfileLoadFailure(Random.nextInt())
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
        data class ProfileLoadFailure(val errorType: Int): ViewState()
    }

    sealed class ActionState {
        object NeedsVerification : ActionState()
        object NeedsLogOut: ActionState()
    }
}