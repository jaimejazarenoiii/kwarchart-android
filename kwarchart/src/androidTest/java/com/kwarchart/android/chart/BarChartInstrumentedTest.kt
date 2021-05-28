package com.kwarchart.android.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.kwarchart.android.enum.BarChartType
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.model.AxesStyle
import com.kwarchart.android.model.BarSeries
import com.kwarchart.android.model.ChartData
import org.junit.Rule
import org.junit.Test

class BarChartInstrumentedTest {

    private val mOneBarSeries = arrayListOf(
        BarSeries(
            data = arrayListOf(
                ChartData(1, 50f),
                ChartData(2, 350f),
                ChartData(3, 250f),
                ChartData(4, 200f),
                ChartData(5, 800f),
                ChartData(6, 500f),
                ChartData(7, 600f)
            ),
            legend = "Spent"
        ),
    )

    private val mTwoBarSeries = arrayListOf(
        mOneBarSeries.first(),
        BarSeries(
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
    fun barSeriesGeneralTest() {
        composeTestRule.setContent {
            BarChart(
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp)
                    .background(color = Color.White),
                title = "This week's transactions",
                data = mOneBarSeries,
                type = BarChartType.VERTICAL,
                axesStyle = AxesStyle(
                    xName = "Day",
                    yName = "Spent",
                )
            )
        }

        // Width test
        composeTestRule.onRoot().assertWidthIsEqualTo(300.dp)
        // Height test
        composeTestRule.onRoot().assertHeightIsEqualTo(300.dp)
        // Title test
        composeTestRule.onNodeWithText("This week's transactions").assertIsDisplayed()
        // Y-axis name test
        composeTestRule.onNodeWithText("Spent").assertIsDisplayed()
        // X-axis name test
        composeTestRule.onNodeWithText("Day").assertIsDisplayed()
    }

    @Test
    fun barSeriesNoTitleTest() {
        composeTestRule.setContent {
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mOneBarSeries,
                axesStyle = AxesStyle(
                    xName = "Day",
                    yName = "Spent",
                )
            )
        }

        // Title test
        composeTestRule.onNodeWithText("This week's transactions").assertDoesNotExist()
        // Y-axis name test
        composeTestRule.onNodeWithText("Spent").assertIsDisplayed()
        // X-axis name test
        composeTestRule.onNodeWithText("Day").assertIsDisplayed()
    }

    @Test
    fun barSeriesNoAxesNamesTest() {
        composeTestRule.setContent {
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mOneBarSeries,
                title = "This week's transactions"
            )
        }

        // Title test
        composeTestRule.onNodeWithText("This week's transactions").assertIsDisplayed()
        // Y-axis name test
        composeTestRule.onNodeWithText("Spent").assertDoesNotExist()
        // X-axis name test
        composeTestRule.onNodeWithText("Day").assertDoesNotExist()
    }

    @Test
    fun oneBarSeriesLegendTest() {
        composeTestRule.setContent {
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mOneBarSeries,
                legendPos = LegendPosition.TOP_RIGHT
            )
        }

        composeTestRule.onNodeWithText("Spent").assertIsDisplayed()
    }

    @Test
    fun oneBarSeriesVerticalStackedTest() {
        composeTestRule.setContent {
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mOneBarSeries,
                legendPos = LegendPosition.TOP_RIGHT,
                type = BarChartType.VERTICAL_STACKED
            )
        }

        composeTestRule.onNodeWithText("Spent").assertIsDisplayed()
    }

    @Test
    fun oneBarSeriesHorizontalTest() {
        composeTestRule.setContent {
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mOneBarSeries,
                legendPos = LegendPosition.TOP_RIGHT,
                type = BarChartType.HORIZONTAL
            )
        }

        composeTestRule.onNodeWithText("Spent").assertIsDisplayed()
    }

    @Test
    fun oneBarSeriesHorizontalStackedTest() {
        composeTestRule.setContent {
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mOneBarSeries,
                legendPos = LegendPosition.TOP_RIGHT,
                type = BarChartType.HORIZONTAL_STACKED
            )
        }

        composeTestRule.onNodeWithText("Spent").assertIsDisplayed()
    }

    @Test
    fun oneBarSeriesNoLegendTest() {
        composeTestRule.setContent {
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mOneBarSeries,
            )
        }

        composeTestRule.onNodeWithText("Spent").assertDoesNotExist()
    }

    @Test
    fun twoBarSeriesLegendTest() {
        composeTestRule.setContent {
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mTwoBarSeries,
                legendPos = LegendPosition.TOP_RIGHT
            )
        }

        composeTestRule.onNodeWithText("Spent").assertIsDisplayed()
        composeTestRule.onNodeWithText("Budget").assertIsDisplayed()
    }

    @Test
    fun twoBarSeriesNoLegendTest() {
        composeTestRule.setContent {
            BarChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mTwoBarSeries,
            )
        }

        composeTestRule.onNodeWithText("Spent").assertDoesNotExist()
        composeTestRule.onNodeWithText("Budget").assertDoesNotExist()
    }

    @Test
    fun twoBarSeriesVerticalStackedTest() {
        composeTestRule.setContent {
            BarChart(
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp),
                data = mTwoBarSeries,
                type = BarChartType.VERTICAL_STACKED
            )
        }

        composeTestRule.onNodeWithText("Spent").assertDoesNotExist()
        composeTestRule.onNodeWithText("Budget").assertDoesNotExist()
    }

    @Test
    fun twoBarSeriesHorizontalTest() {
        composeTestRule.setContent {
            BarChart(
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp),
                data = mTwoBarSeries,
                type = BarChartType.HORIZONTAL
            )
        }

        composeTestRule.onNodeWithText("Spent").assertDoesNotExist()
        composeTestRule.onNodeWithText("Budget").assertDoesNotExist()
    }

    @Test
    fun twoBarSeriesHorizontalStackedTest() {
        composeTestRule.setContent {
            BarChart(
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp),
                data = mTwoBarSeries,
                type = BarChartType.HORIZONTAL_STACKED
            )
        }

        composeTestRule.onNodeWithText("Spent").assertDoesNotExist()
        composeTestRule.onNodeWithText("Budget").assertDoesNotExist()
    }

}