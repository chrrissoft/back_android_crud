package com.chrrissoft.back_android_crud.user

import com.chrrissoft.back_android_crud.common.CredentialsValidator
import com.chrrissoft.back_android_crud.notes.NotesRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserControllers(
    @Autowired private val repo: UsersRepo,
    @Autowired private val notesRepo: NotesRepo,
    @Autowired private val validator: CredentialsValidator,
) {

    @GetMapping("/all")
    fun readAll(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(repo.findAll())
    }


    @DeleteMapping("/delete")
    fun delete(@RequestBody credentials: UserCredentials): ResponseEntity<Unit> {
        return validator.validate(
            credentials,
            onUserNotFound = { ResponseEntity.notFound().build() },
            onPassNotCorrect = { ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() },
            onSuccess = {
                notesRepo.deleteAllById(it.notes)
                repo.deleteByEmail(credentials.email)
                ResponseEntity.noContent().build()
            }
        )
    }

}
