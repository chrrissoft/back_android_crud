package com.chrrissoft.back_android_crud.notes

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
@EnableMongoRepositories
interface NotesRepo : MongoRepository<Note, Long> {
    fun existsById(id: String) : Boolean
    fun findById(id: String) : Optional<Note>
    fun deleteById(id: String)

    fun deleteAllById(list: List<String>) {
        list.forEach { deleteById(it) }
    }

}
