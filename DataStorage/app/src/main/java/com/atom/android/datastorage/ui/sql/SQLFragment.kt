package com.atom.android.datastorage.ui.sql

import android.R
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.SimpleCursorAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment


import com.atom.android.datastorage.databinding.FragmentSQLBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class SQLFragment : Fragment() {

    private var _binding: FragmentSQLBinding? = null
    private var list:ArrayList<String> = ArrayList<String>()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSQLBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val databaseHelper = DatabaseHelper(context)
        list = databaseHelper.getAll()
        binding.listView.emptyView = binding.empty
        val adapter = ArrayAdapter<String>(requireContext(), R.layout.simple_dropdown_item_1line, list)
        binding.listView.setAdapter(adapter)

        fun fetchData(){
            this.list.clear()
            this.list.addAll(databaseHelper.getAll())
            adapter.notifyDataSetChanged()
        }

        binding.add.setOnClickListener{
            val subject = binding.subject.text.toString()
            val desc = binding.description.text.toString()
            databaseHelper.insert(subject, desc)
            fetchData()

        }

        binding.remove.setOnClickListener{
            val id = binding.id.text.toString().trim()
            try {
                databaseHelper.delete(id.toLong())
                fetchData()
            }catch (ex:Exception){
                binding.id.setText("Phải là 1 số")
            }
        }

        binding.modify.setOnClickListener{
            val id = binding.id.text.toString().trim()
            val subject = binding.subject.text.toString()
            val desc = binding.description.text.toString()

            try {
                databaseHelper.update(id.toLong(),subject, desc)
                fetchData()
            }catch (ex:Exception){
                binding.id.setText("Phải là 1 số")
            }
        }

        binding.listView.setOnItemClickListener(
            object : AdapterView.OnItemClickListener{
                override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val item = adapter.getItem(p2)
                    try {
                        binding.id.setText(item!!.split('|')[0].trim())
                        binding.subject.setText(item!!.split('|')[1].trim())
                        binding.description.setText(item!!.split('|')[2].trim())
                    }catch (ex:Exception){

                    }
                }
            }
        )


          return root
    }



    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SQLFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}