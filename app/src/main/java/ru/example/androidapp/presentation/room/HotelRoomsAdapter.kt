package ru.example.androidapp.presentation.room

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.chip.ChipGroup
import ru.example.androidapp.common.Utils
import ru.example.androidapp.databinding.HotelRoomBinding
import ru.example.androidapp.view.MyChip
import ru.example.domain.model.HotelRoom

class HotelRoomsAdapter(private val listener: OnSelectRoomClickListener) :
    RecyclerView.Adapter<HotelRoomsAdapter.ViewHolder>() {
    private var hotelRooms: List<HotelRoom> = emptyList()

    interface OnSelectRoomClickListener {
        fun onItemClick(room: HotelRoom)
    }

    override fun getItemCount(): Int = hotelRooms.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HotelRoomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent.context, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val room = hotelRooms[position]
        holder.bind(room)
    }

    fun updateItems(items: List<HotelRoom>?) {
        hotelRooms = items ?: emptyList()
        notifyDataSetChanged()
    }

    class ViewHolder(
        private val binding: HotelRoomBinding,
        private val context: Context,
        private val listener: OnSelectRoomClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(room: HotelRoom) {
            binding.nameRoom.text = room.name
            binding.priceRoom.text = "${Utils.formatNumber(room.price)} ₽"
            binding.pricePer.text = room.pricePer

            initChipGroup(room.peculiarities)

            val images = ArrayList<SlideModel>()
            images.addAll(room.imageUrls.map { url -> SlideModel(url, ScaleTypes.CENTER_CROP) })
            binding.imageSlider.setImageList(images)

            binding.btnSelectRoom.setOnClickListener {
                listener.onItemClick(room)
            }
        }

        fun initChipGroup(tags: List<String>){
            val chipGroup: ChipGroup = binding.chipGroup
            tags.forEach { tagName ->
                chipGroup.addView(MyChip.createChip(tagName, context))
            }
        }
    }
}