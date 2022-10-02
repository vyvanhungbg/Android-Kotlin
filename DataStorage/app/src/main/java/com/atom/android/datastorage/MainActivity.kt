package com.atom.android.datastorage

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.atom.android.datastorage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_internal, R.id.navigation_external, R.id.navigation_shared_pref
                ,R.id.navigation_sql
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        log()
    }


    fun log(){
        System.err.println("getFileDir() : $filesDir")
        System.err.println("getExternalFilesDir(null) : " + getExternalFilesDir(null).toString())
        System.err.println("getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) : " + getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString())

        System.err.println("Environment.getExternalStorageDirectory() : " + Environment.getExternalStorageDirectory().toString())
//        System.err.println("Environment.getExternalStoragePublicDirectory(null) : " + Environment.getExternalStoragePublicDirectory(null).toString())
        System.err.println("Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) : " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString())
    }
}