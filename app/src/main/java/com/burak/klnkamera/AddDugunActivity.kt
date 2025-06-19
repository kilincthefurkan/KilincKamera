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
        setContentView(R.layout.activity_add_dugun)

        val db = DatabaseHelper(this)

        val editTarih = findViewById<EditText>(R.id.editTarih)
        val editKazanc = findViewById<EditText>(R.id.editKazanc)
        val editBahsis = findViewById<EditText>(R.id.editBahsis)
        val editMasraf = findViewById<EditText>(R.id.editMasraf)
        val editAciklama = findViewById<EditText>(R.id.editAciklama)
        val spinnerKonum = findViewById<Spinner>(R.id.spinnerKonum)
        val btnKaydet = findViewById<Button>(R.id.btnKaydet)


        // DatePicker gösterme
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

            datePicker.show()  // Bunu ekledik
        }

        btnKaydet.setOnClickListener {
            val tarihStr = editTarih.text.toString().trim()
            val kazancDouble = editKazanc.text.toString().toDoubleOrNull() ?: 0.0
            val bahsisDouble = editBahsis.text.toString().toDoubleOrNull() ?: 0.0
            val masrafDouble = editMasraf.text.toString().toDoubleOrNull() ?: 0.0
            val konum = spinnerKonum.selectedItem.toString()
            val aciklama = editAciklama.text.toString().trim()

            if (tarihStr.isEmpty()) {
                Toast.makeText(this, "Lütfen tarih giriniz", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.ekleDugun(tarihStr, kazancDouble, bahsisDouble, masrafDouble, konum, aciklama)
            Toast.makeText(this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show()
            finish()
        }


        val konumlar = arrayOf("Izmir", "Aydin", "Diğer")
        spinnerKonum.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, konumlar)


    }
}
