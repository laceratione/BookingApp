package ru.example.androidapp.presentation.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.example.androidapp.R
import ru.example.androidapp.databinding.FragmentOrderPaidBinding
import kotlin.random.Random

class PaymentFragment : Fragment() {
    private lateinit var binding: FragmentOrderPaidBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderPaidBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            val randomNumer = Random.nextInt(100000, 110000)
            tvDescription.text = getString(R.string.payment_description, randomNumer.toString())
            btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
            btnOk.setOnClickListener {
                findNavController().navigate(R.id.action_paymentFragment_to_hotelFragment)
            }
        }
    }
}