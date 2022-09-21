package com.atom.android.datastorage.ui.internal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.atom.android.datastorage.databinding.FragmentHomeBinding
import java.io.*


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val fileName = "demo_internal.txt"
    val READ_BLOCK_SIZE = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnWrite.setOnClickListener(View.OnClickListener {
            writeFile()
        })

        binding.btnRead.setOnClickListener(View.OnClickListener {
            readFile()
        })

        binding.textViewPath.setOnClickListener{
            val folder = context?.filesDir
            val file: File = File(folder, fileName)
            openFile(file.path)
        }

        Log.e("getFile Dir", requireContext().filesDir.toString())

        return root
    }


    //open file by path

    fun openFile(path:String){
//        val intent = Intent(Intent.ACTION_PICK)
//
//        intent.setDataAndType(Uri.parse(path), "file/*")
//
//        try {
//            startActivityForResult(Intent.createChooser(intent, "Open folder"), 123)
//        }catch (ex:Exception){
//            Toast.makeText(
//                context, "No package manager support open file!",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"

            // Optionally, specify a URI for the file that should appear in the
            // system file picker when it loads.
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, Uri.parse(path))
        }

        startActivityForResult(intent, 1)

    }

    // write text to file
    fun writeFile() {

        try {
            val folder = context?.filesDir  ///data/user/0/com.atom.android.datastorage/files
            val file: File = File(folder, fileName)
            val fileOutputStream : FileOutputStream = FileOutputStream(file)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            outputStreamWriter.write(binding.editTextDisplay.getText()?.toString())
            outputStreamWriter.close()

            Toast.makeText(
                context, "File saved successfully!",
                Toast.LENGTH_SHORT
            ).show()
            binding.textViewPath.text = "Write path : " + file?.path;
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Read text from file
    fun readFile() {

        try {
            val folder = context?.filesDir
            val file: File = File(folder, fileName)
            val fileInputStream: FileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val inputBuffer = CharArray(READ_BLOCK_SIZE)
            var s = ""
            var charRead: Int
            while (inputStreamReader.read(inputBuffer).also { charRead = it } > 0) {
                // char to string conversion
                val readstring = String(inputBuffer, 0, charRead)
                s += readstring
            }
            inputStreamReader.close()
            binding.editTextDisplay?.setText(s)
            binding.textViewPath.text = "Path read : " + file?.path
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}