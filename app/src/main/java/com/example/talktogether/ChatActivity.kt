package com.example.talktogether

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.talktogether.Adapter.MessageAdapter
import com.example.talktogether.Model.MessageModel
import com.example.talktogether.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var senderUid: String
    private lateinit var receiverUid: String
    private lateinit var senderRoom:String
    private lateinit var receiverRoom:String
    private lateinit var list:ArrayList<MessageModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityChatBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val imageUrl = intent.getStringExtra("imageUrl")
        val name = intent.getStringExtra("name")

        database=FirebaseDatabase.getInstance()
        list=ArrayList()

        senderUid=FirebaseAuth.getInstance().uid.toString()
        receiverUid=intent.getStringExtra("uid")!!

        senderRoom=senderUid+receiverUid
        receiverRoom=receiverUid+senderUid

        binding.imageView.setOnClickListener{
            if(binding.messagebox.text.isEmpty()){
                Toast.makeText(this, "Please enter a message", Toast.LENGTH_SHORT).show()
            }
            else{
                val timestamp = System.currentTimeMillis()
                var message=MessageModel(binding.messagebox.text.toString(), senderUid, timestamp)
                val randomKey=database.reference.push().key
                database.reference.child("chats")
                    .child(senderRoom).child("message").child(randomKey!!)
                    .setValue(message)
                    .addOnSuccessListener {
                        database.reference.child("chats").child(receiverRoom)
                            .child("message").child(randomKey!!)
                            .setValue(message)
                            .addOnSuccessListener {
                                binding.messagebox.text=null
                                Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show()
                            }
                    }
            }
        }
        database.reference.child("chats").child(senderRoom).child("message")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()

                    for(snapshot1 in snapshot.children){
                        val data=snapshot1.getValue(MessageModel::class.java)
                        list.add(data!!)
                    }
                    binding.recyclerView.adapter=MessageAdapter(this@ChatActivity,list)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChatActivity, "Error: $error", Toast.LENGTH_SHORT).show()
                }
            })

        binding.senderName.text = name

        Glide.with(this)
            .load(imageUrl)
            .into(binding.senderImage)
    }
}