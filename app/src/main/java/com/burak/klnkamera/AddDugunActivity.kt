package com.kilinc.kamera

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.kilinc.kamera.data.DatabaseHelper

class AddDugunActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_dugun)

        val db = DatabaseHelper(this)

        val tarih = findViewById<EditText>(R.id.editTarih)
        val dugunSayisi = findViewById<EditText>(R.id.editDugunSayisi)
        val kazanc = findViewById<EditText>(R.id.editKazanc)
        val bahsis = findViewById<EditText>(R.id.editBahsis)
        val masraf = findViewById<EditText>(R.id.editMasraf)
        val btnKaydet = findViewById<Button>(R.id.btnKaydet)

        btnKaydet.setOnClickListener {
            db.ekleDugun(
                tarih.text.toString(),
                dugunSayisi.text.toString().toIntOrNull() ?: 0,
                kazanc.text.toString().toDoubleOrNull() ?: 0.0,
                bahsis.text.toString().toDoubleOrNull() ?: 0.0,
                masraf.text.toString().toDoubleOrNull() ?: 0.0
            )
            Toast.makeText(this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
