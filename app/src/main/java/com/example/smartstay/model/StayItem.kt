package com.example.smartstay.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StayItem(
    val name: String,
    val price: Int,
    val type: String
): Parcelable

data class QuestionItem(
    val questionTitle: String,
    val progress: Int
)
