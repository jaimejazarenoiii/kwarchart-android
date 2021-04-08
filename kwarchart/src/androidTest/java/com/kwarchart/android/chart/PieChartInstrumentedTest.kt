package com.kwarchart.android.chart

import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.model.ChartData
import com.kwarchart.android.model.PieSeries
import org.junit.Rule
import org.junit.Test

class PieChartInstrumentedTest {

    private val mPieSeries = arrayListOf(
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
            color = Color.Yellow,
            legend = "Transportation"
        ),
    )

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun lineSeriesGeneralTest() {
        composeTestRule.setContent {
            PieChart(
                modifier = Modifier
                    .width(300.dp)
                    .height(300.dp),
                data = mPieSeries,
                title = "This month's transactions"
            )
        }

        // Width test
        composeTestRule.onRoot().assertWidthIsEqualTo(300.dp)
        // Height test
        composeTestRule.onRoot().assertHeightIsEqualTo(300.dp)
        // Title test
        composeTestRule.onNodeWithText("This month's transactions").assertIsDisplayed()
    }

    @Test
    fun lineSeriesNoTitleTest() {
        composeTestRule.setContent {
            PieChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mPieSeries
            )
        }

        // Title test
        composeTestRule.onNodeWithText("This month's transactions").assertDoesNotExist()
    }

    @Test
    fun pieSeriesLegendTest() {
        composeTestRule.setContent {
            PieChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mPieSeries,
                legendPos = LegendPosition.RIGHT
            )
        }

        composeTestRule.onNodeWithText("Bills").assertIsDisplayed()
        composeTestRule.onNodeWithText("Shopping").assertIsDisplayed()
        composeTestRule.onNodeWithText("Food").assertIsDisplayed()
        composeTestRule.onNodeWithText("Transportation").assertIsDisplayed()
    }

    @Test
    fun pieSeriesNoLegendTest() {
        composeTestRule.setContent {
            PieChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                data = mPieSeries
            )
        }

        composeTestRule.onNodeWithText("Bills").assertDoesNotExist()
        composeTestRule.onNodeWithText("Shopping").assertDoesNotExist()
        composeTestRule.onNodeWithText("Food").assertDoesNotExist()
        composeTestRule.onNodeWithText("Transportation").assertDoesNotExist()
    }

}