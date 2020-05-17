package com.example.superchargedmvvm

import kotlin.random.Random

data class Profile(
    val avatar: String,
    val name: String,
    val photos: List<String>
)

class GetProfileInteractor {
    fun getProfile(): State {
        val pureLuck = Random.nextInt(4)
        return when (pureLuck) {
            0 -> {
                val catPerson = Random.nextBoolean()
                State.Success(
                    Profile(
                        "https://example.com/cat.png",
                        if (catPerson) "Cat person" else "Dog person",
                        listOf("https://example.com/cat1.png", "https://example.com/cat2.png")
                    )
                )
            }
            1 -> State.Failure("sth went wrong")
            2 -> State.TnCUpdated
            else -> State.LogOutRequired
        }
    }

    sealed class State {
        data class Success(val profile: Profile) : State()
        data class Failure(val errorMessage: String) : State()
        object TnCUpdated : State()
        object LogOutRequired : State()
    }
}