package ua.nuop.bushniak.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Note(
    @PrimaryKey
    val id: Long? = null,
    val text: String,
    @SerialName("private")
    val isPrivate: Boolean,
    val createdAt: Long = System.currentTimeMillis() / 1000,
    val updatedAt: Long = System.currentTimeMillis() / 1000,
    val deletedAt: Long? = null
)
