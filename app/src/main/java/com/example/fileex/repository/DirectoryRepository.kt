package com.example.fileex.repository

import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DirectoryRepository {

    suspend fun getDirectories(filePath : String) : List<String>{
        lateinit var directoriesFound : List<String>

        withContext(Dispatchers.IO){
            val subdirectoriesNames = File(filePath).list()?.asList()
            val subdirectories : List<String>? = subdirectoriesNames?.map{
                "$filePath/$it"
            }
            directoriesFound = subdirectories ?: ArrayList()
        }

        return directoriesFound
    }

    fun getCurrentFiles(filePath : String) : List<String>{
        val subdirectoriesNames = File(filePath).listFiles()?.asList()
        val subdirectories : List<String>? = subdirectoriesNames?.map{
            "$filePath/$it"
        }
        return subdirectories ?: ArrayList()
    }
}