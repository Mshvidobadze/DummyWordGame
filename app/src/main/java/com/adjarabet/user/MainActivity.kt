package com.adjarabet.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.adjarabet.user.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(BuildConfig.FLAVOR == "bot"){
            binding.txtGameTitle.text = "Dummy Word Game Bot"
            binding.btnPlay.visibility = View.INVISIBLE
        }

        binding.btnPlay.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            this.startActivity(intent)
        }

    }




}