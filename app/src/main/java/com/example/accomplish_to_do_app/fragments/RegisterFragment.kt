package com.example.accomplish_to_do_app.fragments

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.accomplish_to_do_app.R
import com.example.accomplish_to_do_app.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterFragment : Fragment() {

    private lateinit var auth:FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        register()
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun register() {
        binding.txtRegisterSignIn.setOnClickListener {
            navController.navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btnRegisterSignUp.setOnClickListener {
            val email = binding.txtRegisterEmail.text.toString()
            val pass = binding.txtRegisterPassword.text.toString()
            val reTypePass = binding.txtRegisterReTypePass.text.toString()

            if(email.isNotEmpty() || pass.isNotEmpty() || reTypePass.isNotEmpty()){
                if(pass == reTypePass){
                    auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(context, "Successfully registered", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate(R.id.action_registerFragment_to_taskFragment)
                        } else {
                            Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }else{
                    Toast.makeText(context, "Password doesn't match" , Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
                Toast.makeText(context, "Please enter all fields" , Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }





}