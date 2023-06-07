package com.example.accomplish_to_do_app.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.accomplish_to_do_app.R
import com.example.accomplish_to_do_app.databinding.FragmentSplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

class SplashScreenFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)

        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            if(auth.currentUser != null){ //if the user is logged in
                navController.navigate(R.id.action_splashScreenFragment_to_taskFragment)
            }else{
                navController.navigate(R.id.action_splashScreenFragment_to_loginFragment)
            }
        },2000)
    }

}