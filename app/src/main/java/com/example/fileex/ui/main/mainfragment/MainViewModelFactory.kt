package com.example.fileex.ui.main.mainfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fileex.repository.DirectoryRepository

class MainViewModelFactory(private val directoryRepository: DirectoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(directoryRepository) as T
    }
}