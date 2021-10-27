package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.netology.nmedia.databinding.CardPostBinding


class CardPostFragment: Fragment() {

    // TODO доделать открытие карточки поста на фрагменте

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = CardPostBinding.inflate(layoutInflater)

        arguments?.getString("TEXT_KEY")?.also {
            binding.content.text = it
        }
        arguments?.getString("LIKE_KEY")?.also {
            binding.buttonLike.text = it
        }

        return binding.root
    }
}
