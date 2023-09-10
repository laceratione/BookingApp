package ru.example.androidapp.common

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class Utils {
    companion object {
        fun formatNumber(number: Int): String {
            val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
            formatter.applyPattern("#,###")
            return formatter.format(number).replace(",", " ")
        }
    }
}