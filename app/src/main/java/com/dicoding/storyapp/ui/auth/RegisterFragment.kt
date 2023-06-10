package com.dicoding.storyapp.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        showLoading()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setupViewModel()
        setupAnimation()
        binding.tvLogin.setOnClickListener(this)
        binding.btnSignup.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tv_login -> findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            R.id.btn_signup -> register()
        }
    }

    private fun setupViewModel(){
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.isRegistered.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                AlertDialog.Builder(requireActivity())
                    .setMessage(getString(R.string.akun_berhasil_dibuat))
                    .setCancelable(false)
                    .setPositiveButton("Ok") { _, _ ->
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                    .create()
                    .show()
            } else {
                toastMaker(getString(R.string.email_digunakan))
            }
        }
    }

    private fun register() {
        val nama = binding.etNama.text.toString()
        val layoutEmail = binding.tlEmail
        val email = binding.etEmail.text.toString()
        val layoutPassword = binding.tlPass
        val password = binding.etPassword.text.toString()


        if (nama.isEmpty() || email.isEmpty() || !TextUtils.isEmpty(layoutEmail.error) || password.isEmpty() || !TextUtils.isEmpty(layoutPassword.error))
            toastMaker("Periksa kembali form!")
        else
            viewModel.register(nama,email,password)
    }

    private fun toastMaker(msg: String){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean = false) {
        binding.pbRegister.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.imgLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tlNama = ObjectAnimator.ofFloat(binding.tlNama, View.ALPHA, 1f).setDuration(500)
        val tlEmail = ObjectAnimator.ofFloat(binding.tlEmail, View.ALPHA, 1f).setDuration(500)
        val tlPass = ObjectAnimator.ofFloat(binding.tlPass, View.ALPHA, 1f).setDuration(500)
        val btnSignup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(500)
        val tvKiri = ObjectAnimator.ofFloat(binding.tvKiri, View.ALPHA, 1f).setDuration(500)
        val tvLogin = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(tlNama,tlEmail,tlPass,btnSignup,tvKiri,tvLogin)
            start()
        }
    }
}