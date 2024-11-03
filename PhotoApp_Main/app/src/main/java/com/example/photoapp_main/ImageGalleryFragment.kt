package com.example.photoapp_main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.photoapp_main.adapter.ImageAdapter
import com.example.photoapp_main.databinding.FragmentImageGalleryBinding
import com.example.photoapp_main.viewmodel.MainVM
import com.example.photoapp_main.viewmodel.MainVMFactory

class ImageGalleryFragment : DialogFragment() {
    private lateinit var binding: FragmentImageGalleryBinding
    private lateinit var viewModel: MainVM
    private val imageAdapter by lazy {
        ImageAdapter { imageUri ->
            openEditImageScreen(imageUri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val factory = MainVMFactory(requireActivity())
        viewModel = ViewModelProvider(this, factory)[MainVM::class.java]
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeImages()
    }

    private fun initViews() {
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = imageAdapter
        }
    }

    private fun observeImages() {
        viewModel.iamge.observe(viewLifecycleOwner, Observer { images ->
            val imageUris =
                images.map { Uri.parse(it.uri) }
            imageAdapter.submitList(imageUris)
        })
    }

    private fun openEditImageScreen(imageUri: Uri) {
        val intent = Intent(requireContext(), EditImageActivity::class.java)
        intent.putExtra("imageUri", imageUri.toString())
        startActivity(intent)
    }

    companion object {
        fun newInstance(): ImageGalleryFragment {
            return ImageGalleryFragment()
        }
    }
}
