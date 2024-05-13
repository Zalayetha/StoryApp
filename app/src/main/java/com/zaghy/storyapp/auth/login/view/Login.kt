package com.zaghy.storyapp.auth.login.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.zaghy.storyapp.R
import com.zaghy.storyapp.auth.login.viewmodel.LoginViewModel
import com.zaghy.storyapp.auth.login.viewmodel.LoginViewModelFactory
import com.zaghy.storyapp.databinding.FragmentLoginBinding
import com.zaghy.storyapp.local.datastore.Muser
import com.zaghy.storyapp.network.Result
import com.zaghy.storyapp.widget.CustomAlertDialog

class Login : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
//        setup viewmodel
        val viewModel: LoginViewModel by viewModels<LoginViewModel> {
            LoginViewModelFactory.getInstance(context = requireContext())
        }
        setupAction(viewModel)
        playAnimation()
        return binding.root
    }

    private fun setupAction(viewModel: LoginViewModel) {
        binding.loginButton.setOnClickListener {
            viewModel.login(
                binding.edLoginEmail.text.toString(),
                binding.edLoginPassword.text.toString()
            )
                .observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            result.data.loginResult?.let {
                                viewModel.saveTokenAndNavigate(
                                    Muser(
                                        id = it.userId ?: "",
                                        name = it.name ?: "",
                                        token = it.token ?: ""
                                    )
                                )
                            }

                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE
                            CustomAlertDialog("Information", result.error, "Ok", callback = {

                            }).show(
                                parentFragmentManager,
                                "LOGIN DIALOG FAILED"
                            )
                        }

                        null -> {
                            //do nothing
                        }
                    }
                }
        }

        viewModel.navigateToHomePage.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate) {
                view?.findNavController()?.navigate(R.id.action_login_to_homepage)
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageViewLogin, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditText =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditText =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val loginButton =
            ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditText,
                passwordTextView,
                passwordEditText,
                loginButton
            )
        }.start()
    }

    companion object {
        private const val TAG = "Login"
    }
}