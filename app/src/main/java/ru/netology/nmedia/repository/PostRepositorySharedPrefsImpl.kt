package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositorySharedPrefsImpl(
    context: Context
) : PostRepository {

    private val gson = Gson()
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val key = "posts"
    private var postId = 1L
    private var posts = emptyList<Post>()
    override val data = MutableLiveData(posts)

    init {
        prefs.getString(key, null)?.let {
            posts = gson.fromJson(it, type)
            data.value = posts
        }
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            val newPosts = listOf(post.copy(id = postId++))
            data.value = newPosts + data.value.orEmpty()
            sync()
            return
        }
        data.value = data.value?.map {
            if (it.id == post.id) {
                it.copy(content = post.content)
            } else {
                it
            }
        }
        sync()
    }

    override fun removeById(id: Long) {
        data.value = data.value?.filter { it.id != id }
        sync()
    }

    override fun likeById(id: Long) {
        val currentPosts: List<Post> = data.value ?: return
        val result = currentPosts.map {
            if (it.id != id) it else
                it.copy(likedByMe = !it.likedByMe, likes = if (it.likedByMe) it.likes -1 else it.likes +1)
        }
        data.value = result
        sync()
    }

    override fun shareById(id: Long) {
        val currentPosts: List<Post> = data.value ?: return
        val result = currentPosts.map {
            if (it.id == id) it.copy(shares = it.shares +1) else it
        }
        data.value = result
        sync()
    }

    private  fun sync() {
        with(prefs.edit()) {
            putString(key, gson.toJson(data.value))
            apply()
        }
    }
}

