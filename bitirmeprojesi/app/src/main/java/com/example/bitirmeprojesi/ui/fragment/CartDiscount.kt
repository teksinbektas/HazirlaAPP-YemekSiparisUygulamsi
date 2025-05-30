package com.example.bitirmeprojesi.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.databinding.FragmentCartDiscountBinding
import com.example.bitirmeprojesi.ui.adapter.DiscountAdapter
import com.example.bitirmeprojesi.ui.viewmodel.CartDiscountViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class CartDiscount : Fragment() {

    private lateinit var viewModel : CartDiscountViewModel
    private lateinit var binding : FragmentCartDiscountBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart_discount,container,false)
        binding.sepetFragment = this

        binding.buttonDetayClose2.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }












        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.sepetListesi.observe(viewLifecycleOwner) { sepetListesi ->
            if (!sepetListesi.isNullOrEmpty() && isAdded) {
                val adapter = DiscountAdapter(requireContext(), sepetListesi, viewModel)
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = adapter

                binding.recyclerView.visibility = View.VISIBLE
                binding.emptyVieww.visibility = View.GONE

                val totalPrice = viewModel.toplamparaHesapla()
                binding.textFiyatToplam.text = "₺ $totalPrice"

            } else {
                // Liste boşsa boş görünümü göster, recyclerView gizle
                binding.recyclerView.visibility = View.GONE
                binding.emptyVieww.visibility = View.VISIBLE
                binding.textFiyatToplam.text = "₺ 0"  // ya da uygun default değer
            }
        }


        viewModel.sepettekiYemekleriGetir()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempViewModel: CartDiscountViewModel by viewModels()
        viewModel = tempViewModel
    }







}