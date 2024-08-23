package net.braniumacademy.lesson1204

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.storage.StorageManager
import android.os.storage.StorageManager.ACTION_MANAGE_STORAGE
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import com.google.android.material.snackbar.Snackbar
import net.braniumacademy.lesson1204.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCheckSpace.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                checkAllocatableMemory()
            }
        }
        binding.btnSave.setOnClickListener { saveData() }
        binding.btnLoad.setOnClickListener { loadData() }
        binding.btnListFile.setOnClickListener { listFiles() }
        // create cache file
        File.createTempFile(CACHE_FILE_NAME, null, cacheDir)
    }

    private fun listFiles() {
        val fileNames = StringBuilder()
        fileList().forEach {
            fileNames.append(it).append("\n")
        }
        fileNames.deleteCharAt(fileNames.toString().lastIndex)
        binding.textSavedValue.text = fileNames
    }

    private fun saveData() {
        val message = binding.editInputMessage.text.toString()
        val myFile = File(filesDir, NORMAL_FILE_NAME)
        FileOutputStream(myFile, true).bufferedWriter().use {
            it.write(message + "\n")
        }
        binding.editInputMessage.setText("")
    }

    private fun loadData() {
        val stringBuilder = StringBuilder()
        val myFile = File(filesDir, NORMAL_FILE_NAME)

        if (myFile.exists()) {
            val fileInputStream = myFile.inputStream()
            fileInputStream.bufferedReader().use {
                val strings = it.readLines()
                strings.forEach { item ->
                    stringBuilder.append(item).append("\n")
                }
                if (stringBuilder.toString().isNotEmpty()) {
                    stringBuilder.deleteAt(stringBuilder.lastIndexOf("\n"))
                }
            }
        }
        if (stringBuilder.isEmpty()) {
            Snackbar.make(
                binding.containerView,
                "File is empty!",
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            binding.textSavedValue.text = stringBuilder.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkAllocatableMemory() {
        val storageManager = applicationContext.getSystemService<StorageManager>()!!
        val appSpecificInternalDirUuid: UUID = storageManager.getUuidForPath(filesDir)
        val availableBytes: Long =
            storageManager.getAllocatableBytes(appSpecificInternalDirUuid)
        if (availableBytes >= NEEDED_SPACE) {
            storageManager.allocateBytes(
                appSpecificInternalDirUuid, NEEDED_SPACE
            )
            binding.textFreeSpace.text = getString(
                R.string.message_free_space,
                "${availableBytes / MB_UNIT}MB"
            )
            Snackbar.make(
                binding.containerView,
                "Available space: ${availableBytes / MB_UNIT}MB",
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            val storageIntent = Intent().apply {
                action = ACTION_MANAGE_STORAGE
            }
            startActivity(storageIntent)
        }
    }

    companion object {
        const val NEEDED_SPACE = 1024 * 1024 * 50L
        const val MB_UNIT = 1024 * 1024
        const val NORMAL_FILE_NAME = "message.txt"
        const val CACHE_FILE_NAME = "cache_message.txt"
    }
}
//package net.braniumacademy.lesson1204
//
//import android.content.Intent
//import android.os.Build
//import android.os.Bundle
//import android.os.storage.StorageManager
//import android.os.storage.StorageManager.ACTION_MANAGE_STORAGE
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.getSystemService
//import com.google.android.material.snackbar.Snackbar
//import net.braniumacademy.lesson1204.databinding.ActivityMainBinding
//import java.io.File
//import java.io.FileInputStream
//import java.io.FileOutputStream
//import java.util.UUID
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        binding.btnCheckSpace.setOnnhấnListener {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                checkAllocatableMemory()
//            }
//        }
//        binding.btnSave.setOnnhấnListener { saveData() }
//        binding.btnLoad.setOnnhấnListener { loadData() }
//        binding.btnListFile.setOnnhấnListener { listFiles() }
//        // create cache file
//        File.createTempFile(CACHE_FILE_NAME, null, cacheDir)
//    }
//
//    private fun listFiles() {
//        val fileNames = StringBuilder()
//        fileList().forEach {
//            fileNames.append(it).append("n")
//        }
//        fileNames.deleteCharAt(fileNames.toString().lastIndex)
//        binding.textSavedValue.text = fileNames
//    }
//
//    private fun saveData() {
//        val message = binding.editInputMessage.text.toString()
//        val myCacheFile = File(cacheDir, CACHE_FILE_NAME)
//        FileOutputStream(myCacheFile, true).bufferedWriter().use {
//            it.write(message + "n")
//        }
//        binding.editInputMessage.setText("")
//    }
//
//    private fun loadData() {
//        val cachedFile = File(cacheDir, CACHE_FILE_NAME)
//        val stringBuilder = StringBuilder()
//        if (cachedFile.exists()) {
//            FileInputStream(cachedFile).bufferedReader().use {
//                val lines = it.readLines()
//                lines.forEach { line ->
//                    stringBuilder.append(line).append("n")
//                }
//            }
//            if (stringBuilder.toString().isNotEmpty()) {
//                stringBuilder.deleteAt(stringBuilder.lastIndexOf("n"))
//            }
//        }
//        if (stringBuilder.isEmpty()) {
//            Snackbar.make(
//                binding.containerView,
//                "File is empty!",
//                Snackbar.LENGTH_LONG
//            ).show()
//        } else {
//            binding.textSavedValue.text = stringBuilder.toString()
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun checkAllocatableMemory() {
//        val storageManager = applicationContext.getSystemService<StorageManager>()!!
//        val appSpecificInternalDirUuid: UUID = storageManager.getUuidForPath(filesDir)
//        val availableBytes: Long =
//            storageManager.getAllocatableBytes(appSpecificInternalDirUuid)
//        if (availableBytes >= NEEDED_SPACE) {
//            storageManager.allocateBytes(
//                appSpecificInternalDirUuid, NEEDED_SPACE
//            )
//            binding.textFreeSpace.text = getString(
//                R.string.message_free_space,
//                "${availableBytes / MB_UNIT}MB"
//            )
//            Snackbar.make(
//                binding.containerView,
//                "Available space: ${availableBytes / MB_UNIT}MB",
//                Snackbar.LENGTH_LONG
//            ).show()
//        } else {
//            val storageIntent = Intent().apply {
//                action = ACTION_MANAGE_STORAGE
//            }
//            startActivity(storageIntent)
//        }
//    }
//
//    companion object {
//        const val NEEDED_SPACE = 1024 * 1024 * 50L
//        const val MB_UNIT = 1024 * 1024
//        const val CACHE_FILE_NAME = "cache_message.txt"
//    }
//}