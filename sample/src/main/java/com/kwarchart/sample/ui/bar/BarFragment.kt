package com.kwarchart.sample.ui.bar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kwarchart.android.chart.BarChart
import com.kwarchart.android.enum.BarChartType
import com.kwarchart.android.enum.LegendPosition
import com.kwarchart.sample.databinding.FragmentBarBinding

class BarFragment : Fragment(), AdapterView.OnItemSelectedListener {

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
        binding.spinner.apply {
            val aa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, barViewModel.spinnerData)
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            onItemSelectedListener = this@BarFragment
            adapter = aa
        }
        val barChart: ComposeView = binding.chartBar
        barViewModel.selectedSpinnerData.observe(viewLifecycleOwner, {
            barChart.apply {
                setContent {
                    BarChart(
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .background(color = Color.White),
                            data = if (it == "0" || it == "2") {
                                arrayListOf(barViewModel.spentSeries)
                            } else {
                                arrayListOf(barViewModel.goalSeries, barViewModel.spentSeries)
                            },
                            legendPos = LegendPosition.TOP_RIGHT,
                            type = if (it == "0" || it == "1") {
                                BarChartType.VERTICAL
                            } else {
                                BarChartType.VERTICAL_STACKED
                            },
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

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        barViewModel.changeData(p2.toString())
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}
