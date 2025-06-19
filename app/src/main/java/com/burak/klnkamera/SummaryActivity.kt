package com.burak.klnkamera

import android.app.AlertDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.burak.klnkamera.data.DatabaseHelper
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class SummaryActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper
    private lateinit var formatter: DecimalFormat

    private lateinit var listViewDugunler: ListView
    private lateinit var adapter: ArrayAdapter<String>

    // Düğün listesini id, tarih, kazanç içeren data class olarak tutuyoruz:
    data class DugunItem(
        val id: Int,
        val tarih: String,
        val kazanc: Double,
        val konum: String,
        val aciklama: String?
    )

    private val dugunListe = mutableListOf<DugunItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        listViewDugunler = findViewById(R.id.listViewDugunler)
        db = DatabaseHelper(this)

        formatter = DecimalFormat("#,###", DecimalFormatSymbols(Locale("tr", "TR")).apply {
            groupingSeparator = '.'
            decimalSeparator = ','
        })

        guncelleListe()
        guncelleOzet()

        // Verileri sıfırlama butonu
        findViewById<Button>(R.id.btnVerileriSifirla).setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Verileri Sıfırla")
            builder.setMessage("Tüm kayıtları silmek istediğinize emin misiniz?")
            builder.setPositiveButton("Evet") { _, _ ->
                db.tumVerileriSil()
                Toast.makeText(this, "Gitti paracıklar", Toast.LENGTH_SHORT).show()
                guncelleListe()
                guncelleOzet()
            }
            builder.setNegativeButton("Hayır", null)
            builder.show()
        }

        // Liste öğesine tıklama dinleyicisi - düzenleme/silme dialogu açılır
        listViewDugunler.setOnItemClickListener { _, _, position, _ ->
            val dugun = dugunListe[position]
            showEditDeleteDialog(dugun.id)
        }
    }

    private fun guncelleOzet() {
        val toplamKazanc = db.sumColumn("kazanc")
        val toplamBahsis = db.sumColumn("bahsis")
        val toplamMasraf = db.sumColumn("masraf")

        val ciro = toplamKazanc + toplamBahsis
        val kar = ciro - toplamMasraf

        findViewById<TextView>(R.id.textViewToplamKazanc).text = getString(R.string.toplam_kazanc, formatter.format(toplamKazanc))
        findViewById<TextView>(R.id.textViewToplamBahsis).text = getString(R.string.toplam_bahsis, formatter.format(toplamBahsis))
        findViewById<TextView>(R.id.textViewToplamMasraf).text = getString(R.string.toplam_masraf, formatter.format(toplamMasraf))
        findViewById<TextView>(R.id.textViewCiro).text = getString(R.string.ciro, formatter.format(ciro))
        findViewById<TextView>(R.id.textViewKar).text = getString(R.string.kar, formatter.format(kar))
    }

    private fun guncelleListe() {
        dugunListe.clear()
        val cursor = db.getTumDugunlerCursor()
        val stringList = mutableListOf<String>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val tarih = cursor.getString(cursor.getColumnIndexOrThrow("tarih"))
                val kazanc = cursor.getDouble(cursor.getColumnIndexOrThrow("kazanc"))
                val konum = cursor.getString(cursor.getColumnIndexOrThrow("konum"))
                val aciklama = cursor.getString(cursor.getColumnIndexOrThrow("aciklama"))

                dugunListe.add(DugunItem(id, tarih, kazanc, konum, aciklama))
            } while (cursor.moveToNext())
        }
        cursor.close()

        // Sayaçları hesapla
        val sayacIzmir = dugunListe.count { it.konum == "Izmir" }
        val sayacAydin = dugunListe.count { it.konum == "Aydin" }
        val sayacDiger = dugunListe.count { it.konum == "Diğer" }

        // Sayaçları TextView'a yaz (strings.xml içinden)
        val textKonumBilgi = findViewById<TextView>(R.id.textViewKonumSayac)
        textKonumBilgi.text = getString(R.string.konum_sayac, sayacIzmir, sayacAydin, sayacDiger)

        // Liste için gösterilecek metinler hazırlanıyor
        for (dugun in dugunListe) {
            val satir = "${dugun.konum} – ${dugun.tarih} – ₺${formatter.format(dugun.kazanc)}"
            stringList.add(satir)
        }

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, stringList)
        listViewDugunler.adapter = adapter
    }


    private fun showEditDeleteDialog(dugunId: Int) {
        val cursor = db.getDugunById(dugunId)
        if (!cursor.moveToFirst()) {
            cursor.close()
            Toast.makeText(this, "Kayıt bulunamadı", Toast.LENGTH_SHORT).show()
            return
        }

        val tarih = cursor.getString(cursor.getColumnIndexOrThrow("tarih"))
        val kazanc = cursor.getDouble(cursor.getColumnIndexOrThrow("kazanc"))
        val bahsis = cursor.getDouble(cursor.getColumnIndexOrThrow("bahsis"))
        val masraf = cursor.getDouble(cursor.getColumnIndexOrThrow("masraf"))
        val konum = cursor.getString(cursor.getColumnIndexOrThrow("konum"))
        val aciklama = cursor.getString(cursor.getColumnIndexOrThrow("aciklama"))
        cursor.close()

        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_dugun, null)
        val editTarih = dialogView.findViewById<EditText>(R.id.editTarih)
        val editKazanc = dialogView.findViewById<EditText>(R.id.editKazanc)
        val editBahsis = dialogView.findViewById<EditText>(R.id.editBahsis)
        val editMasraf = dialogView.findViewById<EditText>(R.id.editMasraf)
        val editAciklama = dialogView.findViewById<EditText>(R.id.editAciklama)
        val spinnerKonum = dialogView.findViewById<Spinner>(R.id.spinnerKonum)

        val localeTR = Locale("tr", "TR")
        editTarih.setText(tarih)
        editKazanc.setText(String.format(localeTR, "%.0f", kazanc))
        editBahsis.setText(String.format(localeTR, "%.0f", bahsis))
        editMasraf.setText(String.format(localeTR, "%.0f", masraf))
        editAciklama.setText(aciklama)


        val konumlar = arrayOf("Izmir", "Aydin", "Diğer")
        spinnerKonum.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, konumlar)
        spinnerKonum.setSelection(konumlar.indexOf(konum))

        AlertDialog.Builder(this)
            .setTitle("Düğün Düzenle")
            .setView(dialogView)
            .setPositiveButton("Kaydet") { _, _ ->
                val yeniTarih = editTarih.text.toString()
                val yeniKazanc = editKazanc.text.toString().toDoubleOrNull() ?: 0.0
                val yeniBahsis = editBahsis.text.toString().toDoubleOrNull() ?: 0.0
                val yeniMasraf = editMasraf.text.toString().toDoubleOrNull() ?: 0.0
                val yeniKonum = spinnerKonum.selectedItem.toString()
                val yeniAciklama = editAciklama.text.toString()

                db.updateDugun(
                    dugunId,
                    yeniTarih,
                    yeniKazanc,
                    yeniBahsis,
                    yeniMasraf,
                    yeniKonum,
                    yeniAciklama
                )
                Toast.makeText(this, "Kayıt güncellendi", Toast.LENGTH_SHORT).show()
                guncelleListe()
                guncelleOzet()
            }
            .setNegativeButton("Sil") { _, _ ->
                db.deleteDugun(dugunId)
                Toast.makeText(this, "Kayıt silindi", Toast.LENGTH_SHORT).show()
                guncelleListe()
                guncelleOzet()
            }
            .setNeutralButton("İptal", null)
            .show()
    }

}
