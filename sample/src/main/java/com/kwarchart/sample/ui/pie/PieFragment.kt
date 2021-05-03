package com.kwarchart.sample.ui.pie

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
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kwarchart.android.chart.PieChart
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.android.enum.PieChartType
import com.kwarchart.sample.R
import com.kwarchart.sample.databinding.FragmentPieBinding

class PieFragment : Fragment() {

    private lateinit var pieViewModel: PieViewModel
    private var _binding: FragmentPieBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pieViewModel = ViewModelProvider(this).get(PieViewModel::class.java)
        _binding = FragmentPieBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val add: Button = binding.add
        add.setOnClickListener {
            pieViewModel.add()
        }

        val pieChart: ComposeView = binding.chartPie
        pieViewModel.chartData.observe(viewLifecycleOwner, Observer {
            pieChart.apply {
                setContent {
                    PieChart(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(color = Color.White),
                        data = it,
                        type = PieChartType.DOUGHNUT,
                        title = resources.getString(R.string.title_pie_chart),
                        legendPos = LegendPosition.RIGHT
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