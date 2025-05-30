package com.example.bitirmeprojesi.data.repository

import android.util.Log
import com.example.bitirmeprojesi.data.Retrofit.YemeklerDaoRetrofit
import com.example.bitirmeprojesi.data.datasource.YemeklerLocalDataSource
import com.example.bitirmeprojesi.data.entity.Yemekler
import javax.inject.Inject

class YemekRetroRepository (var yds : YemeklerLocalDataSource) {

    suspend fun tumYemekleriGetir() : List<Yemekler> = yds.yemekleriYukle()









}
