package com.zaghy.storyapp.auth.welcome.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.zaghy.storyapp.R
import com.zaghy.storyapp.auth.welcome.viewmodel.WelcomeViewModel
import com.zaghy.storyapp.auth.welcome.viewmodel.WelcomeViewModelFactory
import com.zaghy.storyapp.databinding.FragmentWelcomeBinding

class Welcome : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        val viewModel:WelcomeViewModel by viewModels<WelcomeViewModel> {
            WelcomeViewModelFactory.getInstance(requireContext())
        }

        viewModel.getUser().observe(viewLifecycleOwner){
            if (it != null){
                view?.findNavController()?.navigate(R.id.action_welcome_to_homepage)
            }
        }
//        Setup Action of each button
        setupAction()
//        Play the animation
        playAnimation()


        return binding.root

    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.loginPageBtn, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.registerPageButton,View.ALPHA,1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.titleswitch,View.ALPHA,1f).setDuration(500)
        val subTitle = ObjectAnimator.ofFloat(binding.subTitle,View.ALPHA,1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login,register)
        }
        AnimatorSet().apply {
            playSequentially(title,subTitle,together)
            start()
        }
    }

    private fun setupAction() {
        binding.loginPageBtn.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_welcome_to_login)
        }
        binding.registerPageButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_welcome_to_register)
        }
    }


}