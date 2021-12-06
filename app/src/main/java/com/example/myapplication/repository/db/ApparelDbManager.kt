package com.example.myapplication.repository.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class ApparelDbManager(context: Context) {
    private val dbHelper: DatabaseHelper = DatabaseHelper(context)
    private val db: SQLiteDatabase by lazy { dbHelper.writableDatabase }

    fun insert(values: ContentValues): Long {
        return db.insert(ApparelContract.ApparelEntry.DB_TABLE, "", values)
    }

    fun queryAll(): Cursor {
        return db.rawQuery("select * from ${ApparelContract.ApparelEntry.DB_TABLE}", null)
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        return db.delete(ApparelContract.ApparelEntry.DB_TABLE, selection, selectionArgs)
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
        return db.update(ApparelContract.ApparelEntry.DB_TABLE, values, selection, selectionArgs)
    }

    fun close() {
        db.close()
    }
}