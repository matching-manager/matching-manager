package com.example.matching_manager.ui.match

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MatchWritingActivityBinding

class MatchWritingActivity : AppCompatActivity() {

    private lateinit var binding : MatchWritingActivityBinding

    private val events = resources.getStringArray(R.array.event_array)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MatchWritingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        val eventAdapter = ArrayAdapter(this@MatchWritingActivity, android.R.layout.simple_spinner_dropdown_item, events)

        spEvent.adapter = eventAdapter


    }
}