package com.burak.klnkamera

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar ayarı
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)  // Kendi TextView başlığımızı kullanıyoruz

        // Buton tıklamaları
        findViewById<Button>(R.id.btnYeniDugun).setOnClickListener {
            startActivity(Intent(this, AddDugunActivity::class.java))
        }

        findViewById<Button>(R.id.btnOzet).setOnClickListener {
            startActivity(Intent(this, SummaryActivity::class.java))
        }
    }
}
