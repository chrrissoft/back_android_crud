package com.chrrissoft.back_android_crud.auth

import com.chrrissoft.back_android_crud.common.CredentialsValidator
import com.chrrissoft.back_android_crud.user.User
import com.chrrissoft.back_android_crud.user.UserCredentials
import com.chrrissoft.back_android_crud.user.UserCredentialsRequest
import com.chrrissoft.back_android_crud.user.UsersRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
class AuthController(
    private val usersRepo: UsersRepo,
    @Autowired private val validator: CredentialsValidator,
    ) {

    @PostMapping("/logUp")
    fun logUp(@RequestBody credentials: UserCredentials): ResponseEntity<User> {
        if (usersRepo.existsByEmail(credentials.email)) return ResponseEntity.badRequest().build()
        val user = User(credentials.email, credentials.password)
        return ResponseEntity.status(HttpStatus.CREATED).body(usersRepo.save(user))
    }


    @PostMapping("/logIn")
    fun logIn(@RequestBody credentials: UserCredentials): ResponseEntity<User> {
        return validator.validate(
            credentials,
            onUserNotFound = { ResponseEntity.notFound().build() },
            onPassNotCorrect = { ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() },
            onSuccess = { ResponseEntity.ok(it) }
        )
    }


    @PatchMapping("/updatePassword")
    fun updatePassword(@RequestBody request: UserCredentialsRequest): ResponseEntity<User> {
        return validator.validate(
            request.credentials,
            onUserNotFound = { ResponseEntity.notFound().build() },
            onPassNotCorrect = { ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() },
            onSuccess = {
                val newUser = it.copy(password = request.newPassword)
                ResponseEntity.ok(usersRepo.save(newUser))
            }
        )
    }

}
