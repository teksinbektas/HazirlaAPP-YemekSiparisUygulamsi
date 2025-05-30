package com.example.bitirmeprojesi.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bitirmeprojesi.data.entity.FavoriYemekler
import kotlinx.coroutines.withContext
import com.example.bitirmeprojesi.data.entity.SepetYemek
import com.example.bitirmeprojesi.data.entity.Yemekler
import com.example.bitirmeprojesi.data.repository.FavoriYemeklerRepository
import com.example.bitirmeprojesi.data.repository.SepetRepository
import com.example.bitirmeprojesi.data.repository.YemekRetroRepository
import com.example.bitirmeprojesi.ui.fragment.DetailFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailFragmentViewModel @Inject constructor(var sepetRepository: SepetRepository, var favorirepo : FavoriYemeklerRepository) : ViewModel() {


    val quantity = MutableLiveData(1)
    private var currentSepetYemek: SepetYemek? = null

    fun setInitialQuantity(q: Int) {
        quantity.value = q
    }

    fun setCurrentSepetYemek(sepetYemek: SepetYemek?) {
        currentSepetYemek = sepetYemek
        quantity.value = sepetYemek?.yemek_siparis_adet ?: 1

    }


    private var isUpdating = false

    fun increaseQuantity(yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int) {
        if (isUpdating) return
        isUpdating = true


        val current = quantity.value ?: 1
        val newQuantity = current + 1
        quantity.value = newQuantity

        CoroutineScope(Dispatchers.IO).launch {
            val kartKontrol = sepetRepository.sepettekiYemekleriGetir().find { it.yemek_adi == yemek_adi }

            if (kartKontrol != null) {
                sepetRepository.sepetYemekSil(kartKontrol.sepet_yemek_id)
            }

            sepetRepository.sepeteYemekEkle(yemek_adi, yemek_resim_adi, yemek_fiyat, newQuantity)

            withContext(Dispatchers.Main) {
                isUpdating = false
            }
        }
    }

    fun decreaseQuantity(yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int) {
        if (isUpdating) return
        val current = quantity.value ?: 1
        if (current > 1) {
            isUpdating = true
            val newQuantity = current - 1

            quantity.value = newQuantity

            CoroutineScope(Dispatchers.IO).launch {
                val kartKontrol = sepetRepository.sepettekiYemekleriGetir().find { it.yemek_adi == yemek_adi }

                if (kartKontrol != null) {
                    sepetRepository.sepetYemekSil(kartKontrol.sepet_yemek_id)
                }

                sepetRepository.sepeteYemekEkle(yemek_adi, yemek_resim_adi, yemek_fiyat, newQuantity)

                withContext(Dispatchers.Main) {
                    isUpdating = false
                }
            }
        }
    }


    suspend fun checkProductInCart(yemek_adi: String): SepetYemek? {
        val cartItems = sepetRepository.sepettekiYemekleriGetir()
        return cartItems.find { it.yemek_adi == yemek_adi }
    }

    fun sepeteYemekEkle(yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val kartKontrol = checkProductInCart(yemek_adi)

            if (kartKontrol != null) {
                sepetRepository.sepetYemekSil(kartKontrol.sepet_yemek_id)
                sepetRepository.sepeteYemekEkle(
                    yemek_adi,
                    yemek_resim_adi,
                    yemek_fiyat,
                    kartKontrol.yemek_siparis_adet + 1
                )
                quantity.postValue(kartKontrol.yemek_siparis_adet + 1)
            } else {
                sepetRepository.sepeteYemekEkle(
                    yemek_adi,
                    yemek_resim_adi,
                    yemek_fiyat,
                    quantity.value ?: 1
                )
            }
        }
    }

    fun favoriyemekEkle(favoriyemek: FavoriYemekler) {

        viewModelScope.launch {
            favorirepo.favoriEkle(favoriyemek)
        }


    }
    fun sepetYemekToYemekler(sepet: SepetYemek): Yemekler {
        return Yemekler(
            yemek_adi = sepet.yemek_adi,
            yemek_resim_adi = sepet.yemek_resim_adi,
            yemek_fiyat = sepet.yemek_fiyat,
            yemek_id = 0
        )
    }
}












