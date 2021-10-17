package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.longToString

interface OnActionListener {
    fun onEditClicked(post: Post) = Unit
    fun onRemoveClicked(post: Post) = Unit
    fun onLikeClicked(post: Post) = Unit
    fun onShareClicked(post: Post) = Unit
    fun onVideoClicked(post: Post) = Unit
}

class PostAdapter(
    private val actionListener: OnActionListener
): RecyclerView.Adapter<PostViewHolder>() {
    var posts: List<Post> = emptyList()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(
            CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            actionListener,
        )


    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size
}

class PostViewHolder(
    private  val binding: CardPostBinding,
    private val actionListener: OnActionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            avatar.setImageResource(R.drawable.ic_netology_48dp)
            content.text = post.content
            author.text = post.author
            published.text = post.published
            textView.text = longToString(post.views)
            buttonLike.isChecked = post.likedByMe
            buttonLike.text = "${post.likes}"
            buttonShare.text = "${post.shares}"

            if (post.video == null) {
                video.visibility = View.GONE
            } else {
                video.visibility = View.VISIBLE
            }

            buttonLike.setOnClickListener {
                actionListener.onLikeClicked(post)
            }
            buttonShare.setOnClickListener {
                actionListener.onShareClicked(post)
            }
            video.setOnClickListener {
                actionListener.onVideoClicked(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.post_menu)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.menu_remove -> {
                                actionListener.onRemoveClicked(post)
                                true
                            }
                            R.id.menu_edit -> {
                                actionListener.onEditClicked(post)
                                true
                            }
                            else -> false
                        }
                    }
                    show()
                }
            }

        }
    }

}
