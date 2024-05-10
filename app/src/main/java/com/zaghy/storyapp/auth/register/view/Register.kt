package com.zaghy.storyapp.auth.register.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zaghy.storyapp.R
import com.zaghy.storyapp.databinding.FragmentRegisterBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Register.newInstance] factory method to
 * create an instance of this fragment.
 */
class Register : Fragment() {
    private lateinit var binding:FragmentRegisterBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater,container,false)
        setupAction()
        playAnimation()
        return binding.root
    }

    private fun setupAction(){
        binding.registerButton.setOnClickListener {
//            TODO : REGISTER FUNCTION
        }
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.imageViewRegister,View.TRANSLATION_X,-30f,30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView,View.ALPHA,1f).setDuration(500)
        val namaTextView = ObjectAnimator.ofFloat(binding.nameTextView,View.ALPHA,1f).setDuration(500)
        val namaEditText = ObjectAnimator.ofFloat(binding.nameEditTextLayout,View.ALPHA,1f).setDuration(500)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView,View.ALPHA,1f).setDuration(500)
        val emailEditText = ObjectAnimator.ofFloat(binding.emailEditTextLayout,View.ALPHA,1f).setDuration(500)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView,View.ALPHA,1f).setDuration(500)
        val passwordEditText = ObjectAnimator.ofFloat(binding.passwordEditTextLayout,View.ALPHA,1f).setDuration(500)
        val registerButton = ObjectAnimator.ofFloat(binding.registerButton,View.ALPHA,1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title,namaTextView,namaEditText,emailTextView,emailEditText,passwordTextView,passwordEditText,registerButton)
        }.start()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Register.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Register().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}