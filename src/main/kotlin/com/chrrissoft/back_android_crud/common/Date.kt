package com.chrrissoft.back_android_crud.common

import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Date(val day: Int, val month: Int, val year: Int)
