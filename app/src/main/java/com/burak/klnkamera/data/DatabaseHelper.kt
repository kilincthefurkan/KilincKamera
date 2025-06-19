package com.burak.klnkamera.data

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

    fun sumColumn(columnName: String): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM($columnName) FROM dugun", null)
        var sum = 0.0
        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(0)
        }
        cursor.close()
        return sum
    }

    fun sumColumnBetweenDates(column: String, startDate: String, endDate: String): Double {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT SUM($column) FROM dugun WHERE tarih BETWEEN ? AND ?",
            arrayOf(startDate, endDate)
        )
        var sum = 0.0
        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(0)
        }
        cursor.close()
        return sum
    }
    fun tumVerileriSil() {
        val db = writableDatabase
        db.execSQL("DELETE FROM dugun")
    }
}
