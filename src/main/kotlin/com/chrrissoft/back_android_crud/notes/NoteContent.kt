package com.chrrissoft.back_android_crud.notes

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class NoteContent(val title: String, val body: String)
