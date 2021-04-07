package com.kwarchart.android.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.enum.LineChartType
import com.kwarchart.android.model.ChartData
import com.kwarchart.android.model.LineSeries
import org.junit.Rule
import org.junit.Test

class LineChartInstrumentedTest {

    private val mOneLineSeries = arrayListOf(
        LineSeries(
            data = arrayListOf(
                ChartData(1, 50f),
                ChartData(2, 350f),
                ChartData(3, 250f),
                ChartData(4, 200f),
                ChartData(5, 800f),
                ChartData(6, 500f),
                ChartData(7, 600f)
            ),
            type = LineChartType.SMOOTH,
            showDataPoint = true,
            legend = "Spent"
        ),
    )

    private val mTwoLineSeries = arrayListOf(
        mOneLineSeries.first(),
        LineSeries(
            data = arrayListOf(
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
    )

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun lineSeriesGeneralTest() {
        composeTestRule.setContent {
            LineChart(
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp),
                data = mOneLineSeries,
                yAxisName = "Spent",
                xAxisName = "Day"
            )
        }

        // Width test
        composeTestRule.onRoot().assertWidthIsEqualTo(300.dp)
        // Height test
        composeTestRule.onRoot().assertHeightIsEqualTo(300.dp)
        // Y-axis name test
        composeTestRule.onNodeWithText("Spent").assertIsDisplayed()
        // X-axis name test
        composeTestRule.onNodeWithText("Day").assertIsDisplayed()
    }

    @Test
    fun lineSeriesNoAxesNamesTest() {
        composeTestRule.setContent {
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mOneLineSeries
            )
        }

        // Y-axis name test
        composeTestRule.onNodeWithText("Spent").assertDoesNotExist()
        // X-axis name test
        composeTestRule.onNodeWithText("Day").assertDoesNotExist()
    }

    @Test
    fun oneLineSeriesLegendTest() {
        composeTestRule.setContent {
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mOneLineSeries,
                legendPos = LegendPosition.TOP_RIGHT
            )
        }

        composeTestRule.onNodeWithText("Spent").assertIsDisplayed()
    }

    @Test
    fun oneLineSeriesNoLegendTest() {
        composeTestRule.setContent {
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mOneLineSeries,
            )
        }

        composeTestRule.onNodeWithText("Spent").assertDoesNotExist()
    }

    @Test
    fun twoLineSeriesLegendTest() {
        composeTestRule.setContent {
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mTwoLineSeries,
                legendPos = LegendPosition.TOP_RIGHT
            )
        }

        composeTestRule.onNodeWithText("Spent").assertIsDisplayed()
        composeTestRule.onNodeWithText("Budget").assertIsDisplayed()
    }

    @Test
    fun twoLineSeriesNoLegendTest() {
        composeTestRule.setContent {
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mTwoLineSeries,
            )
        }

        composeTestRule.onNodeWithText("Spent").assertDoesNotExist()
        composeTestRule.onNodeWithText("Budget").assertDoesNotExist()
    }

}