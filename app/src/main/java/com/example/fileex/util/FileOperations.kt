package com.example.fileex.util

import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.nio.file.Files

object FileOperations {
    fun moveFile(source : String, destination : String){
        Log.d("FileOperations", "File Move.")
        copyFile(source, destination)
        deleteFile(source)
    }

    fun copyFile(source : String, destination : String) {
        Log.d("FileOperations", "File Copy.")
        try {
            if (File(source).isFile) {
                File(source).copyTo(File(destination, getFileName(source)))
            } else {
                File(source).copyRecursively(File(destination, getFileName(source)))
            }
        } catch (e : Exception){
            when(e){
                is FileAlreadyExistsException -> {
                    Log.e("FileOperations", "File Already Exists", e)
                }
                is IOException -> {
                    Log.e("FileOperations", "File Copy Failed", e)
                }
            }
        }
    }

    fun deleteFile(fileName : String){
        Log.d("FileOperations", "File Deletion.")
        val fileToBeDeleted = File(fileName)
        if(fileToBeDeleted.isFile) {
            try {
                File(fileName).delete()
            }
            catch (e : SecurityException){
                Log.e("FileOperations", "File Deletion Failed.", e)
            }
        } else{
          fileToBeDeleted.deleteRecursively()
        }
    }

    private fun getFileName(source: String) : String{
        return source.substringAfterLast("/")
    }
}