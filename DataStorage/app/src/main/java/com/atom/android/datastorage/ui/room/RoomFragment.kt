package com.atom.android.datastorage.ui.room

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.atom.android.datastorage.R


class RoomFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View =  inflater.inflate(R.layout.fragment_room, container, false)
        val save: Button = view.findViewById(R.id.btn_save)
        val get: Button = view.findViewById(R.id.btn_get)
        val delete: Button = view.findViewById(R.id.btn_delete)
        val editId: EditText = view.findViewById(R.id.edit_text_id)
        val editName: EditText = view.findViewById(R.id.edit_text_name)

        save.setOnClickListener{
            val id = editId.text.toString().toInt()
            val name = editName.text.toString()
            val student: Student? = RomDB.getInstance(requireContext())?.studentDao()?.getById(id)
            if(student == null){
                RomDB.getInstance(requireContext())?.studentDao()?.insertAll(Student(id, name))
                Toast.makeText(context, "Lưu thành công", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Vui lòng chọn khóa chính khác", Toast.LENGTH_SHORT).show()
            }

        }

        get.setOnClickListener{
            val id = editId.text.toString().toInt()
            val student: Student? = RomDB.getInstance(requireContext())?.studentDao()?.getById(id)
            if(student == null)
                editName.setHint("Không tìm thấy với id $id")
            else
            {
                editName.setText(student.name?.toString())
                Toast.makeText(context, "Lấy thành công", Toast.LENGTH_SHORT).show()
            }

        }

        delete.setOnClickListener{
            val id = editId.text.toString().toInt()
            val student: Student? = RomDB.getInstance(requireContext())?.studentDao()?.getById(id)
            if(student == null)
                editName.setHint("Không tìm thấy với id $id")
            else
            {
                RomDB.getInstance(requireContext())?.studentDao()?.delete(id)
                Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show()
            }

        }
        return view
    }

    companion object {

    }
}