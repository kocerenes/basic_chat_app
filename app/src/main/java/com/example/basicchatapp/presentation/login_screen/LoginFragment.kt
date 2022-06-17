package com.example.basicchatapp.presentation.login_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.basicchatapp.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener{
            auth.signInWithEmailAndPassword(binding.ptEmail.text.toString(),binding.ptPassword.text.toString())
                .addOnSuccessListener {
                    val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
                    findNavController().navigate(action)
                }.addOnFailureListener{ exception ->
                    Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
                }
        }

        binding.btnSignup.setOnClickListener{

            auth.createUserWithEmailAndPassword(binding.ptEmail.text.toString(),binding.ptPassword.text.toString())
                .addOnSuccessListener {
                    val action = LoginFragmentDirections.actionLoginFragmentToChatFragment()
                    findNavController().navigate(action)
                }.addOnFailureListener{ exception ->
                    Toast.makeText(requireContext(),exception.localizedMessage,Toast.LENGTH_LONG).show()
                }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}