package com.dicoding.storyapp.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.FragmentAddStoryBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddStoryFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAddStoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CameraViewModel by activityViewModels()

    private var getFile: File? = null

    private val args: AddStoryFragmentArgs by navArgs()

    private lateinit var currentPhotoPath: String

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, requireContext())
                getFile = myFile
                binding.imgPreview.setImageURI(uri)
                toastMaker(getString(R.string.berhasil_milih_gambar))
            }
        } else
            toastMaker(getString(R.string.gagal_milih_gambar))
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val myFile = File(currentPhotoPath)

            myFile.let { file ->
                getFile = file
                binding.imgPreview.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStoryBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setupModel()
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.btnGaleri.setOnClickListener(this)
        binding.btnCamera.setOnClickListener(this)
        binding.btnUnggah.setOnClickListener(this)
        checkIsFromCamera()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupModel() {
        viewModel.uploadProc.observe(viewLifecycleOwner) {
            when (it) {
                1 -> {
                    toastMaker(getString(R.string.upload_berhasil))
                    findNavController().navigate(R.id.action_addStoryFragment_to_landingFragment)
                    viewModel.setUploadDone()
                }
                2 -> {
                    toastMaker(getString(R.string.gagal_upload_story))
                }
            }
        }
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_galeri -> {
                openIntentGaleri()
            }
            R.id.btn_camera -> {
                startTakePhoto()
            }
            R.id.btn_unggah -> {
                uploadStory()
            }
        }
    }

    private fun uploadStory() {
        val description = binding.etDeskripsi.text.toString()
        if (getFile != null && !description.isEmpty()) {
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file?.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file?.name,
                requestImageFile!!
            )
            viewModel.uploadStory(imageMultipart,description)
        } else {
            toastMaker(getString(R.string.silahkan_memilih_gambar))
        }
    }

    private fun openCameraFragment() {
        findNavController().navigate(R.id.action_addStoryFragment_to_cameraFragment)
    }

    private fun openIntentGaleri() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.pilih_gambar))
        launcherIntentGallery.launch(chooser)
    }

    private fun checkIsFromCamera(){
        if (args.cameraArgs?.img?.exists() == true){
            val isBackCamera = args.cameraArgs!!.isBackCamera
            val file = args.cameraArgs?.img
            getFile = file
            file?.let { file ->
                rotateFile(file, isBackCamera)
                binding.imgPreview.setImageBitmap(BitmapFactory.decodeFile(file?.path))
            }
            toastMaker(getString(R.string.berhasil_milih_gambar))
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)

        createTempFile(requireActivity().application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.dicoding.storyapp",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun toastMaker(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "Add story fragment"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}