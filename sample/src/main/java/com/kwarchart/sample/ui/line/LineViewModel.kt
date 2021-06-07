package com.kwarchart.sample.ui.line

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kwarchart.android.enum.LineChartType
import com.kwarchart.android.model.ChartData
import com.kwarchart.android.model.Legend
import com.kwarchart.android.model.LineSeries
import kotlin.random.Random

class LineViewModel : ViewModel() {

    /**
     * LineSeries for expense.
     */
    private lateinit var _spentSeries: MutableLiveData<LineSeries<Int>>

    /**
     * LineSeries for budget goal.
     */
    private lateinit var _goalSeries: LineSeries<Int>

    /**
     * LineSeries for expense.
     */
    lateinit var spentSeries: LiveData<LineSeries<Int>>

    /**
     * LineSeries for budget goal.
     */
    lateinit var goalSeries: LineSeries<Int>

    /**
     * Is initialized state.
     */
    private var _isInit = false

    /**
     * Initialize data.
     */
    fun init(spentColor: Color, budgetColor: Color) {
        if (_isInit) {
            return
        }

        _spentSeries = MutableLiveData<LineSeries<Int>>().apply {
            value = LineSeries(
                data = mutableListOf(
                    ChartData(1, 50f),
                    ChartData(2, 350f),
                    ChartData(3, 250f),
                    ChartData(4, 200f),
                    ChartData(5, 800f),
                    ChartData(6, 500f),
                    ChartData(7, 600f)
                ),
                type = LineChartType.SMOOTH,
                colors = arrayListOf(spentColor),
                showDataPoint = true,
                legend = Legend("Spent")
            )
        }
        spentSeries = _spentSeries

        _goalSeries = LineSeries(
            data = mutableListOf(
                ChartData(1, 100f),
                ChartData(2, 300f),
                ChartData(3, 200f),
                ChartData(4, 200f),
                ChartData(5, 800f),
                ChartData(6, 500f),
                ChartData(7, 610f)
            ),
            colors = arrayListOf(budgetColor),
            legend = Legend("Budget", RectangleShape)
        )
        goalSeries = _goalSeries

        _isInit = true
    }

    /**
     * Add random LineSeries.
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