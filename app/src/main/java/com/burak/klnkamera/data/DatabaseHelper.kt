package com.burak.klnkamera.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "dugungelir.db", null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2 // Versiyon 2 yaptık
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE dugun (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                tarih TEXT,
                kazanc REAL,
                bahsis REAL,
                masraf REAL,
                konum TEXT DEFAULT 'Diğer',
                aciklama TEXT
            )
        """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE dugun ADD COLUMN konum TEXT DEFAULT 'Diğer'")
            db.execSQL("ALTER TABLE dugun ADD COLUMN aciklama TEXT")
        }
    }

    fun ekleDugun(
        tarih: String,
        kazanc: Double,
        bahsis: Double,
        masraf: Double,
        konum: String,
        aciklama: String?
    ) {
        val db = this.writableDatabase
        val cv = ContentValues().apply {
            put("tarih", tarih)
            put("kazanc", kazanc)
            put("bahsis", bahsis)
            put("masraf", masraf)
            put("konum", konum)
            put("aciklama", aciklama)
        }
        db.insert("dugun", null, cv)
        db.close()
    }

    fun sumColumn(columnName: String): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM($columnName) FROM dugun", null)
        val sum = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        return sum
    }

    fun sumColumnBetweenDates(column: String, startDate: String, endDate: String): Double {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM($column) FROM dugun WHERE tarih BETWEEN ? AND ?",
            arrayOf(startDate, endDate)
        )
        val sum = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        return sum
    }

    fun tumVerileriSil() {
        val db = writableDatabase
        db.delete("dugun", null, null)
    }

    fun getTumDugunlerCursor(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM dugun ORDER BY tarih DESC", null)
    }

    fun getDugunById(id: Int): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM dugun WHERE id = ?", arrayOf(id.toString()))
    }

    fun updateDugun(
        id: Int,
        tarih: String,
        kazanc: Double,
        bahsis: Double,
        masraf: Double,
        konum: String,
        aciklama: String?
    ) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("tarih", tarih)
            put("kazanc", kazanc)
            put("bahsis", bahsis)
            put("masraf", masraf)
            put("konum", konum)
            put("aciklama", aciklama)
        }
        db.update("dugun", values, "id=?", arrayOf(id.toString()))
    }

    fun deleteDugun(id: Int) {
        val db = writableDatabase
        db.delete("dugun", "id=?", arrayOf(id.toString()))
    }
}
