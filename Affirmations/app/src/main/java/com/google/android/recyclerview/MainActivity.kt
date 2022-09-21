package com.google.android.recyclerview

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       /* val myDataSet = Datasource().loadAffirmations();
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView);
        recyclerView.adapter = ItemAdapter(this,myDataSet);
        recyclerView.setHasFixedSize(true)*/

        val internal_m1: File = getDir("abc", 0)
        val internal_m2: File = filesDir


        val external_m1: File = Environment.getExternalStorageDirectory()

        val external_m2: File? = getExternalFilesDir(null)
        val external_m2_Args: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val external_m3: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        val external_AND_removable_storage_m1: Array<File> = getExternalFilesDirs(null)
        val external_AND_removable_storage_m1_Args: Array<File> =
            getExternalFilesDirs(Environment.DIRECTORY_PICTURES)

        val file1: File = File(
            getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            "YourAppDirectory"
        )

        val docs = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS
            ), "YourAppDirectory"
        )

        val fileName = "helloworld"
        val textToWrite = "Hello, World!"
        val fileOutputStream: FileOutputStream
        try {
            fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE)
            Log.e("Path ",  fileOutputStream.fd.toString())
            fileOutputStream.write(textToWrite.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }



        Log.e("TAG internal_m1 : ", internal_m1.toString())
        Log.e("TAG internal_m2 : ", internal_m2.toString())
        Log.e("TAG external_m1 : ", external_m1.toString())
        Log.e("TAG external_m2 : ", external_m2.toString())
        Log.e("TAG external_m2_Args : ", external_m2_Args.toString())
        Log.e("TAG external_m3 : ", external_m3.toString())
        Log.e("external_remov_m1 : ", external_AND_removable_storage_m1.toString())
        Log.e("external_remov_Args : ", external_AND_removable_storage_m1_Args.toString())
        Log.e("getExternalFilesDir : ", file1.toString())
        Log.e("gExternalStoragePublic:", docs.toString())

    }
}