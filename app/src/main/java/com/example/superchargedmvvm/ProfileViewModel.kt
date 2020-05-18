package com.example.superchargedmvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel(val interactor: GetProfileInteractor) : ViewModel() {
    val viewState = MutableLiveData<ViewState>()
    val actionState = SingleLiveEvent<ActionState>()

    init {
        fetchProfileAndRender()
    }

    private fun fetchProfileAndRender() {
        println("fetchProfileAndRender")
        viewState.value = ViewState.Loading
        interactor.getProfile().let { getProfileState ->
            println("profile state is $getProfileState")
            when (getProfileState) {
                is GetProfileInteractor.State.Success -> {
                    // render updates on UI
                    viewState.value = ViewState.ProfileLoaded(getProfileState.profile)
                }
                is GetProfileInteractor.State.Failure -> {
                    viewState.value = ViewState.ProfileLoadFailure(getProfileState.errorMessage)
                }
                GetProfileInteractor.State.TnCUpdated -> {
                    actionState.value = ActionState.TnCUpdated
                }
                GetProfileInteractor.State.LogOutRequired -> {
                    // clear local data
                    actionState.value = ActionState.NeedsLogOut
                }
            }
        }
    }

    sealed class ViewState {
        object Loading: ViewState()
        data class ProfileLoaded(val profile: Profile): ViewState()
        data class ProfileLoadFailure(val errorMessage: String): ViewState()
    }

    sealed class ActionState {
        object TnCUpdated : ActionState()
        object NeedsLogOut: ActionState()
    }
}