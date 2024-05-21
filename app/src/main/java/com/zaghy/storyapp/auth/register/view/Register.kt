package com.zaghy.storyapp.auth.register.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.zaghy.storyapp.R
import com.zaghy.storyapp.auth.register.viewmodel.RegisterViewModel
import com.zaghy.storyapp.auth.register.viewmodel.RegisterViewModelFactory
import com.zaghy.storyapp.databinding.FragmentRegisterBinding
import com.zaghy.storyapp.network.Result
import com.zaghy.storyapp.widget.CustomAlertDialog

class Register : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        val viewModel: RegisterViewModel by viewModels<RegisterViewModel> {
            RegisterViewModelFactory.getInstance(requireContext())
        }
        setupAction(viewModel)
        playAnimation()
        return binding.root
    }

    private fun setupAction(viewModel: RegisterViewModel) {
////        Setup dialog
//        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
//        builder
//            .setMessage("Register Success")
//            .setTitle("Information")
//            .setPositiveButton("Ok") { dialog, _ ->
//                dialog.dismiss()
//
//            }
//        val dialog: AlertDialog = builder.create()

//        setup register button
        binding.registerButton.setOnClickListener {
            viewModel.register(
                name = binding.edRegisterName.text.toString(),
                email = binding.edRegisterEmail.text.toString(),
                password = binding.edRegisterPassword.text.toString()
            ).observe(viewLifecycleOwner) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        CustomAlertDialog("Information",result.data.message.toString(),"Ok",callback={}).show(parentFragmentManager,"REGISTER DIALOG SUCCESS")
                        view?.findNavController()?.navigate(R.id.action_register_to_login)
                    }

                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        CustomAlertDialog("Information",result.error,"Ok",callback={

                        }).show(parentFragmentManager,"REGISTER DIALOG FAILED")
                    }

                    null -> {
//                        do nothing
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageViewRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val namaTextView =
            ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(500)
        val namaEditText =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditText =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditText =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val registerButton =
            ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                namaTextView,
                namaEditText,
                emailTextView,
                emailEditText,
                passwordTextView,
                passwordEditText,
                registerButton
            )
        }.start()
    }

    companion object {
        private const val TAG = "Register"
    }
}