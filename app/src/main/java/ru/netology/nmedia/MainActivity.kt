package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.adapter.OnActionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter (

            object : OnActionListener {
                override fun onEditClicked(post: Post) {
                    viewModel.edit(post)
                }

                override fun onRemoveClicked(post: Post) {
                    viewModel.removeById(post.id)
                }

                override fun onLikeClicked(post: Post) {
                    viewModel.likeById(post.id)
                }

                override fun onShareClicked(post: Post) {
                    viewModel.shareById(post.id)
                    // intent шаринга
                    val intent = Intent(Intent.ACTION_SEND).apply{
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(intent, null)
                    startActivity(shareIntent)

                }

                override fun onVideoClicked(post: Post) {
                    val youtubeIntent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    startActivity(youtubeIntent)
                }
            }
        )
        binding.posts.adapter = adapter
        viewModel.data.observe(this) { posts -> adapter.posts = posts }

        val launcherEdit = registerForActivityResult(EditPostActivityContract()) {text ->
            text ?: return@registerForActivityResult
            viewModel.changeContent(text.toString())
            viewModel.save()
        }
        viewModel.edited.observe(this) {
            if (it.id == 0L) {
                return@observe
            }

            launcherEdit.launch(it.content.toString())
        }

        val launcher = registerForActivityResult(NewPostActivityContract()) {text ->
            text ?: return@registerForActivityResult
            viewModel.changeContent(text.toString())
            viewModel.save()
        }
        binding.newPost.setOnClickListener {
            launcher.launch()
        }
    }
}
