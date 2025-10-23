package com.example.smartstay

import java.text.DecimalFormat

object Utils {

    fun formatPrice(price: Int): String {
        val formatter = DecimalFormat("#,###")
        return "${formatter.format(price)}원"
    }
    fun decodeUnicode(input: String): String {
        val regex = Regex("""\\u([0-9a-fA-F]{4})""")
        return regex.replace(input) {
            val intVal = it.groupValues[1].toInt(16)
            intVal.toChar().toString()
        }
    }
}