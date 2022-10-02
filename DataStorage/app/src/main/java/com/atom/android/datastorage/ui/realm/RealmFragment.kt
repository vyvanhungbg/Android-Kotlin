package com.atom.android.datastorage.ui.realm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.atom.android.datastorage.R
import io.realm.Realm


class RealmFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null



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
        val view: View =  inflater.inflate(R.layout.fragment_realm, container, false)



        val save: Button = view.findViewById(R.id.btn_save)
        val get: Button = view.findViewById(R.id.btn_get)
        val delete: Button = view.findViewById(R.id.btn_delete)
        val editId: EditText = view.findViewById(R.id.edit_text_id)
        val editName: EditText = view.findViewById(R.id.edit_text_name)

        save.setOnClickListener{
            val _id = editId.text.toString()
            val _name = editName.text.toString()
            val realm: Realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            val dog:Dog = realm.createObject(Dog::class.java, _id).apply {
                name = _name
            }
            realm.commitTransaction()
            realm.close()
            Toast.makeText(context, "Lưu thành công", Toast.LENGTH_SHORT).show()
        }

        get.setOnClickListener{
            val id = editId.text.toString()
            val realm: Realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            val student: Dog? = realm.where(Dog::class.java).equalTo("id", id).findFirst()
            if(student == null)
                editName.setHint("Không tìm thấy với id $id")
            else
            {
                editName.setText(student.name)
            }
            realm.commitTransaction()
            realm.close()
        }

        delete.setOnClickListener{
            val id = editId.text.toString()
            val realm: Realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            val student: Dog? = realm.where(Dog::class.java).equalTo("id", id).findFirst()

            if(student == null)
                editName.setHint("Không tìm thấy với id $id")
            else
            {
                student.deleteFromRealm()

                Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show()
            }
            realm.commitTransaction()
            realm.close()

        }

        return view
    }


    override fun onDestroy() {
        super.onDestroy()

    }
    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RealmFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}