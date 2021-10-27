package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.adapter.OnActionListener
import ru.netology.nmedia.adapter.PostAdapter
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.dto.Post

class FeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val viewModel: PostViewModel by activityViewModels()
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

                // TODO доделать открытие карточки поста на фрагменте
                override fun onContentClicked(post: Post) {
                    findNavController().navigate(R.id.action_feedFragment_to_cardPostFragment, Bundle().apply {
                        putString("TEXT_KEY", post.content)
                        putString("LIKE_KEY", post.likes.toString())
                    })
                }
            }
        )
        binding.posts.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts -> adapter.posts = posts }

        viewModel.edited.observe(viewLifecycleOwner) {
            if (it.id == 0L) {
                return@observe
            }

            findNavController().navigate(R.id.action_feedFragment_to_editPostFragment, Bundle().apply {
                putString("TEXT_KEY", it.content)})
        }

        binding.newPost.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root

    }
}
