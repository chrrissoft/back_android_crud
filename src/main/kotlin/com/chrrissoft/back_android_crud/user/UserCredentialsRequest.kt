package com.chrrissoft.back_android_crud.user

data class UserCredentialsRequest(
    val credentials: UserCredentials, val newPassword: String,
)
