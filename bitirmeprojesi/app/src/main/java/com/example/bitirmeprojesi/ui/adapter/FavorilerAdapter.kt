package com.example.bitirmeprojesi.ui.adapter

import android.content.Context
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bitirmeprojesi.data.entity.FavoriYemekler

import com.example.bitirmeprojesi.databinding.FavorilerAdapterBinding
import com.example.bitirmeprojesi.ui.viewmodel.FavorilerFragmentViewModel
import com.google.android.material.snackbar.Snackbar

class FavorilerAdapter(val mContext: Context,
                       var favoriliste : List<FavoriYemekler> ,
                       val viewModel: FavorilerFragmentViewModel) : RecyclerView.Adapter<FavorilerAdapter.FavoriAdapterTutucu>() {

    inner class FavoriAdapterTutucu(var binding : FavorilerAdapterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriAdapterTutucu {
        val binding = FavorilerAdapterBinding.inflate(LayoutInflater.from(mContext),parent,false)
        return FavoriAdapterTutucu(binding)
    }

    override fun onBindViewHolder(
        holder: FavoriAdapterTutucu,
        position: Int
    ) {

       var favoriler = favoriliste[position]

       var t = holder.binding


        t.kart = favoriler





        t.imageDelete.setOnClickListener {

            AlertDialog.Builder(mContext).setTitle("Uyarı")
                .setMessage("Silmek istediğinize emin misiniz?")
                .setPositiveButton("EVET") { dialog, _ ->
                    viewModel.favoridenYemekSil(favoriler.yemek_id)
                    dialog.dismiss()
                }
                .setNegativeButton("HAYIR") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        Glide.with(mContext).load("http://kasimadalan.pe.hu/yemekler/resimler/${favoriler.yemek_resim_adi}").override(300,300).into(t.textMealImage)





     }

    override fun getItemCount(): Int {
        return favoriliste.size
    }


}
