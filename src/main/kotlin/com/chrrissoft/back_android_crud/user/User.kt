package com.chrrissoft.back_android_crud.user

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document


@Document
data class User(
    @Id
    @Indexed(name = "email")
    val email: String,
    val password: String,
    val notes: List<String> = emptyList(),
)
