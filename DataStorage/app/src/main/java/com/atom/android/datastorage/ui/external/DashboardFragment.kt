package com.atom.android.datastorage.ui.external

import android.Manifest
import android.R.attr
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.atom.android.datastorage.databinding.FragmentDashboardBinding
import java.io.*


class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val fileName = "demo_external.txt"
    val READ_BLOCK_SIZE = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnWrite.setOnClickListener(View.OnClickListener {

            val permission: Boolean = binding.radioPermission.isChecked
            //val folder = Environment.getExternalStorageDirectory()  // yêu cầu quyền trong thời gian chạy
            //val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            //val folder = context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) //// scope storage external của app tạo ra thư mục external có thể nhìn thấy nhưng xóa app cũng mất

            if(permission){
                requestPermission()
            }
            if(binding.radioExternalStorage.isChecked){
                writeFile(Environment.getExternalStorageDirectory())
            }else if(binding.radioExternalStorePublicDir.isChecked){
                writeFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS))
            }else{
                writeFile(context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!)
            }
           // binding.editTextDisplay.text.clear()

        })

        binding.btnRead.setOnClickListener(View.OnClickListener {
           // binding.editTextDisplay.text.clear()
            val permission: Boolean = binding.radioPermission.isChecked
            if(permission){
                requestPermission()
            }
            if(binding.radioExternalStorage.isChecked){
                readFile(Environment.getExternalStorageDirectory())
            }else if(binding.radioExternalStorePublicDir.isChecked){
                readFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS))
            }else{
                readFile(context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)!!)
            }

        })

        binding.textViewPath.setOnClickListener{
            val folder = context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            val file: File = File(folder, fileName)
            openFile(folder!!.path)
        }

        return root
    }


    //open file by path

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



    // write text to file
    fun writeFile(folder:File) {

        try {
            //val folder = Environment.getExternalStorageDirectory()  // yêu cầu quyền trong thời gian chạy
            //val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
            //val folder = context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) //// scope storage external của app tạo ra thư mục external có thể nhìn thấy nhưng xóa app cũng mất
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
            binding.textViewPath.text = e.toString()
        }
    }

    fun requestPermission(){
        val permissions = arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requestPermissions(permissions, 111)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            111 -> if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    // Read text from file
    fun readFile(folder: File) {

        try {
            //val folder = context?.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
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
            binding.textViewPath.text = e.toString()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}