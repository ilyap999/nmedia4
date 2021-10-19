package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import java.io.File

class PostRepositoryFileImpl(
    private val context: Context
) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var postId = 1L
    private var posts = emptyList<Post>()
    override val data = MutableLiveData(posts)

    private val filename = "posts.json"
    init {
        val file: File = context.filesDir.resolve(filename)
        if (file.exists()) {
            context.openFileInput(filename).bufferedReader().use {
                posts = gson.fromJson(it, type)
                data.value = posts
            }
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
        context.openFileOutput(filename, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(data.value))
        }
    }
}

