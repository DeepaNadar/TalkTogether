package com.example.talktogether.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.talktogether.MainActivity
import com.example.talktogether.R
import com.example.talktogether.databinding.ActivityNumberBinding
import com.google.firebase.auth.FirebaseAuth

class Number : AppCompatActivity() {

    private lateinit var binding:ActivityNumberBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()

        if(auth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        binding.button.setOnClickListener{
            if(binding.phoneNumber.text!!.isEmpty()){
                Toast.makeText(this,"Please enter your phone number",Toast.LENGTH_SHORT).show()
            }
            else{
                var intent=Intent(this,OTP::class.java)
                intent.putExtra("number",binding.phoneNumber.text!!.toString())
                startActivity(intent)
            }
        }

    }
}