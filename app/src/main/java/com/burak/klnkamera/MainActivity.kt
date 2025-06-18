package com.burak.klnkamera

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dugungelirtakip.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnYeniDugun.setOnClickListener {
            startActivity(Intent(this, AddDugunActivity::class.java))
        }

        binding.btnOzet.setOnClickListener {
            startActivity(Intent(this, SummaryActivity::class.java))
        }
    }
}