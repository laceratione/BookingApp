package ru.example.androidapp.presentation.hotel

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.shape.CornerFamily
import ru.example.androidapp.R
import ru.example.androidapp.databinding.FragmentHotelBinding
import ru.example.domain.model.Hotel
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class HotelFragment : Fragment() {
    private lateinit var binding: FragmentHotelBinding
    private val hotelViewModel: HotelViewModel by activityViewModels(){
        HotelViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHotelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            hotelViewModel.uiState.collect { uiState ->
                when (uiState) {
                    is HotelUiState.Success -> {
                        uiState.hotel?.let { updateScreen(it) }
//                        updateScreen(uiState.hotel)
                    }

                    is HotelUiState.Error -> {
                        showError(uiState.exception)
                    }

                    is HotelUiState.Loading -> {
//                        shimmerLayout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun updateScreen(hotel: Hotel) {
        with(binding) {
            hotelName.text = hotel.name
            hotelAdress.text = hotel.adress
            val minPriceFormatted = formatNumber(hotel.minimalPrice)
            minPrice.text = "от $minPriceFormatted ₽"
            priceForIt.text = hotel.priceForIt
            rating.text = "${hotel.rating} ${hotel.ratingName}"
            hotelDescription.text = hotel.aboutTheHotel.description

            initChipGroup(hotel.aboutTheHotel.peculiarities)

            val images = ArrayList<SlideModel>()
            images.addAll(hotel.imageUrls.map { url -> SlideModel(url, ScaleTypes.CENTER_CROP) })
            imageSlider.setImageList(images)

            binding.btnSelectHotelRoom.setOnClickListener {
                val action = HotelFragmentDirections.actionHotelFragmentToHotelRoomFragment(hotel.name)
                findNavController().navigate(action)
            }
        }
    }

    private fun initChipGroup(tags: List<String>) {
        val chipGroup: ChipGroup = binding.chipGroup
        tags.forEach { tagName ->
            chipGroup.addView(createChip(tagName))
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
        val formatter: DecimalFormat  = NumberFormat.getInstance(Locale.US) as DecimalFormat
        formatter.applyPattern("#,###")
        return formatter.format(number).replace(",", " ")
    }

    private fun showError(t: Throwable) {
        Toast.makeText(context, getString(R.string.error_load_data), Toast.LENGTH_LONG).show()
    }
}