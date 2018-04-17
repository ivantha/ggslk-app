package com.ggslk.ggslk.common

import android.content.Context
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object SaveHandler {

    @JvmStatic
    fun load(context: Context, name: String): Any? {
        val fileInputStream = context.openFileInput(name)
        val objectInputStream = ObjectInputStream(fileInputStream)

        val obj = objectInputStream.readObject()

        objectInputStream.close()
        fileInputStream.close()

        return obj
    }

    @JvmStatic
    fun save(context: Context, name: String, obj: Any) {
        val fileOutputStream = context.openFileOutput(name, Context.MODE_PRIVATE)
        val objectOutputStream = ObjectOutputStream(fileOutputStream)

        objectOutputStream.writeObject(obj)

        objectOutputStream.close()
        fileOutputStream!!.close()
    }

    @JvmStatic
    fun saveExists(context: Context, name: String): Boolean {
        return try {
            var fileOutputStream = context.openFileInput(name)
            var ret = fileOutputStream != null

            fileOutputStream.close()

            ret
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}