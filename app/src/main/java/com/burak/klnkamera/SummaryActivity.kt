package com.burak.klnkamera

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.burak.klnkamera.data.DatabaseHelper
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import android.widget.Button
import android.widget.Toast


class SummaryActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var formatter: DecimalFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        db = DatabaseHelper(this)

        // Türkçe locale ile formatlayıcı, binlik ayraç nokta, ondalık virgül (ondalık göstermiyoruz)
        formatter = DecimalFormat("#,###", DecimalFormatSymbols(Locale("tr", "TR")).apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        })

        guncelleOzet()
    }

    private fun guncelleOzet() {
        // Toplamlar veritabanından çekiliyor
        val toplamKazanc = db.sumColumn("kazanc")
        val toplamBahsis = db.sumColumn("bahsis")
        val toplamMasraf = db.sumColumn("masraf")

        // Ciro = Kazanç + Bahşiş
        val ciro = toplamKazanc + toplamBahsis

        // Kar = Ciro - Masraf
        val kar = ciro - toplamMasraf

        // Formatlayıp TextView'lere yazıyoruz
        findViewById<TextView>(R.id.textViewToplamKazanc).text = "Toplam Kazanç: ₺${formatter.format(toplamKazanc)}"
        findViewById<TextView>(R.id.textViewToplamBahsis).text = "Toplam Bahşiş: ₺${formatter.format(toplamBahsis)}"
        findViewById<TextView>(R.id.textViewToplamMasraf).text = "Toplam Masraf: ₺${formatter.format(toplamMasraf)}"
        findViewById<TextView>(R.id.textViewCiro).text = "Ciro: ₺${formatter.format(ciro)}"
        findViewById<TextView>(R.id.textViewKar).text = "Kar: ₺${formatter.format(kar)}"
        findViewById<Button>(R.id.btnVerileriSifirla).setOnClickListener {
            db.tumVerileriSil()
            Toast.makeText(this, "Tüm veriler silindi", Toast.LENGTH_SHORT).show()
            guncelleOzet() // Özetleri güncelle
        }
    }
}
