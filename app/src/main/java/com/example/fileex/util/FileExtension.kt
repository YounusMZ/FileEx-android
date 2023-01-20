package com.example.fileex.util

object FileExtension {
    private val imageExtensions = listOf("png", "jpeg", "jpg")
    private val audioExtensions = listOf("mp3", "flac", "ogg", "aac")
    private val docExtensions = listOf("docx", "txt", "md")
    private val videoExtensions = listOf("mp4", "mkv")


    fun getFileExtension(fileName : String) : String{
        return fileName.substringAfterLast(".")
    }

    fun isImage(extension : String) : Boolean{
        if(imageExtensions.contains(extension)){
            return true
        }
        return false
    }

    fun isVideo(extension : String) : Boolean{
        if(videoExtensions.contains(extension)){
            return true
        }
        return false
    }

    fun isAudio(extension : String) : Boolean{
        if(audioExtensions.contains(extension)){
            return true
        }
        return false
    }

    fun isDoc(extension : String) : Boolean{
        if(docExtensions.contains(extension)){
            return true
        }
        return false
    }
}