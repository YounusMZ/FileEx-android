package com.example.fileex.ui.main

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.example.fileex.R
import kotlinx.coroutines.*
import java.io.File

class DirectoryAdapter(private var directoryNames : List<String>, private val viewModel: MainViewModel, private val context : Context)
        : RecyclerView.Adapter<DirectoryAdapter.DirectoryViewHolder>() {

    class DirectoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val directoryButton: TextView = itemView.findViewById(R.id.button)
        val folderImage : ImageView = itemView.findViewById(R.id.folder_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.directory_list, parent, false)

        return DirectoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return directoryNames.size
    }

    override fun onBindViewHolder(holder: DirectoryViewHolder, position: Int) {
        val currentSubdirectoryName = directoryNames[position]

        holder.directoryButton.text = getFolderName(currentSubdirectoryName)

        if (File(currentSubdirectoryName).isDirectory){
            CoroutineScope(Dispatchers.IO).launch {
                holder.folderImage.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.folder_foreground
                    )
                )
            }
        }

        holder.directoryButton.setOnClickListener {
            if(File(currentSubdirectoryName).isDirectory){
                viewModel.currentDirectory.value =  currentSubdirectoryName

                updateDirectoryNames(currentSubdirectoryName)
            }
        }
    }

    private fun getFolderName(folderFullName : String) : String{
        return folderFullName.substringAfterLast("/")
    }

    fun updateDirectoryNames(directoryName : String){
        directoryNames = viewModel.getCurrentDirectories(directoryName)
        notifyDataSetChanged()
    }

}