package com.burak.klnkamera

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.burak.klnkamera.data.DatabaseHelper

class SummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val db = DatabaseHelper(this)
        val toplam = db.toplamKazanc()
        val sonuc = getString(R.string.toplam_kazanc, toplam)
        findViewById<TextView>(R.id.textViewOzet).text = sonuc
    }

}
