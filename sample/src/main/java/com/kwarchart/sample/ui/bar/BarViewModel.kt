package com.kwarchart.sample.ui.bar

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kwarchart.android.model.BarSeries
import com.kwarchart.android.model.ChartData
import com.kwarchart.android.model.Legend
import kotlin.random.Random

class BarViewModel : ViewModel() {

    val spinnerData: List<String> = listOf(
        "Vertical Normal 1 Datasource",
        "Vertical Normal 2 Datasource",
        "Vertical Stacked 1 Datasource",
        "Vertical Stacked 2 Datasource"
    )
    private val _selectedSpinnerData: MutableLiveData<String> = MutableLiveData<String>().apply { value = spinnerData[0] }
    val selectedSpinnerData: LiveData<String> = _selectedSpinnerData

    /**
     * BarSeries for expense.
     */
    private lateinit var _spentSeries: BarSeries<Int>

    /**
     * LineSeries for budget goal.
     */
    private lateinit var _goalSeries: BarSeries<Int>

    /**
     * BarSeries for expense.
     */
    lateinit var spentSeries: BarSeries<Int>

    /**
     * BarSeries for budget goal.
     */
    lateinit var goalSeries: BarSeries<Int>

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

        _spentSeries = BarSeries(
            data = mutableListOf(
                ChartData(1, 50f),
                ChartData(2, 350f),
                ChartData(3, 250f),
                ChartData(4, 200f),
                ChartData(5, 800f),
                ChartData(6, 500f),
                ChartData(7, 600f)
            ),
            colors = arrayListOf(spentColor),
            radius = 5f,
            legend = Legend("Spent")
        )
        spentSeries = _spentSeries

        _goalSeries = BarSeries(
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
            radius = 10f,
            legend = Legend("Budget", RectangleShape)
        )
        goalSeries = _goalSeries

        _isInit = true
    }

    /**
     * Add random BarSeries.
     */
    fun add() {
        (_spentSeries.data as MutableList).add(
            ChartData(
                _spentSeries.data.size + 1,
                Random.nextInt(100, 1000).toFloat()
            ),
        )
        (_goalSeries.data as MutableList).add(
            ChartData(
                _goalSeries.data.size + 1,
                Random.nextInt(100, 1000).toFloat()
            ),
        )
        _selectedSpinnerData.value = _selectedSpinnerData.value
    }

    /**
     *
     */
    fun changeData(data: String) {
        _selectedSpinnerData.value = data
    }
}