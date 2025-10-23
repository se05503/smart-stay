package com.example.smartstay.model.user

import java.io.Serializable

data class UserInfo(
    val genderCode: String,
    val age: Int,
    val jobType: String,
    val marriageType: String,
    val childrenType: String,
    val familyType: String,
    val incomePerMember: Float,
    val isCompanionExist: String,
    val companionType: String
): Serializable