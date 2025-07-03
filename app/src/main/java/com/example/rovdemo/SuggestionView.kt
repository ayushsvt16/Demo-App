package com.example.rovdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rovdemo.MainActivity2.Companion.KEY
import com.example.rovdemo.MainActivity2.Companion.USERNAME
import com.example.rovdemo.databinding.ActivitySuggestionViewBinding

class SuggestionView : AppCompatActivity() {

    private lateinit var binding: ActivitySuggestionViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuggestionViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedText = intent.getStringExtra(KEY)
        val name = intent.getStringExtra(USERNAME)

        binding.showsugg.text = receivedText

        // Update heading with name if available
        binding.textView3.text = if (!name.isNullOrEmpty()) {
            "Thank you for Suggestion ♥\n\nWelcome back, $name"
        } else {
            "Thank you for Suggestion ♥"
        }
    }
}
