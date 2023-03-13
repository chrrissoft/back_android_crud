package com.chrrissoft.back_android_crud.notes

import com.chrrissoft.back_android_crud.common.Date
import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Note(
    @Id
    val id: String = ObjectId().toString(),
    val createdDate: Date,
    val content: NoteContent,
    val reminderDates: List<Date>,
)
