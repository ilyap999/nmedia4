package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ru.netology.nmedia.databinding.FragmentEditPostBinding
import ru.netology.nmedia.utils.AndroidUtils
import androidx.navigation.fragment.findNavController

class EditPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditPostBinding.inflate(layoutInflater)
        val viewModel: PostViewModel by activityViewModels()
        binding.edit.requestFocus()
        arguments?.getString("TEXT_KEY")?.also {
            binding.edit.setText(it)
        }
        binding.save.setOnClickListener {
            val result = binding.edit.text
            if (!result.isNullOrBlank()) {
                viewModel.changeContent(result.toString())
                viewModel.save()
                AndroidUtils.hideKeyboard(binding.root)
            }

            findNavController().navigateUp()
        }
        return binding.root
    }

}
