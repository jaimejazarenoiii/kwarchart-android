package com.kwarchart.sample.ui.bar

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
import com.kwarchart.android.chart.BarChart
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.sample.databinding.FragmentBarBinding

class BarFragment : Fragment() {

    private lateinit var barViewModel: BarViewModel
    private var _binding: FragmentBarBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        barViewModel =
            ViewModelProvider(this).get(BarViewModel::class.java)

        _binding = FragmentBarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val add: Button = binding.add
        add.setOnClickListener {
            barViewModel.add()
        }

        val barChart: ComposeView = binding.chartBar
        barViewModel.spentSeries.observe(viewLifecycleOwner, Observer {
            barChart.apply {
                setContent {
                    BarChart(
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .background(color = Color.White),
                            data = arrayListOf(
                                    barViewModel.spentSeries.value!!,
                                    barViewModel.goalSeries
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