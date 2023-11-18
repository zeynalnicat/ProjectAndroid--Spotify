package com.example.spotifyclone.Login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.spotifyclone.HomeActivity
import com.example.spotifyclone.R
import com.example.spotifyclone.databinding.FragmentLoginBinding

class Login : Fragment() {
    private lateinit var binding : FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)


        binding.navBack.setOnClickListener {
            val navigation  = Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
            navigation.popBackStack(R.id.startFragment,false)
        }

        binding.btnNext.setOnClickListener{
            val intent = Intent(requireActivity() , HomeActivity::class.java )
            startActivity(intent)
            requireActivity().finish()
        }
        return binding.root
    }

}