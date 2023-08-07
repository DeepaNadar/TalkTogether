package com.example.talktogether

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.talktogether.Activity.Number
import com.example.talktogether.Adapter.ViewPagerAdapter
import com.example.talktogether.UserInterface.ChatFragment
import com.example.talktogether.UserInterface.StatusFragment
import com.example.talktogether.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private var binding:ActivityMainBinding ?= null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        val fragmentArrayList = ArrayList<Fragment>()

        fragmentArrayList.add(ChatFragment())

        auth=FirebaseAuth.getInstance()

        if(auth.currentUser==null){
            startActivity(Intent(this,Number::class.java))
            finish()
        }

        val adapter=ViewPagerAdapter(this,supportFragmentManager,fragmentArrayList)

        binding!!.viewPager.adapter=adapter

        binding!!.tabLayout.setupWithViewPager(binding!!.viewPager)
    }
    override fun onBackPressed() {
        val exitIntent = Intent(Intent.ACTION_MAIN)
        exitIntent.addCategory(Intent.CATEGORY_HOME)
        exitIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(exitIntent)
    }
}