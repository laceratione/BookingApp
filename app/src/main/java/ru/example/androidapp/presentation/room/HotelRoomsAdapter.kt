package ru.example.androidapp.presentation.room

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.shape.CornerFamily
import ru.example.androidapp.R
import ru.example.androidapp.databinding.HotelRoomBinding
import ru.example.domain.model.HotelRoom
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class HotelRoomsAdapter(private val listener: OnSelectRoomClickListener) : RecyclerView.Adapter<HotelRoomsAdapter.ViewHolder>() {
    private var hotelRooms: List<HotelRoom> = emptyList()

    interface OnSelectRoomClickListener {
        fun onItemClick(room: HotelRoom)
    }

    override fun getItemCount(): Int = hotelRooms.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HotelRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = hotelRooms[position]
        holder.bind(room)
    }

    fun updateItems(items: List<HotelRoom>?) {
        hotelRooms = items ?: emptyList()
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: HotelRoomBinding, private val context: Context,
        private val listener: OnSelectRoomClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(room: HotelRoom) {
            binding.nameRoom.text = room.name
            binding.priceRoom.text = "${formatNumber(room.price)} ₽"
            binding.pricePer.text = room.pricePer

            val chipGroup: ChipGroup = binding.chipGroup
            room.peculiarities.forEach { tagName ->
                chipGroup.addView(createChip(tagName))
            }

            val images = ArrayList<SlideModel>()
            images.addAll(room.imageUrls.map { url -> SlideModel(url, ScaleTypes.CENTER_CROP) })
            binding.imageSlider.setImageList(images)

            binding.btnSelectRoom.setOnClickListener {
                listener.onItemClick(room)
            }
        }

        private fun createChip(name: String): Chip {
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

        private fun formatNumber(number: Int): String{
            val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
            formatter.applyPattern("#,###")
            return formatter.format(number).replace(",", " ")
        }
    }



}