package com.example.basicchatapp.presentation.chat_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.basicchatapp.R
import com.example.basicchatapp.databinding.FragmentChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ChatFragment : Fragment() {

    private var _binding : FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore : FirebaseFirestore
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivSend.setOnClickListener {
            auth.currentUser?.let {
                val user = it.email
                val chatText = binding.ptEnterMessage.text.toString()
                val date = FieldValue.serverTimestamp()

                val dataMap = HashMap<String,Any>()
                dataMap.put("text",chatText)
                dataMap.put("user",user!!)
                dataMap.put("date",date)

                firestore.collection("Chats").add(dataMap)
                    .addOnSuccessListener {
                        binding.ptEnterMessage.setText("")
                    }
                    .addOnFailureListener{
                        Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                        binding.ptEnterMessage.setText("")
                    }

            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}