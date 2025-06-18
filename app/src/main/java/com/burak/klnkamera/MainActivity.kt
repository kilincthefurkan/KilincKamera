package com.burak.klnkamera

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnYeniDugun).setOnClickListener {
            startActivity(Intent(this, AddDugunActivity::class.java))
        }

        findViewById<Button>(R.id.btnOzet).setOnClickListener {
            startActivity(Intent(this, SummaryActivity::class.java))
        }

    }
}
