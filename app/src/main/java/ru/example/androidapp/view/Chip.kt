package ru.example.androidapp.view

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.google.android.material.shape.CornerFamily
import ru.example.androidapp.R

class MyChip {
    companion object{
        fun createChip(name: String, context: Context): Chip {
            return Chip(context).apply {
                text = name
                textSize = 16f
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                setChipBackgroundColorResource(R.color.chip_back_color)
                setCheckedIconVisible(false)
                setCloseIconVisible(false)
                setTextColor(ContextCompat.getColorStateList(context, R.color.chip_text_color))
                isCheckable = false
                isClickable = false
                shapeAppearanceModel = shapeAppearanceModel
                    .toBuilder()
                    .setAllCorners(CornerFamily.ROUNDED, 30F)
                    .build()
                chipStrokeWidth = 0f
            }
        }
    }
}