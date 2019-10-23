package br.com.renatoarg.exoplayer.model

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Meditation(var title:String, var description:String, var image_url:String, var thumbnail:String, var uri: String, var bitmap:Bitmap?) :
    Parcelable
