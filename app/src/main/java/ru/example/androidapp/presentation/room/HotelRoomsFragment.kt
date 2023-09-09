package ru.example.androidapp.presentation.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.example.androidapp.R
import ru.example.androidapp.databinding.FragmentHotelRoomBinding
import ru.example.androidapp.presentation.hotel.HotelViewModelFactory
import ru.example.domain.model.HotelRoom

class HotelRoomsFragment : Fragment() {
    private lateinit var binding: FragmentHotelRoomBinding
    private val hotelRoomsViewModel: HotelRoomsViewModel by activityViewModels() {
        HotelRoomsViewModelFactory(requireActivity().application)
    }
    private val args: HotelRoomsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHotelRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnSelectRoomListener = object : HotelRoomsAdapter.OnSelectRoomClickListener {
            override fun onItemClick(room: HotelRoom) {
                findNavController().navigate(R.id.action_hotelRoomFragment_to_bookingFragment)
            }
        }

        val hotelRoomsAdapter = HotelRoomsAdapter(btnSelectRoomListener)
        binding.rvHotelRooms.adapter = hotelRoomsAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            hotelRoomsViewModel.uiState.collect { uiState ->
                when (uiState) {
                    is HotelRoomsUiState.Success -> {
//                        uiState.hotel?.let { updateScreen(it) }
//                        updateScreen(uiState.hotel)
                        hotelRoomsAdapter.updateItems(uiState.hotelRooms)
                    }

                    is HotelRoomsUiState.Error -> {
//                        showError(uiState.exception)
                    }

                    is HotelRoomsUiState.Loading -> {
//                        shimmerLayout.visibility = View.VISIBLE
                    }
                }
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.hotelName.text = args.hotelName
    }

}