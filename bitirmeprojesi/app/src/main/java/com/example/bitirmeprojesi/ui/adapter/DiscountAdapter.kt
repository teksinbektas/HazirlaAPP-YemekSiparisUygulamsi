package com.example.bitirmeprojesi.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bitirmeprojesi.R
import com.example.bitirmeprojesi.data.entity.SepetYemek
import com.example.bitirmeprojesi.data.entity.Yemekler
import com.example.bitirmeprojesi.databinding.HolderDiscountAdapterBinding
import com.example.bitirmeprojesi.ui.fragment.CartDiscountDirections
import com.example.bitirmeprojesi.ui.fragment.DetailFragmentDirections
import com.example.bitirmeprojesi.ui.fragment.MainFragmentDirections
import com.example.bitirmeprojesi.ui.viewmodel.CartDiscountViewModel
import com.example.bitirmeprojesi.utils.gecisYap
import com.google.android.material.snackbar.Snackbar


class DiscountAdapter(var mContext: Context,
                      var sepetListesi : List<SepetYemek>,
                      var viewModel: CartDiscountViewModel
                   ) : RecyclerView.Adapter<DiscountAdapter.AdapterTutucu>() {

    inner class AdapterTutucu(var binding: HolderDiscountAdapterBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterTutucu {
        val binding : HolderDiscountAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.holder_discount_adapter,parent,false)
        return AdapterTutucu(binding)

    }

    override fun onBindViewHolder(holder: AdapterTutucu, position: Int) {
        val sepet = sepetListesi[position]
        val t = holder.binding




        t.kart = sepet


        t.imageDelete.setOnClickListener {

            AlertDialog.Builder(mContext).setTitle("Uyarı")
                .setMessage("Bu ürünü silmek istediğinize emin misiniz?")
                .setPositiveButton("EVET") { dialog, _ ->
                    viewModel.sepettenYemekSil(sepet.sepet_yemek_id)
                    dialog.dismiss()
                }
                .setNegativeButton("HAYIR") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }


        t.nesne.setOnClickListener {
            val action = CartDiscountDirections.discountToDetail(sepet = sepet, yemek = null)
            Navigation.gecisYap(it, action)
        }



        val imageName = sepet.yemek_resim_adi ?: ""
        if (imageName.isNotBlank()) {
            val url = "http://kasimadalan.pe.hu/yemekler/resimler/$imageName"
            Glide.with(mContext)
                .load(url)
                .override(300, 300)
                .into(t.textMealImage)
        } else {
            t.textMealImage.setImageResource(R.drawable.ic_launcher_background)
        }
    }


    override fun getItemCount(): Int {
        return sepetListesi.size
    }


}