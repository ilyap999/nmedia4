package ru.netology.nmedia

import android.content.Intent
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

            /*val launcherEdit = registerForActivityResult(EditPostActivityContract()) {text ->
                text ?: return@registerForActivityResult
                viewModel.changeContent(text.toString())
                viewModel.save()
            }*/
            launcherEdit.launch(it.content.toString())
            //launcherEdit.launch(it.content.toString())
            //Открыть видимость группы и установить текст
            /*binding.groupEdit.visibility = View.VISIBLE
            binding.labelText.setText(it.content)

            binding.content.setText(it.content)
            binding.content.requestFocus()*/
        }

/*        binding.save.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(context, "Content must not be empty!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
            //Закрыть видимость
            binding.groupEdit.visibility = View.GONE
        }*/

/*        binding.cross.setOnClickListener {
            with(binding.content) {
                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(this)
            }
            //Закрыть видимость
            binding.groupEdit.visibility = View.GONE
        }*/

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
