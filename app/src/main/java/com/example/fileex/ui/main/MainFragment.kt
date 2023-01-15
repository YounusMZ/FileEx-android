package com.example.fileex.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fileex.R
import com.example.fileex.repository.DirectoryRepository
import java.io.File

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private val directoryRepository: DirectoryRepository = DirectoryRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, MainViewModelFactory(directoryRepository))[MainViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var currentObservedDirectory : String = Environment.getExternalStorageDirectory().toString()

        var subdirectories : List<String> = viewModel.getCurrentDirectories(currentObservedDirectory)///

        val directoryList = view.findViewById<RecyclerView>(R.id.directoryList)
        val directoryListAdapter = DirectoryAdapter(subdirectories, viewModel, this.requireContext())

        directoryList.layoutManager = LinearLayoutManager(view.context)
        directoryList.adapter = directoryListAdapter

        viewModel.currentDirectory.observe(viewLifecycleOwner){
            currentObservedDirectory = it
        }

        val fileUpButton : Button = view.findViewById<Button>(R.id.fileUp)
        fileUpButton.setOnClickListener {
            val parentDirectory = File(currentObservedDirectory).parent!!.toString()

            viewModel.currentDirectory.value =  parentDirectory
            directoryListAdapter.updateDirectoryNames(parentDirectory)
        }
    }

}