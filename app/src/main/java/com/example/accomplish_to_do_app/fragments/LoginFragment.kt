package com.example.accomplish_to_do_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.accomplish_to_do_app.R
import com.example.accomplish_to_do_app.databinding.FragmentLoginBinding
import com.example.accomplish_to_do_app.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        login()
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun login() {
        binding.txtLoginRegister.setOnClickListener {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.txtLoginEmail.text.toString()
            val pass = binding.txtLoginPass.text.toString()

            if (email.isNotEmpty() || pass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Welcome back!", Toast.LENGTH_SHORT)
                            .show()
                        navController.navigate(R.id.action_loginFragment_to_taskFragment)
                    } else {
                        Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}


