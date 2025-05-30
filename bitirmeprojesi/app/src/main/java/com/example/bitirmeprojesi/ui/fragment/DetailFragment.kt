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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.data.entity.FavoriYemekler
import com.example.bitirmeprojesi.data.entity.SepetYemek
import com.example.bitirmeprojesi.data.entity.Yemekler
import com.example.bitirmeprojesi.databinding.FragmentCartDiscountBinding
import com.example.bitirmeprojesi.databinding.FragmentDetailBinding
import com.example.bitirmeprojesi.ui.viewmodel.DetailFragmentViewModel
import com.example.bitirmeprojesi.utils.gecisYap
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailFragmentViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var currentYemek: Yemekler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.buttonDetayClose.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        val yemek: Yemekler? = args.yemek
        val sepet: SepetYemek? = args.sepet

        val yemekNesnesi = when {
            yemek != null -> yemek
            sepet != null -> viewModel.sepetYemekToYemekler(sepet)
            else -> null
        }

        yemekNesnesi?.let { yemekItem ->

            currentYemek = yemekItem

            if (sepet != null) {

                viewModel.setCurrentSepetYemek(sepet)
            } else {

                viewModel.setInitialQuantity(1)
            }

            val imageUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${yemekItem.yemek_resim_adi}"
            Glide.with(binding.imageDetail).load(imageUrl).override(300, 300).into(binding.imageDetail)

            binding.buttonDetayHeart.setOnClickListener {
                try {
                    val favori = FavoriYemekler(yemekItem.yemek_adi, yemekItem.yemek_resim_adi, yemekItem.yemek_id, yemekItem.yemek_fiyat)
                    viewModel.favoriyemekEkle(favori)
                    Snackbar.make(it, "${yemekItem.yemek_adi} favorilere eklendi", Snackbar.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                } catch (e: Exception) {
                    Log.e("MealsAdapter", "Favorilere ekleme hatası: ${e.message}")
                }
            }

            binding.textAddBacket.setOnClickListener { view ->
                viewModel.sepeteYemekEkle(yemekItem.yemek_adi, yemekItem.yemek_resim_adi, yemekItem.yemek_fiyat)
                Navigation.findNavController(view).popBackStack()
                Snackbar.make(view, "${yemekItem.yemek_adi} karta eklendi!", Snackbar.LENGTH_SHORT).show()
            }


            viewModel.quantity.observe(viewLifecycleOwner) { deger ->
                binding.buttonStock.text = deger.toString()

                val fiyat = yemekItem.yemek_fiyat.toDouble()
                val toplamFiyat = deger * fiyat

                binding.textPrice.text = String.format("₺ %.2f", toplamFiyat)
                binding.textView4.text = String.format("₺ %.2f", fiyat)

                binding.buttonMinus.alpha = if (deger <= 1) 0.1f else 1.0f
                binding.buttonMinus.isClickable = deger > 1
            }


            binding.buttonPlus.setOnClickListener {
                viewModel.increaseQuantity(currentYemek.yemek_adi, currentYemek.yemek_resim_adi, currentYemek.yemek_fiyat)
            }

            binding.buttonMinus.setOnClickListener {
                viewModel.decreaseQuantity(currentYemek.yemek_adi, currentYemek.yemek_resim_adi, currentYemek.yemek_fiyat)
            }
        }

        return binding.root
    }

}












