package com.kwarchart.sample.ui.bar

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kwarchart.android.model.BarSeries
import com.kwarchart.android.model.ChartData
import kotlin.random.Random

class BarViewModel : ViewModel() {

    val spinnerData: List<String> = listOf("Vertical Normal 1 Datasource", "Vertical Normal 2 Datasource", "Vertical Stacked 1 Datasource", "Vertical Stacked 2 Datasource")
    private val _selectedSpinnerData: MutableLiveData<String> = MutableLiveData<String>().apply { value = spinnerData[0] }
    val selectedSpinnerData: LiveData<String> = _selectedSpinnerData

    /**
     * BarSeries for expense.
     */
    private val _spentSeries = BarSeries(
            data = mutableListOf(
                    ChartData(1, 50f),
                    ChartData(2, 350f),
                    ChartData(3, 250f),
                    ChartData(4, 200f),
                    ChartData(5, 800f),
                    ChartData(6, 500f),
                    ChartData(7, 600f)
            ),
            legend = "Spent"
    )

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
    val spentSeries: BarSeries<Int> = _spentSeries

    /**
     * BarSeries for budget goal.
     */
    val goalSeries: BarSeries<Int> = _goalSeries

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

    fun changeData(data: String) {
        _selectedSpinnerData.value = data
    }
}