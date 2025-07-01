package com.example.rovdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rovdemo.databinding.ActivitySuggestionViewBinding

class SuggestionView : AppCompatActivity() {

    private lateinit var binding: ActivitySuggestionViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Setup ViewBinding
        binding = ActivitySuggestionViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ Retrieve data from intent using KEY
        val receivedText = intent.getStringExtra(MainActivity2.KEY)

        // ✅ Set it in the TextView
        binding.showsugg.text = receivedText
    }
}
