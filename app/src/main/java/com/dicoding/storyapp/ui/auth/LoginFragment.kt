package com.dicoding.storyapp.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dicoding.storyapp.R
import com.dicoding.storyapp.databinding.FragmentLoginBinding
import com.dicoding.storyapp.helper.LoginPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by activityViewModels()

    @Inject
    lateinit var loginPref: LoginPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (loginPref.isLogin()) {
            findNavController().navigate(R.id.action_loginFragment_to_landingFragment)
        }
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        showLoading()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()
        setupViewModel()
        setupAnimation()
        binding.tvSignup.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setToolbar() {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_signup -> findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            R.id.btn_login -> login()
        }
    }

    private fun setupViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        viewModel.loginProc.observe(viewLifecycleOwner) {
            when (it) {
                1 -> {
                    toastMaker(getString(R.string.login_berhasil))
                    findNavController().navigate(R.id.action_loginFragment_to_landingFragment)
                    viewModel.setLoginDone()
                }
                2 -> {
                    toastMaker(getString(R.string.email_pass_salah))
                }
            }

        }
    }

    private fun login() {
        val layoutEmail = binding.tlEmail
        val email = binding.etEmail.text.toString()
        val layoutPassword = binding.tlPass
        val password = binding.etPassword.text.toString()

        if (email.isEmpty() || !TextUtils.isEmpty(layoutEmail.error) || password.isEmpty() || !TextUtils.isEmpty(
                layoutPassword.error
            )
        ) {
            toastMaker(getString(R.string.periksa_form))
        } else
            viewModel.login(email, password)
    }

    private fun toastMaker(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean = false) {
        binding.pbLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupAnimation() {
        ObjectAnimator.ofFloat(binding.imgLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val tlEmail = ObjectAnimator.ofFloat(binding.tlEmail, View.ALPHA, 1f).setDuration(500)
        val tlPass = ObjectAnimator.ofFloat(binding.tlPass, View.ALPHA, 1f).setDuration(500)
        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val tvKiri = ObjectAnimator.ofFloat(binding.tvKiri, View.ALPHA, 1f).setDuration(500)
        val tvSignup = ObjectAnimator.ofFloat(binding.tvSignup, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(tlEmail,tlPass,btnLogin,tvKiri,tvSignup)
            start()
        }
    }
}