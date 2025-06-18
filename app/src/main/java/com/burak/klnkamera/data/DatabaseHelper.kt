package com.kilinc.kamera.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "dugungelir.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE dugun (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                tarih TEXT,
                dugun_sayisi INTEGER,
                kazanc REAL,
                bahsis REAL,
                masraf REAL
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}

    fun ekleDugun(tarih: String, sayi: Int, kazanc: Double, bahsis: Double, masraf: Double) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("tarih", tarih)
            put("dugun_sayisi", sayi)
            put("kazanc", kazanc)
            put("bahsis", bahsis)
            put("masraf", masraf)
        }
        db.insert("dugun", null, values)
    }

    fun toplamKazanc(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM(kazanc + bahsis - masraf) FROM dugun", null)
        return if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
    }
}
