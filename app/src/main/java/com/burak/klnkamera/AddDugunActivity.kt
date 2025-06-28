package com.burak.klnkamera

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.burak.klnkamera.data.DatabaseHelper
import java.util.*

class AddDugunActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_dugun)

        val db = DatabaseHelper(this)

        val editTarih = findViewById<EditText>(R.id.editTarih)
        val editKazanc = findViewById<EditText>(R.id.editKazanc)
        val editBahsis = findViewById<EditText>(R.id.editBahsis)
        val editMasraf = findViewById<EditText>(R.id.editMasraf)
        val editAciklama = findViewById<EditText>(R.id.editAciklama)
        val spinnerKonum = findViewById<AutoCompleteTextView>(R.id.spinnerKonum)
        val btnKaydet = findViewById<Button>(R.id.btnKaydet)

        // Konum listesi ayarı
        val konumListesi = listOf("İzmir", "Aydın", "Diğer")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, konumListesi)
        spinnerKonum.setAdapter(adapter)

        // Takvim seçimi
        editTarih.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val aylar = arrayOf(
                "Ocak", "Şubat", "Mart", "Nisan", "Mayıs", "Haziran",
                "Temmuz", "Ağustos", "Eylül", "Ekim", "Kasım", "Aralık"
            )

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                val secilenTarih = "$d ${aylar[m]} $y"
                editTarih.setText(secilenTarih)
            }, year, month, day)

            datePicker.show()
        }

        // Kaydet butonu işlemi
        btnKaydet.setOnClickListener {
            val tarihStr = editTarih.text.toString().trim()
            val kazancDouble = editKazanc.text.toString().toDoubleOrNull() ?: 0.0
            val bahsisDouble = editBahsis.text.toString().toDoubleOrNull() ?: 0.0
            val masrafDouble = editMasraf.text.toString().toDoubleOrNull() ?: 0.0
            val konum = spinnerKonum.text.toString().trim()  // DÜZELTİLDİ
            val aciklama = editAciklama.text.toString().trim()

            if (tarihStr.isEmpty()) {
                Toast.makeText(this, "Lütfen tarih giriniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.ekleDugun(tarihStr, kazancDouble, bahsisDouble, masrafDouble, konum, aciklama)
            Toast.makeText(this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
