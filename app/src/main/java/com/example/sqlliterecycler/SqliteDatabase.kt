package com.example.sqlliterecycler

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class SqliteDatabase(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_CONTACTS_TABLE =
            "CREATE	TABLE " + TABLE_CONTACTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT," + COLUMN_NO + " INTEGER" + ")"
        db.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }

    fun listContacts(): ArrayList<Contacts> {
        val sql = "select * from " + TABLE_CONTACTS
        val db = this.readableDatabase
        val storeContacts: ArrayList<Contacts> = ArrayList()
        val cursor: Cursor = db.rawQuery(sql, null)
        if (cursor.moveToFirst()) {
            do {
                val id: Int = cursor.getString(0).toInt()
                val name: String = cursor.getString(1)
                val phno: String = cursor.getString(2)
                storeContacts.add(Contacts(id, name, phno))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return storeContacts
    }

    fun addContacts(contacts: Contacts) {
        val values = ContentValues()
        values.put(COLUMN_NAME, contacts.name)
        values.put(COLUMN_NO, contacts.phno)
        val db = this.writableDatabase
        db.insert(TABLE_CONTACTS, null, values)
    }

    fun updateContacts(contacts: Contacts) {
        val values = ContentValues()
        values.put(COLUMN_NAME, contacts.name)       //.getName())
        values.put(COLUMN_NO, contacts.phno)                    //.getPhno())
        val db = this.writableDatabase
        db.update(
            TABLE_CONTACTS,
            values,
            COLUMN_ID + "	= ?",
            arrayOf(java.lang.String.valueOf(contacts.id))      //.getId()
        )
    }

    fun findContacts(name: String?): Contacts? {
        val query = "Select * FROM " + TABLE_CONTACTS + " WHERE " + COLUMN_NAME + " = " + "name"
        val db = this.writableDatabase
        var contacts: Contacts? = null
        val cursor: Cursor = db.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            val id: Int = cursor.getString(0).toInt()
            val contactsName: String = cursor.getString(1)
            val contactsNo: String = cursor.getString(2)
            contacts = Contacts(id, contactsName, contactsNo)
        }
        cursor.close()
        return contacts
    }

    fun deleteContact(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_CONTACTS, COLUMN_ID + "	= ?", arrayOf(id.toString()))
    }

    companion object {
        private const val DATABASE_VERSION = 5
        private const val DATABASE_NAME = "contact"
        private const val TABLE_CONTACTS = "contacts"
        private const val COLUMN_ID = "_id"
        private const val COLUMN_NAME = "contactname"
        private const val COLUMN_NO = "phno"
    }
}