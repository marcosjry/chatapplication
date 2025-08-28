package com.marcos.chatapplication.domain.model

data class User(
    val uid: String = "",
    val email: String? = null,
    val phone: String? = null,
    val username: String? = null
)
