package ru.netology.nmedia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFileImpl
import ru.netology.nmedia.repository.PostRepositoryInMemory
import ru.netology.nmedia.repository.PostRepositorySharedPrefsImpl

private val empty = Post()

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryFileImpl(application)
    val data: LiveData<List<Post>> = repository.data
    private val _edited = MutableLiveData(empty)
    val edited: LiveData<Post>
    get() = _edited
    fun likeById(id: Long) {
        repository.likeById(id)
    }
    fun shareById(id: Long) {
        repository.shareById(id)
    }
    fun removeById(id: Long) {
        repository.removeById(id)
    }

    fun changeContent(content: String) {
        _edited.value = _edited.value?.copy(content = content)
    }

    fun save() {
        _edited.value?.also {
            repository.save(it)
            _edited.value = empty
        }
    }

    fun edit(post: Post) {
        _edited.value = post
    }
}
