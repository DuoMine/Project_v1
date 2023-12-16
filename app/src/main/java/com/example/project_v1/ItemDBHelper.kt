package com.example.project_v1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.project_v1.activity.MainActivity


private const val DBNAME = "Item.db"
class ItemDBHelper(context: Context?) : SQLiteOpenHelper(context, DBNAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE Items " +
                "(itemId INTEGER PRIMARY KEY, user TEXT, type TEXT, name TEXT, content TEXT, resource INTEGER);")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS Items")
        onCreate(db)
    }

    fun addItems(user: String, type: String, name: String, content: String, resource: Int) : Boolean{
        var itemDB: SQLiteDatabase = this.writableDatabase
        var contentValues = ContentValues()

        contentValues.put("user", user)
        contentValues.put("type" , type)
        contentValues.put("name", name)
        contentValues.put("content", content)
        contentValues.put("resource", resource)

        var result = itemDB.insert("Items", null, contentValues)
        return result != (-1).toLong()
    }

    fun getItems(user: String, type: String) : ArrayList<ItemData> {
        var itemDB: SQLiteDatabase = this.readableDatabase

        var cursor = itemDB.rawQuery("SELECT * FROM Items WHERE user = ? AND type = ?", arrayOf(user, type))

        var name = ArrayList<String>()
        var content = ArrayList<String>()
        var resource = ArrayList<Int>()
        var list = ArrayList<ItemData>()


        while(cursor.moveToNext()){
            name.add(cursor.getString(3))
            content.add(cursor.getString(4))
            resource.add(cursor.getInt(5))
        }
        for (i in 0..< name.size){
            list.add(ItemData(name[i], content[i], resource[i]))
        }

        return list
    }

    fun checkItems(user: String, name: String) : Boolean{
        var itemDB: SQLiteDatabase = this.readableDatabase
        var cursor = itemDB.rawQuery("SELECT * FROM Items WHERE user = ? AND name = ?", arrayOf(user, name))

        return cursor.count > 0
    }

    fun checkTypeItems(user: String, type: String): Boolean{
        var itemDB: SQLiteDatabase = this.readableDatabase
        var cursor = itemDB.rawQuery("SELECT * FROM Items WHERE user = ? AND type = ?", arrayOf(user, type))

        return cursor.count > 0
    }
}