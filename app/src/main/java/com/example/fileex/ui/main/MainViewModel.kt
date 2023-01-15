package com.example.fileex.ui.main

import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fileex.repository.DirectoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class MainViewModel(private val directoryRepository: DirectoryRepository) : ViewModel() {

    val currentDirectory : MutableLiveData<String> = MutableLiveData()

    /*
    fun getCurrentDirectories(filePath: String) : List<String>{
        return directoryRepository.getDirectories(filePath)
    }*/

    suspend fun getCurrentDirectories(filePath: String) : List<String>{
        return directoryRepository.getDirectories(filePath)
    }
}