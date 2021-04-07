package com.kwarchart.sample.ui.bar

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kwarchart.android.enum.BarChartType
import com.kwarchart.android.model.BarSeries
import com.kwarchart.android.model.ChartData
import kotlin.random.Random

class BarViewModel : ViewModel() {

    /**
     * BarSeries for expense.
     */
    private val _spentSeries = MutableLiveData<BarSeries<Int>>().apply {
        value = BarSeries(
                data = mutableListOf(
                        ChartData(1, 50f),
                        ChartData(2, 350f),
                        ChartData(3, 250f),
                        ChartData(4, 200f),
                        ChartData(5, 800f),
                        ChartData(6, 500f),
                        ChartData(7, 600f)
                ),
                type = BarChartType.NORMAL,
                legend = "Spent"
        )
    }

    /**
     * LineSeries for budget goal.
     */
    private val _goalSeries = BarSeries(
            data = mutableListOf(
                    ChartData(1, 100f),
                    ChartData(2, 300f),
                    ChartData(3, 200f),
                    ChartData(4, 200f),
                    ChartData(5, 800f),
                    ChartData(6, 500f),
                    ChartData(7, 610f)
            ),
            color = Color.Green,
            legend = "Budget"
    )

    /**
     * BarSeries for expense.
     */
    val spentSeries: LiveData<BarSeries<Int>> = _spentSeries

    /**
     * LineSeries for budget goal.
     */
    val goalSeries: BarSeries<Int> = _goalSeries

    /**
     * Add random BarSeries.
     */
    fun add() {
        (_spentSeries.value!!.data as MutableList).add(
                ChartData(
                        _spentSeries.value!!.data.size + 1,
                        Random.nextInt(100, 1000).toFloat()
                ),
        )
        (_goalSeries.data as MutableList).add(
                ChartData(
                        _goalSeries.data.size + 1,
                        Random.nextInt(100, 1000).toFloat()
                ),
        )

        _spentSeries.value = _spentSeries.value
    }

}