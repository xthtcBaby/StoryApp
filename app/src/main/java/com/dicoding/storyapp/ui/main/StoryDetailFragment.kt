package com.dicoding.storyapp.ui.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.FragmentStoryDetailBinding

class StoryDetailFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentStoryDetailBinding? = null
    private val binding get() = _binding!!

    private val args: StoryDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryDetailBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        binding.apply {
            etDetailName.text = args.name
            etDetailDate.text = args.date
            etDetailDesc.text = args.desc
            Glide.with(view.context)
                .load(args.image)
                .listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        TODO("Not yet implemented")
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        showLoading(false)
                        return false
                    }

                })
                .into(ivStory)
        }
        binding.ivBackDetail.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    private fun showLoading(isLoading: Boolean = false) {
        binding.pbDetail.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAG = "Story detail fragment"
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.iv_back_detail -> {
                findNavController().navigate(R.id.action_storyDetailFragment_to_landingFragment)
            }
        }
    }
}