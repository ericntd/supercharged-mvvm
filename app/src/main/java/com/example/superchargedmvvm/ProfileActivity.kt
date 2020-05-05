package com.example.superchargedmvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileActivity : AppCompatActivity() {
    private val tvName: TextView by lazy {
        findViewById<TextView>(R.id.tv_name)
    }

    private val viewModel: ProfileViewModel by lazy {
        val getProfileInteractor = GetProfileInteractor()
        val viewModelFactory = object: ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ProfileViewModel(getProfileInteractor) as T
            }

        }

        return@lazy ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpObservation()

        viewModel.fetchProfileAndRender()
    }

    private fun setUpObservation() {
        viewModel.viewState.observe(this, Observer { state ->
            when (state) {
                is ProfileViewModel.ViewState.ProfileLoaded -> {
                    // render avatar, user name, photos etc.
                    tvName.text = state.profile.name
                }
                is ProfileViewModel.ViewState.ProfileLoadFailure -> {
                    // render error layout with icon, error messages
                }
                ProfileViewModel.ViewState.Loading -> {
                    // render loading animation
                }
            }
        })

        viewModel.actionState.observe(this, Observer { state ->
            when (state) {
                ProfileViewModel.ActionState.NeedsVerification -> {
                    // navigation to Verification screen
                }
                ProfileViewModel.ActionState.NeedsLogOut -> {
                    // Clear data, finish this activity and navigate to LogIn screen?
                }
            }
        })
    }
}
