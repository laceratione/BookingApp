package ru.example.androidapp.presentation.hotel

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.shape.CornerFamily
import ru.example.androidapp.R
import ru.example.androidapp.common.Utils
import ru.example.androidapp.databinding.FragmentHotelBinding
import ru.example.androidapp.view.MyChip
import ru.example.domain.model.Hotel

class HotelFragment : Fragment() {
    private lateinit var binding: FragmentHotelBinding
    private val hotelViewModel: HotelViewModel by activityViewModels() {
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

        binding.btnSelectHotelRoom.setOnClickListener {
            val action =
                HotelFragmentDirections.actionHotelFragmentToHotelRoomFragment(hotelViewModel.getHotelName())
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            hotelViewModel.uiState.collect { uiState ->
                when (uiState) {
                    is HotelUiState.Success -> {
                        uiState.hotel?.let { updateScreen(it) }
                    }

                    is HotelUiState.Error -> {}
                    is HotelUiState.Loading -> {}
                }
            }
        }
    }

    private fun updateScreen(hotel: Hotel) {
        with(binding) {
            hotelName.text = hotel.name
            hotelAdress.text = hotel.adress

            val minPriceFormatted = Utils.formatNumber(hotel.minimalPrice)
            minPrice.text = "от $minPriceFormatted ₽"

            priceForIt.text = hotel.priceForIt
            rating.text = "${hotel.rating} ${hotel.ratingName}"
            hotelDescription.text = hotel.aboutTheHotel.description

            initChipGroup(hotel.aboutTheHotel.peculiarities)

            val images = ArrayList<SlideModel>()
            images.addAll(hotel.imageUrls.map { url -> SlideModel(url, ScaleTypes.CENTER_CROP) })
            imageSlider.setImageList(images)
        }
    }

    private fun initChipGroup(tags: List<String>) {
        val chipGroup: ChipGroup = binding.chipGroup
        tags.forEach { tagName ->
            chipGroup.addView(MyChip.createChip(tagName, requireContext()))
        }
    }
}