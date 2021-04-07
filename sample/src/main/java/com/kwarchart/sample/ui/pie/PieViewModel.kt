package com.kwarchart.sample.ui.pie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kwarchart.android.model.ChartData

class PieViewModel : ViewModel() {

    private val _chartData = MutableLiveData<MutableList<ChartData<String>>>().apply {
        value = arrayListOf(
            ChartData("Bills", 1050f),
            ChartData("Shopping", 500f),
            ChartData("Food", 2050f),
            ChartData("Transportation", 800f),
        )
    }
    val chartData: LiveData<MutableList<ChartData<String>>> = _chartData

}