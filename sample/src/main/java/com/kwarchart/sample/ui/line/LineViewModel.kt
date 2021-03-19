package com.kwarchart.sample.ui.line

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kwarchart.android.model.ChartData
import kotlin.random.Random

class LineViewModel : ViewModel() {

    private val _chartData = MutableLiveData<MutableList<ChartData<Int>>>().apply {
        value = arrayListOf(
            ChartData(1, 50f),
            ChartData(2, 350f),
            ChartData(3, 250f),
            ChartData(4, 200f),
            ChartData(5, 800f),
            ChartData(6, 500f),
            ChartData(7, 600f)
        )
    }
    val chartData: LiveData<MutableList<ChartData<Int>>> = _chartData

    fun add() {
        _chartData.value!!.add(
            ChartData(
                _chartData.value!!.size + 1,
                Random.nextInt(100, 1000).toFloat()
            )
        )

        _chartData.value = _chartData.value
    }

}