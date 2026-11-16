package com.example.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExampleItem(
    val id: String,
    val title: String,
    val description: String
)
