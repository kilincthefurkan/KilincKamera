package com.kilinc.kamera

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kilinc.kamera.data.DatabaseHelper

class SummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val db = DatabaseHelper(this)
        val toplam = db.toplamKazanc()
        findViewById<TextView>(R.id.textViewOzet).text = "Toplam Kazanç: ₺$toplam"
    }
}
