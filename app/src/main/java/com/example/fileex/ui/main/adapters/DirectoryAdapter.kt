package com.example.fileex.ui.main.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.os.Build
import android.util.Size
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.scale
import androidx.recyclerview.widget.RecyclerView
import com.example.fileex.R
import com.example.fileex.util.FileOperations
import com.example.fileex.ui.main.mainfragment.MainViewModel
import com.example.fileex.ui.main.mainfragment.fileoperationspopup.FileOperationsPopup
import com.example.fileex.util.FileExtension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Duration


class DirectoryAdapter(private val directoryNames : MutableList<String>, private val mainViewModel: MainViewModel, private val context: Context)
        : RecyclerView.Adapter<DirectoryAdapter.DirectoryViewHolder>() {

    private lateinit var customPopup : FileOperationsPopup

    class DirectoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val directoryTextView: TextView = itemView.findViewById(R.id.button)
        val directoryImage : ImageView = itemView.findViewById(R.id.folder_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.directory_list, parent, false)

        return DirectoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return directoryNames.size
    }

    override fun onBindViewHolder(holder: DirectoryViewHolder, position: Int) {
        val currentDirectoryName = directoryNames[holder.absoluteAdapterPosition]
        val folderName = getFolderName(currentDirectoryName)

        holder.directoryTextView.text = folderName

        //set drawable for ImageView next to folders. Otherwise, set to null
        if (File(currentDirectoryName).isDirectory) {
            holder.directoryImage.setImageDrawable(AppCompatResources.getDrawable(context,
                R.drawable.folder_foreground))
        }
        else {
            val fileExtension = FileExtension.getFileExtension(currentDirectoryName)

            if (FileExtension.isImage(fileExtension)){
                    CoroutineScope(Dispatchers.Main).launch{
                        val thumbnailImage : Bitmap

                        holder.directoryImage.setImageBitmap(null)

                        withContext(Dispatchers.IO) {
                            val options = BitmapFactory.Options()
                            options.inSampleSize = 4

                            thumbnailImage = BitmapFactory.decodeFile(currentDirectoryName, options).scale(64, 42)
                            thumbnailImage.prepareToDraw()
                        }
                        holder.directoryImage.setImageBitmap(thumbnailImage)
                    }
                }

            else if(FileExtension.isVideo(fileExtension) && Build.VERSION.SDK_INT >= 29){
                var thumbnailImage : Bitmap
                CoroutineScope(Dispatchers.Main).launch{
                    withContext(Dispatchers.IO) {
                        thumbnailImage = ThumbnailUtils.createVideoThumbnail(
                            File(currentDirectoryName), Size(64, 64),
                            null
                        )
                    }
                    holder.directoryImage.setImageBitmap(thumbnailImage)
                }
            }

            else {
                holder.directoryImage.setImageDrawable(null)
            }
        }

        //onCLick handler for subdirectories
        holder.directoryTextView.setOnClickListener {
            if (File(currentDirectoryName).isDirectory) {
                mainViewModel.setDirectoryAndUpdate(currentDirectoryName)
            }
        }

        //set up subdirectories long click handler. Currently inflates a custom layout with options
        holder.directoryTextView.setOnLongClickListener {
            if(!this::customPopup.isInitialized) {
                //customPopup initialization
                customPopup = FileOperationsPopup(context, it.rootView, mainViewModel)
                customPopup.setSource(currentDirectoryName)
                customPopup.setCancelButtonVisible()
                customPopup.setUpButtonCLickHandler()
            }
            else{
                if(customPopup.getCLickState()){
                    //if operation already selected
                    customPopup.setDestination(currentDirectoryName)
                    customPopup.setCancelButtonVisible()
                    customPopup.showLayoutView()

                    Toast.makeText(context, "Long folder press to select and paste", Toast.LENGTH_SHORT).show()
                }
                else{
                    //if no file operation is currently selected
                    customPopup.setSource(currentDirectoryName)
                    customPopup.setCancelButtonVisible()
                    customPopup.showLayoutView()
                }
            }
            true
        }
    }

    private fun getFolderName(folderFullName : String) : String{
        return folderFullName.substringAfterLast("/")
    }

    fun updateDirectoryNames(newDirectoryNames : MutableList<String>){
        val oldSize = directoryNames.size
        val newSize = newDirectoryNames.size
        directoryNames.clear()
        notifyItemRangeRemoved(0, oldSize)
        directoryNames.addAll(newDirectoryNames)
        notifyItemRangeInserted(0, newSize)
    }
}