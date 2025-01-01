package com.example.coffeeshop.ui.admin.setting.statistics

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coffeeshop.R
import com.example.coffeeshop.data.model.Order
import com.example.coffeeshop.data.remote.RemoteOrderDataSource
import com.example.coffeeshop.data.repository.order.OrderRepositoryImpl
import com.example.coffeeshop.databinding.ActivityStatisticsBinding
import com.example.coffeeshop.ui.user.history.complete.CompleteAdapter
import com.example.coffeeshop.ui.user.history.complete.DetailCompleteActivity
import com.example.coffeeshop.ui.user.history.complete.OnClickDetailOrder
import com.example.coffeeshop.utils.Constant
import java.util.Calendar

class StatisticsActivity : AppCompatActivity() {
    private val remoteOrderDataSource = RemoteOrderDataSource()
    private val orderRepository = OrderRepositoryImpl(remoteOrderDataSource)
    private val viewModel: StatisticsViewModel by viewModels {
        StatisticsViewModel.Factory(orderRepository)
    }
    private lateinit var binding: ActivityStatisticsBinding
    private lateinit var adapter: CompleteAdapter
    private var startTime: Long = 0
    private var endTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()
        setupView()
    }

    private fun setupView() {
        adapter = CompleteAdapter(object : OnClickDetailOrder {
            override fun onClickDetailOrder(order: Order) {
                val intent = Intent(this@StatisticsActivity, DetailCompleteActivity::class.java)
                intent.putExtra("orderId", order.orderId)
                startActivity(intent)
            }
        })
        binding.rvOrders.adapter = adapter
        viewModel.order.observe(this) {
            adapter.setData(it)
            Toast.makeText(this, "$it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupViewModel() {
        binding.btnStartDate.setOnClickListener {
            showDatePickerDialog { year, month, dayOfMonth ->
                binding.btnStartDate.text = "$dayOfMonth/$month/$year"
                startTime = getDateInMillis(year, month, dayOfMonth)
                toggleFilterButton()
            }
        }
        binding.btnEndDate.setOnClickListener {
            showDatePickerDialog { year, month, dayOfMonth ->
                binding.btnEndDate.text = "$dayOfMonth/$month/$year"
                endTime = getDateInMillis(year, month, dayOfMonth)
                toggleFilterButton()
            }
        }
        binding.btnFilter.setOnClickListener {
            if (startTime != 0L && endTime != 0L && startTime <= endTime) {
                viewModel.getOrderByDate(startTime, endTime)
            } else {
                Toast.makeText(this, "Vui lòng chọn khoảng thời gian hợp lệ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleFilterButton() {
        binding.btnFilter.isEnabled = startTime != 0L && endTime != 0L && startTime <= endTime
    }


    private fun showDatePickerDialog(onDateSetListener: (Int, Int, Int) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, y, m, d ->
            onDateSetListener(y, m + 1, d)
        }, year, month, dayOfMonth)

        datePickerDialog.show()
    }

    private fun getDateInMillis(year: Int, month: Int, dayOfMonth: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, dayOfMonth, 0, 0, 0)
        return calendar.timeInMillis
    }
}