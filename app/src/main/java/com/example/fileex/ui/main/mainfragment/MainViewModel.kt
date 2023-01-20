package com.example.fileex.ui.main.mainfragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fileex.repository.DirectoryRepository

class MainViewModel(private val directoryRepository: DirectoryRepository) : ViewModel() {

    val currentDirectory : MutableLiveData<String> = MutableLiveData()
    val currentSubdirectories : MutableLiveData<List<String>> = MutableLiveData()

     private fun getCurrentDirectories(filePath: String){
         directoryRepository.getDirectories(filePath)
    }

    fun setDirectoryAndUpdate(directory: String) {
        currentDirectory.value = directory
        this.getCurrentDirectories(directory)
    }
}