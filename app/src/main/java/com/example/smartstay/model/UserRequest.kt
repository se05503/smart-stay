package com.example.smartstay.model

data class UserRequest(
    val userId: String,
    val genderCode: String,
    val age: Int,
    val jobType: String,
    val marriageStatus: String,
    val childrenStatus: String,
    val familyType: String,
    val incomePerPerson: Float,
    val companionStatus: String
)
