package com.example.data.remote.dto

import com.example.domain.model.ExampleItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExampleDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String
) {
    fun toDomain(): ExampleItem = ExampleItem(
        id = id,
        title = title,
        description = description
    )

    companion object {
        fun fromDomain(domain: ExampleItem): ExampleDto = ExampleDto(
            id = domain.id,
            title = domain.title,
            description = domain.description
        )
    }
}
