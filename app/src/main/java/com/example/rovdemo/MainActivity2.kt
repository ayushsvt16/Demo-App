package com.example.rovdemo

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.example.rovdemo.databinding.ActivityMain2Binding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    private val scrollHandler = Handler(Looper.getMainLooper())
    private var scrollY = 0

    companion object {
        const val KEY = "package com.example.rovdemo.MainActivity2,KEY"
    }

    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load HTML content into textViews
        val html = HtmlCompat.fromHtml(getString(R.string.rov_paragraph), HtmlCompat.FROM_HTML_MODE_LEGACY)
        binding.textView.text = html
        binding.textView2.text = html

        // Scroll suggestion EditText
        binding.sugg.isVerticalScrollBarEnabled = true
        binding.sugg.setSingleLine(false)

        // Access inner TextViews of CardViews
        val pdfText = binding.PDF.getChildAt(0) as TextView
        val cameraText = binding.Camera.getChildAt(0) as TextView
        val submitBtn = binding.submit
        val suggBox = binding.sugg

        // Apply theme
        val theme = getSharedPreferences("ThemePrefs", MODE_PRIVATE).getString("theme", "light")
        if (theme == "dark") {
            binding.layoutMain2.setBackgroundColor(Color.BLACK)

            // Text colors
            binding.textView.setTextColor(Color.BLACK)
            binding.textView2.setTextColor(Color.BLACK)
            suggBox.setTextColor(Color.BLACK)
            suggBox.setHintTextColor(Color.BLACK)

            pdfText.setTextColor(Color.BLACK)
            cameraText.setTextColor(Color.BLACK)
            submitBtn.setTextColor(Color.BLACK)
        } else {
            binding.layoutMain2.setBackgroundColor(Color.WHITE)

            binding.textView.setTextColor(Color.BLACK)
            binding.textView2.setTextColor(Color.BLACK)
            suggBox.setTextColor(Color.BLACK)
            suggBox.setHintTextColor(Color.BLACK)

            pdfText.setTextColor(Color.BLACK)
            cameraText.setTextColor(Color.BLACK)
            submitBtn.setTextColor(Color.BLACK)
        }

        // Auto-scroll paragraph
        scrollHandler.post(object : Runnable {
            override fun run() {
                val contentHeight = binding.scrollView.getChildAt(0).height
                val scrollViewHeight = binding.scrollView.height
                scrollY += 1
                if (scrollY >= contentHeight - scrollViewHeight) scrollY = 0
                binding.scrollView.scrollTo(0, scrollY)
                scrollHandler.postDelayed(this, 20)
            }
        })

        // PDF
        binding.PDF.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://drive.google.com/file/d/1KNk1527VzmgCvBDcG4NJ_WCwsIZp1OaJ/view?usp=sharing")
            startActivity(intent)
        }

        // Camera
        binding.Camera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(intent)
        }

        // Suggestion Submit
        submitBtn.setOnClickListener {
            val suggestion = suggBox.text.toString().trim()
            val name = binding.name.editText?.text.toString().trim()

            // 🔒 Name validation
            if (name.isEmpty()) {
                binding.name.error = "Please enter your name"
                return@setOnClickListener
            } else {
                binding.name.error = null // clear error if valid
            }

            val user = User(name, suggestion)

            database = FirebaseDatabase.getInstance().getReference("Users")
            database.child(name).setValue(user).addOnSuccessListener {
                Toast.makeText(this, "Suggestion Submitted", Toast.LENGTH_SHORT).show()

                // Clear input fields
                binding.name.editText?.setText("")
                binding.sugg.setText("")

                val intent = Intent(this, SuggestionView::class.java)
                intent.putExtra(KEY, suggestion)
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this, "Unable to submit Suggestion", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        scrollHandler.removeCallbacksAndMessages(null)
    }
}
