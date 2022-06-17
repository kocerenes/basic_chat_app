package com.example.basicchatapp.presentation.chat_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.basicchatapp.R
import com.example.basicchatapp.databinding.FragmentChatBinding
import com.example.basicchatapp.model.Chat
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
    private lateinit var adapter : ChatRecyclerAdapter
    private var chats = arrayListOf<Chat>()

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

        adapter = ChatRecyclerAdapter()
        binding.chatRecycler.adapter = adapter
        binding.chatRecycler.layoutManager = LinearLayoutManager(requireContext())

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


        firestore.collection("Chats").orderBy("date").addSnapshotListener{value, error ->

            if (error != null){
                Toast.makeText(requireContext(),error.localizedMessage,Toast.LENGTH_LONG).show()
            }else{
                if (value != null){
                    if (value.isEmpty){
                        Toast.makeText(requireContext(),"Mesaj yok",Toast.LENGTH_LONG).show()
                    }else{
                        val documents = value.documents
                        chats.clear()
                        for (document in documents){
                            val user = document.get("user") as String
                            val text = document.get("text") as String
                            val chat = Chat(user,text)
                            chats.add(chat)
                            adapter.chats = chats
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}