package com.chrrissoft.back_android_crud.user

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@EnableMongoRepositories
interface UsersRepo : MongoRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
    fun deleteByEmail(email: String)
    fun existsByEmail(email: String) : Boolean
}
