package com.kwarchart.sample.ui.pie

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kwarchart.android.model.ChartData
import com.kwarchart.android.model.PieSeries
import kotlin.random.Random

class PieViewModel : ViewModel() {

    /**
     * PieChart data.
     */
    private val _chartData = MutableLiveData<MutableList<PieSeries<String>>>().apply {
        value = arrayListOf(
            PieSeries(
                data = ChartData("Bills", 1050f),
                color = Color.Red,
                legend = "Bills"
            ),
            PieSeries(
                data = ChartData("Shopping", 500f),
                color = Color.Green,
                legend = "Shopping"
            ),
            PieSeries(
                data = ChartData("Food", 2050f),
                color = Color.Blue,
                legend = "Food"
            ),
            PieSeries(
                data = ChartData("Transportation", 800f),
                legend = "Transportation"
            ),
        )
    }

    /**
     * PieChart data.
     */
    val chartData: LiveData<MutableList<PieSeries<String>>> = _chartData

    /**
     * Add random PieSeries.
     */
    fun add() {
        _chartData.value!!.size
        _chartData.value!!.add(
            PieSeries(
                data = ChartData(
                    "New ${_chartData.value!!.size}",
                    Random.nextInt(100, 1000).toFloat()
                ),
                color = Color(
                    Random.nextInt(255),
                    Random.nextInt(255),
                    Random.nextInt(255)
                ),
                legend = "New ${_chartData.value!!.size}"
            )
        )

        _chartData.value = _chartData.value
    }
}