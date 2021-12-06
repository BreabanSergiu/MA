package com.example.myapplication.repository.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast


private const val SQL_CREATE_ENTRIES =
    """CREATE TABLE IF NOT EXISTS ${ApparelContract.ApparelEntry.DB_TABLE} (
            ${ApparelContract.ApparelEntry.COLUMN_ID} INTEGER PRIMARY KEY,
            ${ApparelContract.ApparelEntry.COLUMN_PICTURE} TEXT,
            ${ApparelContract.ApparelEntry.COLUMN_NAME} TEXT,
            ${ApparelContract.ApparelEntry.COLUMN_COMPANY} TEXT,
            ${ApparelContract.ApparelEntry.COLUMN_SIZE} TEXT,
            ${ApparelContract.ApparelEntry.COLUMN_DESCRIPTION} TEXT,
            ${ApparelContract.ApparelEntry.COLUMN_COMPOSITION} TEXT)
        """



private const val SQL_DELETE_ENTRIES =
    "DROP TABLE IF EXISTS ${ApparelContract.ApparelEntry.DB_TABLE}"


class DatabaseHelper(private var context: Context) :
    SQLiteOpenHelper(
        context,
        ApparelContract.DB_NAME,
        null,
        ApparelContract.DB_VERSION
    ) {

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SQL_CREATE_ENTRIES)
        context.toast(" database is created")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(SQL_DELETE_ENTRIES)
    }
}


fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()