package com.example.talktogether.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.talktogether.MainActivity
import com.example.talktogether.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class OTP : AppCompatActivity() {

    private lateinit var binding:ActivityOtpBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var verificationID:String
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()

        val builder=AlertDialog.Builder(this)

        builder.setMessage("Loading....")
        builder.setCancelable(false)

        dialog=builder.create()
        dialog.show()

        val phoneNumber="+91 "+intent.getStringExtra("number")

        val options=PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    dialog.dismiss()
                    Toast.makeText(this@OTP,"Please try again ${p0}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)

                    dialog.dismiss()
                    verificationID=p0
                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.button.setOnClickListener{

            if(binding.otp.text!!.isEmpty()){
                Toast.makeText(this,"Please enter OTP",Toast.LENGTH_SHORT).show()
            }
            else {
                dialog.show()
                val credential = PhoneAuthProvider.getCredential(verificationID, binding.otp.text!!.toString())

                auth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uid = auth.currentUser?.uid
                            val usersRef = FirebaseDatabase.getInstance().reference.child("users")
                            usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.hasChild(uid!!)) {
                                        dialog.dismiss()
                                        startActivity(Intent(this@OTP, MainActivity::class.java))
                                        finish()
                                    } else {
                                        dialog.dismiss()
                                        startActivity(Intent(this@OTP, Profile::class.java))
                                        finish()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    dialog.dismiss()
                                    Toast.makeText(this@OTP, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                        } else {
                            dialog.dismiss()
                            Toast.makeText(this, "Error: ${task.exception}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}