package com.dicoding.storyapp.ui.camera

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class CameraArgs (
    val img: File,
    val isBackCamera: Boolean
): Parcelable