package com.kwarchart.sample.ui.pie

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kwarchart.android.model.ChartData
import com.kwarchart.android.model.Legend
import com.kwarchart.android.model.PieSeries
import kotlin.random.Random

class PieViewModel : ViewModel() {

    /**
     * PieChart data.
     */
    private val _chartData = MutableLiveData<MutableList<PieSeries<String>>>().apply {
        val initialLabels = arrayListOf(
            "Bills", "Shopping", "Food", "Transportation"
        )
        val initialValues = arrayListOf(
            1050f, 500f, 2050f, 800f
        )
        val initialPieSeries = arrayListOf<PieSeries<String>>()

        initialValues.forEachIndexed { i, fl ->
            initialPieSeries.add(
                PieSeries(
                    data = ChartData(initialLabels[i], fl),
                    colors = arrayListOf(Color(
                        Random.nextInt(255),
                        Random.nextInt(255),
                        Random.nextInt(255)
                    )),
                    legend = Legend("${initialLabels[i]} ($fl)")
                )
            )
        }

        value = initialPieSeries
    }

    /**
     * PieChart data.
     */
    val chartData: LiveData<MutableList<PieSeries<String>>> = _chartData

    /**
     * Add random PieSeries.
     */
    fun add() {
        val label = "New ${_chartData.value!!.size}"
        val value = Random.nextInt(100, 1000).toFloat()

        _chartData.value!!.add(
            PieSeries(
                data = ChartData(label, value),
                colors = arrayListOf(Color(
                    Random.nextInt(255),
                    Random.nextInt(255),
                    Random.nextInt(255)
                )),
                legend = Legend("$label ($value)")
            )
        )

        _chartData.value = _chartData.value
    }
}