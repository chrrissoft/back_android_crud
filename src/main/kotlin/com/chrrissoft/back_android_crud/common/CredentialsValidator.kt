package com.chrrissoft.back_android_crud.common

import com.chrrissoft.back_android_crud.user.User
import com.chrrissoft.back_android_crud.user.UserCredentials
import com.chrrissoft.back_android_crud.user.UsersRepo
import org.springframework.stereotype.Component

@Component
class CredentialsValidator(
    private val repo: UsersRepo
) {

    fun <R> validate(
        credentials: UserCredentials,
        onSuccess: (User) -> R,
        onUserNotFound: () -> R,
        onPassNotCorrect: () -> R,
    ): R {
        return if (userNotExist(credentials)) onUserNotFound()
        else if (passNotCorrect(credentials)) onPassNotCorrect()
        else onSuccess(repo.findByEmail(credentials.email).get())
    }

    private fun userNotExist(credentials: UserCredentials): Boolean {
        return !(repo.existsByEmail(credentials.email))
    }

    private fun passNotCorrect(credentials: UserCredentials): Boolean {
        val user = repo.findByEmail(credentials.email)
        return if (userNotExist(credentials)) false
        else user.get().password != credentials.password
    }

}

