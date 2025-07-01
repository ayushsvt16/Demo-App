package com.example.rovdemo

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.example.rovdemo.databinding.ActivityMain2Binding
import android.text.method.ScrollingMovementMethod

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private val scrollHandler = Handler(Looper.getMainLooper())
    private var scrollY = 0

    companion object {
        const val KEY = "package com.example.rovdemo.MainActivity2,KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // HTML text setup
        val html = HtmlCompat.fromHtml(getString(R.string.rov_paragraph), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.textView.text = html
        binding.textView2.text = html

        binding.textView.setTextColor(Color.BLACK)
        binding.textView2.setTextColor(Color.BLACK)

        // Theme logic
        val theme = getSharedPreferences("ThemePrefs", MODE_PRIVATE).getString("theme", "light")
        if (theme == "dark") {
            binding.layoutMain2.setBackgroundColor(Color.BLACK)
        } else {
            binding.layoutMain2.setBackgroundColor(Color.WHITE)
        }

        // Enable scrolling inside EditText
        binding.sugg.movementMethod = ScrollingMovementMethod.getInstance()

        // Auto-scroll paragraph
        scrollHandler.post(object : Runnable {
            override fun run() {
                val contentHeight = binding.scrollView.getChildAt(0).height
                val scrollViewHeight = binding.scrollView.height

                scrollY += 1
                if (scrollY >= contentHeight - scrollViewHeight) {
                    scrollY = 0
                }
                binding.scrollView.scrollTo(0, scrollY)
                scrollHandler.postDelayed(this, 20)
            }
        })

        // Open PDF
        binding.PDF.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://drive.google.com/file/d/1KNk1527VzmgCvBDcG4NJ_WCwsIZp1OaJ/view?usp=sharing")
            startActivity(intent)
        }

        // Open Camera
        binding.Camera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(intent)
        }

        // Submit suggestion
        binding.submit.setOnClickListener {
            val Sugg = binding.sugg.text.toString()
            val intent = Intent(this, SuggestionView::class.java)
            intent.putExtra(KEY, Sugg)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scrollHandler.removeCallbacksAndMessages(null)
    }
}
