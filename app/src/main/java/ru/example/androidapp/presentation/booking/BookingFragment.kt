package ru.example.androidapp.presentation.booking

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.contains
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.redmadrobot.inputmask.MaskedTextChangedListener.Companion.installOn
import ru.example.androidapp.R
import ru.example.androidapp.databinding.FragmentBookingBinding
import ru.example.domain.model.BookingData
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

class BookingFragment : Fragment() {
    lateinit var binding: FragmentBookingBinding
    private val bookingViewModel: BookingViewModel by activityViewModels() {
        BookingViewModelFactory(requireActivity().application)
    }

    private var listExpView: MutableList<RecyclerView> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            bookingViewModel.uiState.collect { uiState ->
                when (uiState) {
                    is BookingUiState.Success -> {
                        uiState.data?.let { updateScreen(it) }
                    }

                    is BookingUiState.Error -> {}
                    is BookingUiState.Loading -> {}
                }
            }
        }

        if (listExpView.size == 0) {
            val expView = getExpandableRecyclerView("Первый турист")
            binding.llAddTourist.addView(expView)
            listExpView.add(expView)
        } else {
            initExpViews()
        }

        with(binding) {
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnAddTourist.setOnClickListener {
//                bookingViewModel.countTourists++
                if (listExpView.size == 4){
                    Toast.makeText(requireContext(),
                        getString(R.string.max_count_tourists), Toast.LENGTH_SHORT).show()
                }
                else{
                    val expView =
                        getExpandableRecyclerView(bookingViewModel.countWord[listExpView.size])
                    binding.llAddTourist.addView(expView)
                    listExpView.add(expView)
                }
            }
            btnPay.setOnClickListener {
                if (bookingViewModel.validateForm(binding.etEmail.text.toString()))
                    findNavController().navigate(R.id.action_bookingFragment_to_paymentFragment)
            }

        }

        initLoginMask()
        observeLoginError()
        observeEmailError()
    }

    private fun updateScreen(bookingData: BookingData) {
        with(binding) {
            rating.text = "${bookingData.horating} ${bookingData.ratingName}"
            hotelName.text = bookingData.hotelName
            hotelAdress.text = bookingData.hotelAdress

            departure.text = bookingData.departure
            arrivalCountry.text = bookingData.arrivalCountry
            date.text = "${bookingData.tourDateStart} - ${bookingData.tourDateStop}"
            numberNights.text = bookingData.numberOfNights.toString()
            hotel.text = bookingData.hotelName
            room.text = bookingData.room
            nutrition.text = bookingData.nutrition
            tourPrice.text = "${formatNumber(bookingData.tourPrice)} ₽"
            fuelCharge.text = "${formatNumber(bookingData.fuelCharge)} ₽"
            serviceCharge.text = "${formatNumber(bookingData.serviceCharge)} ₽"

            val summ = bookingData.tourPrice + bookingData.fuelCharge + bookingData.serviceCharge
            val formatSumm = "${formatNumber(summ)} ₽"
            totalSumm.text = formatSumm
            btnPay.text = "Оплатить $formatSumm"
        }
    }

    private fun formatNumber(number: Int): String {
        val formatter: DecimalFormat = NumberFormat.getInstance(Locale.US) as DecimalFormat
        formatter.applyPattern("#,###")
        return formatter.format(number).replace(",", " ")
    }

    private fun getExpandableRecyclerView(name: String): RecyclerView {
        val listData: MutableList<ParentData> = ArrayList()
        val parentData: Array<String> = arrayOf(name)
        val childDataData1: MutableList<ChildData> =
            mutableListOf(
                ChildData("Имя"),
                ChildData("Фамилия"),
                ChildData("Дата рождения"),
                ChildData("Гражданство"),
                ChildData("Номер загранпаспорта"),
                ChildData("Срок действия загранпаспорта")
            )

        val parentObj1 = ParentData(parentTitle = parentData[0], subList = childDataData1)
        listData.add(parentObj1)

        val exRecyclerView = RecyclerView(requireContext())
        exRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        exRecyclerView.adapter = RecycleAdapter(requireContext(), listData)

        return exRecyclerView
    }

    private fun initExpViews() {
        for (item in listExpView) {
            if (item.parent != null)
                (item.parent as ViewGroup).removeView(item)
            binding.llAddTourist.addView(item)
        }
    }

    private fun initLoginMask() {
        installOn(
            binding.etPhone,
            PHONE_MASK,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String,
                    tailPlaceholder: String
                ) {
                    bookingViewModel.setLogin(extractedValue)
                }
            }
        )
    }

    private fun observeLoginError() {
        bookingViewModel.loginError.observe(viewLifecycleOwner) { loginError ->
            when (loginError) {
                LoginError.EMPTY -> {
                    binding.tilPhone.error = getString(R.string.error_empty)
                    binding.tilPhone.boxBackgroundColor = ContextCompat.getColor(requireContext(), R.color.error_color)
                }

                LoginError.NOT_VALID -> {
                    binding.tilPhone.error = getString(R.string.error_not_valid)
                    binding.tilPhone.boxBackgroundColor = ContextCompat.getColor(requireContext(), R.color.error_color)
                }

                LoginError.VALID -> {
                    binding.tilPhone.error = null
                }

                else -> {}
            }
        }
    }

    private fun observeEmailError() {
        bookingViewModel.emailError.observe(viewLifecycleOwner) { emailError ->
            when (emailError) {
                EmailError.EMPTY -> {
                    binding.tilEmail.error = getString(R.string.error_empty)
                    binding.tilEmail.boxBackgroundColor = ContextCompat.getColor(requireContext(), R.color.error_color)
                }

                EmailError.NOT_VALID -> {
                    binding.tilEmail.error = getString(R.string.error_not_valid)
                    binding.tilEmail.boxBackgroundColor = ContextCompat.getColor(requireContext(), R.color.error_color)
                }

                EmailError.VALID -> {
                    binding.tilEmail.error = null
                }

                else -> {}
            }
        }
    }

    companion object {
        private const val PHONE_MASK = "+7 ([000]) [000]-[00]-[00]"
    }
}