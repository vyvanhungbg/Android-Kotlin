package com.atom.android.datastorage.ui.sharepref

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.atom.android.datastorage.databinding.FragmentNotificationsBinding


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val pref: SharedPreferences = requireContext()
            .getSharedPreferences("com.atom.android.datastorage", Context.MODE_PRIVATE) // 0 - for private mode

        val key = "string"
        binding.btnWrite.setOnClickListener{
            val editor: SharedPreferences.Editor = pref.edit()
            val str:String = binding.editTextDisplay.text.toString()
            editor.putString(key, str); // Storing string
            editor.apply();
            binding.textViewPath.text = Environment.getDataDirectory().absoluteFile.path.toString()

            Toast.makeText(
                context, "File saved successfully!",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.btnRead.setOnClickListener{
            val str:String = pref.getString(key, "default value").toString()
            binding.editTextDisplay.setText(str)
            binding.textViewPath.text = Environment.getDataDirectory().path.toString()
        }

        binding.btnClear.setOnClickListener{
            val editor: SharedPreferences.Editor = pref.edit()
            editor.clear()
            editor.apply()
        }

        binding.textViewPath.setOnClickListener{
            openFile(Environment.getDataDirectory().path)
        }


        return root
    }

    fun openFile(path:String){
        val intent = Intent(Intent.ACTION_GET_CONTENT)

        // intent.setDataAndType(Uri.parse(path), "*/*")

        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri = Uri.parse(path)
            intent.setDataAndType(uri, "resource/folder")
            this.startActivity(intent)
            //startActivityForResult(Intent.createChooser(intent, "Open folder"), 123)

        }catch (ex:Exception){
            Toast.makeText(
                context, "No package manager support open file!",
                Toast.LENGTH_SHORT
            ).show()
        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}