package com.atom.android.datastorage.ui.sql

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper






class DatabaseHelper(
    context: Context?,
    name: String? = DB_NAME,
    factory: SQLiteDatabase.CursorFactory? = null,
    version: Int = DB_VERSION
) : SQLiteOpenHelper(context, name, factory, version) {


    // Table Name
    val TABLE_NAME = "COUNTRIES"

    // Table columns
    val _ID = "_id"
    val SUBJECT = "subject"
    val DESC = "description"

    // Database Information
    companion object{
        val DB_NAME = "DATA_STORAGE"

        // database version
        val DB_VERSION = 1
    }

    // Creating table query
    private val CREATE_TABLE = ("create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUBJECT + " TEXT  NULL, " + DESC + " TEXT);")




    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }



    fun insert(name: String?, desc: String?) {
        val database =  writableDatabase
        val contentValue = ContentValues()
        contentValue.put(SUBJECT, name)
        contentValue.put(DESC, desc)
        database.apply {
            insert(TABLE_NAME, null, contentValue)
            database.close()
        }
    }

    fun fetch(): Cursor? {
        val database =  writableDatabase
        val columns =
            arrayOf<String>(_ID, SUBJECT,DESC)
        val cursor: Cursor? =
            database.query(TABLE_NAME, columns, null, null, null, null, null)
        cursor?.moveToFirst()
        database.close()
        return cursor
    }

    @SuppressLint("Range")
    fun getAll(): ArrayList<String> {
        val list:ArrayList<String> = ArrayList<String>()
        val c: Cursor? = fetch()

        c?.let {
            c.moveToFirst();
            do{
                val id = c.getString(c.getColumnIndex(_ID))
                val sub = c.getString(c.getColumnIndex(SUBJECT))
                val dir = c.getString(c.getColumnIndex(DESC))
                list.add(id + " | "+ sub + "|"+ dir)
            }
            while (c.moveToNext())
        }



        return list
    }

    fun update(_id: Long, name: String?, desc: String?) {
        val database =  writableDatabase
        val contentValues = ContentValues()
        contentValues.put(SUBJECT, name)
        contentValues.put(DESC, desc)
        database.apply { 
            update(TABLE_NAME, contentValues, "$_ID = $_id", null)
            database.close()
        }
    }

    fun delete(_id: Long) {
        val database =  writableDatabase
        database.delete(TABLE_NAME, "$_ID=$_id", null)
        database.close()
    }
}