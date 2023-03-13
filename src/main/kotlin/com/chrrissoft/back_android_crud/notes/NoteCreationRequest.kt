package com.chrrissoft.back_android_crud.notes

import com.chrrissoft.back_android_crud.user.UserCredentials

data class NoteCreationRequest(val note: Note, val credentials: UserCredentials)
