package com.example.fileex.ui.main.mainfragment.fileoperationspopup

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fileex.R
import com.example.fileex.ui.main.mainfragment.MainViewModel
import com.example.fileex.util.FileOperations
import java.io.File

@SuppressLint("ViewConstructor")
class FileOperationsPopup(context: Context, parentView: View, private val viewModel: MainViewModel): View(context){

    private val popUp : View = LayoutInflater.from(context)
        .inflate(R.layout.file_operations, parentView as ViewGroup)

    private val layout : ConstraintLayout = popUp.findViewById(R.id.file_oper)
    private val moveButton = popUp.findViewById<Button>(R.id.move)
    private val copyButton = popUp.findViewById<Button>(R.id.copy)
    private val deleteButton = popUp.findViewById<Button>(R.id.delete)
    private val pasteButton = popUp.findViewById<Button>(R.id.paste)
    private val cancelButton = popUp.findViewById<Button>(R.id.cancel)

    private var currentOperation : String = ""
    private var isClickedState : Boolean = false
    private var sourceFileName : String = ""
    private var destinationFileName : String = ""


    fun getCLickState() : Boolean{
        return this.isClickedState
    }

    fun setDestination(fileName: String){
        if(File(fileName).isDirectory) {
            destinationFileName = fileName
        }
    }

    fun setSource(fileName: String){
        sourceFileName = fileName
    }

    fun setUpButtonCLickHandler(){
        moveButton.setOnClickListener {
            removeLayoutView()
            currentOperation = "move"
            isClickedState = true

            deleteButton.visibility = GONE
        }

        copyButton.setOnClickListener {
            removeLayoutView()
            currentOperation = "copy"
            isClickedState = true

            deleteButton.visibility = GONE
        }

        deleteButton.setOnClickListener {
            removeLayoutView()
            currentOperation = "delete"
            performOperation()

            currentOperation = ""
            cancelButton.visibility = GONE
        }

        pasteButton.setOnClickListener {
            removeLayoutView()
            if(isClickedState && File(sourceFileName).exists()) {
                performOperation()
            }
            else{
                Toast.makeText(context, "Can't paste. Operation not selected.", Toast.LENGTH_SHORT).show()
            }
            deleteButton.visibility = VISIBLE
            cancelButton.visibility = GONE
        }

        cancelButton.setOnClickListener {
            removeLayoutView()
            isClickedState = false
        }
    }

    private fun performOperation() {
        if (sourceFileName.isNotEmpty()) {
            when(currentOperation){
                "move" -> FileOperations.moveFile(sourceFileName, destinationFileName)
                "copy" -> FileOperations.copyFile(sourceFileName, destinationFileName)
                "delete" -> FileOperations.deleteFile(sourceFileName)
            }
        }

        Toast.makeText(context, "Operation completed", Toast.LENGTH_SHORT).show()

        if(destinationFileName.isNotEmpty() && File(destinationFileName).isDirectory){
            viewModel.setDirectoryAndUpdate(destinationFileName)
        }
        else{
            viewModel.setDirectoryAndUpdate(Environment.getExternalStorageDirectory().toString())
        }
        isClickedState = false
    }

    fun setCancelButtonVisible(){
        cancelButton.visibility = VISIBLE
    }

    private fun removeLayoutView(){
        layout.visibility = GONE
    }

    fun showLayoutView(){
        layout.visibility = VISIBLE
    }
}