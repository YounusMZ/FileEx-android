package com.example.fileex.ui.main.mainfragment

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fileex.R
import com.example.fileex.repository.DirectoryRepository
import com.example.fileex.ui.main.adapters.DirectoryAdapter
import java.io.File

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel : MainViewModel
    private lateinit var directoryRepository: DirectoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        directoryRepository = DirectoryRepository(requireActivity())
        viewModel = ViewModelProvider(requireActivity(), MainViewModelFactory(directoryRepository))[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val homeDirectory = Environment.getExternalStorageDirectory().toString()
        var currentObservedDirectory : String = homeDirectory
        val subdirectories : MutableList<String> = ArrayList()

        //directoryList RecyclerView set up
        val directoryList = view.findViewById<RecyclerView>(R.id.directoryList)
        val directoryListAdapter = DirectoryAdapter(subdirectories, viewModel, requireContext())

        directoryList.layoutManager = LinearLayoutManager(view.context)
        directoryList.adapter = directoryListAdapter

        viewModel.setDirectoryAndUpdate(currentObservedDirectory)

        //set up observer for MainViewModel items
        viewModel.currentDirectory.observe(viewLifecycleOwner) {
            currentObservedDirectory = it
            (requireActivity() as AppCompatActivity).supportActionBar?.title = "FileEx - " + getFolderName(it)
        }

        viewModel.currentSubdirectories.observe(viewLifecycleOwner){
            val sortedList = it.toMutableList()
            sortedList.sort()
            directoryListAdapter.updateDirectoryNames(sortedList)
        }

        //set up file up ("...") button onClick handler
        val fileUpButton : Button = view.findViewById(R.id.fileUp)
        fileUpButton.setOnClickListener {
            fileUpNav(currentObservedDirectory, homeDirectory)
        }

        //back press handler
        val backPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                fileUpNav(currentObservedDirectory, homeDirectory)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, backPressedCallback)
    }

    private fun fileUpNav(currentDirectory : String, homeDir : String){
        if(currentDirectory != homeDir) {
            val parentDirectory = File(currentDirectory).parent?.toString() ?: homeDir
            viewModel.setDirectoryAndUpdate(parentDirectory)
        }
        else{
            Toast.makeText(requireContext(), "You are already in the home directory", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFolderName(folderFullName : String) : String{
        return folderFullName.substringAfterLast("/")
    }
}


