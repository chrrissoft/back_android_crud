package com.chrrissoft.back_android_crud.notes

import com.chrrissoft.back_android_crud.common.CredentialsValidator
import com.chrrissoft.back_android_crud.user.User
import com.chrrissoft.back_android_crud.user.UserCredentials
import com.chrrissoft.back_android_crud.user.UsersRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/notes")
class NotesController(
    @Autowired private val repo: NotesRepo,
    @Autowired private val userRepo: UsersRepo,
    @Autowired private val validator: CredentialsValidator,
) {

    @GetMapping
    fun read(@RequestBody request: NotesRequest): ResponseEntity<List<Note>> {
        val response = mutableListOf<Note>()
        request.ids.forEach { note ->
            repo.findById(note).let {
                if (it.isPresent) {
                    response.add((it.get()))
                }
            }
        }
        return ResponseEntity.ok(response)
    }


    @GetMapping("/all")
    fun readAll(): ResponseEntity<List<Note>> {
        return ResponseEntity.ok(repo.findAll())
    }


    @PostMapping("/create")
    fun create(@RequestBody request: NoteCreationRequest): ResponseEntity<Note> {

        return validator.validate(
            request.credentials,
            onUserNotFound = { ResponseEntity.notFound().build() },
            onPassNotCorrect = { ResponseEntity.status(HttpStatus.UNAUTHORIZED).build() },
            onSuccess = {
                val savedNote = repo.save(request.note)
                val user = userRepo.findByEmail(request.credentials.email).get()
                val userNotes = user.notes.toMutableList() + savedNote.id
                updateUserNotes(request.credentials, userNotes)
                ResponseEntity.status(HttpStatus.CREATED).body(savedNote)
            },
        )

    }


    @PatchMapping("/update")
    fun update(@RequestBody note: Note): ResponseEntity<Note> {
        if (!repo.existsById(note.id)) return ResponseEntity.notFound().build()
        return ResponseEntity.ok(repo.save(note))
    }


    @DeleteMapping("/remove/{id}")
    fun delete(@PathVariable id: String, @RequestBody credentials: UserCredentials): ResponseEntity<Unit> {
        return if (repo.existsById(id)) {
            repo.deleteById(id)

            userRepo.findByEmail(credentials.email).let {
                if (it.isPresent) {
                    val newNotes = it.get().notes.toMutableList()
                    newNotes.remove(id)
                    updateUserNotes(credentials, newNotes)
                }
            }

            ResponseEntity.noContent().build()
        } else ResponseEntity.notFound().build()
    }


    private fun updateUserNotes(credentials: UserCredentials, notes: List<String>) {
        userRepo.save(User(credentials.email, credentials.password, notes))
    }

}
