package com.google.android.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.recyclerview.adapter.ItemAdapter
import com.google.android.recyclerview.data.Datasource
import javax.sql.DataSource

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myDataSet = Datasource().loadAffirmations();
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView);
        recyclerView.adapter = ItemAdapter(this,myDataSet);
        recyclerView.setHasFixedSize(true)

    }
}