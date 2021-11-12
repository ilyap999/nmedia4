package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepository {

   /* override fun getAll(): LiveData<List<Post>> = dao.getAll().map {
        it.map {
        }
    }*/
   override fun getAll() = Transformations.map(dao.getAll()) { list ->
       list.map {
           Post(it.id, it.author, it.content, it.published, it.likedByMe, it.likes, it.shares, it.views, it.video)
       }
   }

    override fun save(post: Post) {
        dao.insert(PostEntity.fromDto(post))
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)

    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}
