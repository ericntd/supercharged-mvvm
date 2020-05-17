package com.example.superchargedmvvm

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfileActivity : AppCompatActivity() {
    private val tvName: TextView by lazy {
        findViewById<TextView>(R.id.tv_name)
    }

    private val viewModel: ProfileViewModel by lazy {
        val getProfileInteractor = GetProfileInteractor()
        val viewModelFactory = object : ViewModelProvider.Factory {
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
    }

    private fun setUpObservation() {
        viewModel.viewState.observe(this, Observer { state ->
            when (state) {
                is ProfileViewModel.ViewState.ProfileLoaded -> {
                    // render avatar, user name, photos etc.
                    tvName.text = state.profile.name
                }
                is ProfileViewModel.ViewState.ProfileLoadFailure -> {
                    tvName.text = "Something went wrong"
                }
                ProfileViewModel.ViewState.Loading -> {
                    // render loading animation
                    tvName.text = "Loading..."
                }
            }
        })

        viewModel.actionState.observe(this, Observer { state ->
            when (state) {
                ProfileViewModel.ActionState.TnCUpdated -> {
                    AlertDialog.Builder(this).setTitle("Our Terms & Conditions have been updated")
                        .setMessage("Please read... and accept")
                        .setPositiveButton("Accept") { dialog, _ ->
                            println("User accepts T & C")
                            dialog.dismiss()
                        }.setCancelable(false).show()
                }
                ProfileViewModel.ActionState.NeedsLogOut -> {
                    println("ProfileViewModel.ActionState.NeedsLogOut")
                    startActivity(Intent(this, LogInActivity::class.java))
                    finish()
                }
            }
        })
    }
}
