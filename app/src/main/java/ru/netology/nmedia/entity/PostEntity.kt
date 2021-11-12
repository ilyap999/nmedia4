package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val content: String = "",
    val published: String = "5 октября в 20:00",
    val author: String = "Илья",
    val likedByMe: Boolean = false,
    val likes: Long = 0L,
    val shares: Long = 0L,
    val views: Long = 0L,
    val video: String? = null
) {
 /*   fun toDto(): Post = with(this) {
        Post(
            id = id,
            author = author,
            content = content,
            published = published,
            likedByMe = likedByMe,
            likes = likes,
            shares = shares,
            views = views,
            video = video
        )
    }*/
 fun toDto() = Post(id, author, content, published, likedByMe, likes, shares, views, video)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.content, dto.published, dto.likedByMe, dto.likes, dto.shares, dto.views, dto.video)

    }
}