package com.example.smartstay

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StayItem(
    val name: String,
    val price: Int,
    val type: String
): Parcelable

data class QuestionItem(
    val pageText: String,
    val progress: Int
)
