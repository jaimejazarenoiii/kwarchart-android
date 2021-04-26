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
import kotlin.random.Random

class PieChartInstrumentedTest {

    private val mPieSeries = arrayListOf<PieSeries<String>>()

    init {
        val initialLabels = arrayListOf(
            "Bills", "Shopping", "Food", "Transportation"
        )
        val initialValues = arrayListOf(
            1050f, 500f, 2050f, 800f
        )

        initialValues.forEachIndexed { i, fl ->
            mPieSeries.add(
                PieSeries(
                    data = ChartData(initialLabels[i], fl),
                    color = Color(
                        Random.nextInt(255),
                        Random.nextInt(255),
                        Random.nextInt(255)
                    ),
                    legend = "${initialLabels[i]} ($fl)"
                )
            )
        }
    }

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

        mPieSeries.forEach {
            composeTestRule.onNodeWithText("${it.legend}").assertIsDisplayed()
        }
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

        mPieSeries.forEach {
            composeTestRule.onNodeWithText("${it.legend}").assertDoesNotExist()
        }
    }

}