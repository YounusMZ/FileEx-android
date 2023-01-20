package com.example.fileex.repository

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.fileex.ui.main.mainfragment.MainViewModel
import com.example.fileex.ui.main.mainfragment.MainViewModelFactory
import kotlinx.coroutines.*
import java.io.File

class DirectoryRepository(activity: FragmentActivity) {

    private val viewModel = ViewModelProvider(activity, MainViewModelFactory(this))[MainViewModel::class.java]

    fun getDirectories(filePath : String){

        CoroutineScope(Dispatchers.IO).launch {
            val subdirectoriesNames = File(filePath).list()?.asList()
            val subdirectories: List<String>? = subdirectoriesNames?.map {
                "$filePath/$it"
            }
            val directoriesFound = subdirectories ?: listOf()
            viewModel.currentSubdirectories.postValue(directoriesFound)
        }
    }

/*
    fun getFiles(filePath : String){

        CoroutineScope(Dispatchers.IO).launch {
            val subdirectoriesNames = File(filePath).listFiles()
            subdirectoriesNames?.filter { it.isFile }
            val subdirectories: List<String>? = subdirectoriesNames?.asList()?.map {
                "$filePath/$it"
            }
            val directoriesFound = subdirectories ?: listOf()

            viewModel.currentSubdirectories.postValue(directoriesFound)
        }
    }
    */
}