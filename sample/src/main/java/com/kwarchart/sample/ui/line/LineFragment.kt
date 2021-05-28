package com.kwarchart.sample.ui.line

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kwarchart.android.chart.LineChart
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.model.AxesStyle
import com.kwarchart.android.model.GridsStyle
import com.kwarchart.android.model.Style
import com.kwarchart.sample.R
import com.kwarchart.sample.databinding.FragmentLineBinding

class LineFragment : Fragment() {

    private lateinit var lineViewModel: LineViewModel
    private var _binding: FragmentLineBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        lineViewModel = ViewModelProvider(this).get(LineViewModel::class.java)
        _binding = FragmentLineBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val add: Button = binding.add
        add.setOnClickListener {
            lineViewModel.add()
        }

        val lineChart: ComposeView = binding.chartLine
        lineViewModel.spentSeries.observe(viewLifecycleOwner, Observer {
            lineChart.apply {
                setContent {
                    LineChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(color = Color.White),
                        data = arrayListOf(
                            lineViewModel.spentSeries.value!!,
                            lineViewModel.goalSeries
                        ),
                        title = resources.getString(R.string.title_line_chart),
                        axesStyle = AxesStyle(
                            xStyle = Style(
                                color = Color(0xffe4eaef),
                                strokeWidth = 10f
                            ),
                            yStyle = Style(
                                color = Color(0xffe4eaef),
                                strokeWidth = 10f
                            )
                        ),
                        gridsStyle = GridsStyle(
                            horizontal = Style(
                                color = Color(0xffe5ebef),
                                strokeStyle = PathEffect.dashPathEffect(intervals = floatArrayOf(5f, 5f))
                            ),
                            vertical = Style(
                                color = Color(0xffe5ebef),
                                strokeStyle = PathEffect.dashPathEffect(intervals = floatArrayOf(5f, 5f))
                            )
                        ),
                        legendPos = LegendPosition.TOP_RIGHT
                    )
                }
            }
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}