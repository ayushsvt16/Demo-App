package com.example.rovdemo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.rovdemo.databinding.ActivityMain1Binding

class MainActivity1 : AppCompatActivity() {

    private lateinit var binding: ActivityMain1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate using ViewBinding
        binding = ActivityMain1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("ThemePrefs", MODE_PRIVATE)
        val sidebarLayout = binding.drawerLayout.getChildAt(1) as LinearLayout

        // Open drawer
        binding.btnMenu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.drawerLayout.setBackgroundColor(Color.WHITE)

        // Upload button action
        binding.btnUpload.setOnClickListener {
            Toast.makeText(this, "Uploading...", Toast.LENGTH_SHORT).show()
        }

        // Download button action
        binding.btnDownload.setOnClickListener {
            Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show()
        }

        // Dark mode
        binding.Dark.setOnClickListener {
            binding.main.setBackgroundColor(Color.BLACK)
            sidebarLayout.setBackgroundColor(Color.BLACK)
            sharedPreferences.edit().putString("theme", "dark").apply()

            for (i in 0 until sidebarLayout.childCount) {
                val view = sidebarLayout.getChildAt(i)
                if (view is Button || view is TextView) {
                    view.setTextColor(Color.WHITE)
                }
            }
            binding.Dark.setTextColor(Color.BLACK)
            binding.Light.setTextColor(Color.BLACK)
        }

        // Light mode
        binding.Light.setOnClickListener {
            binding.main.setBackgroundColor(Color.WHITE)
            sidebarLayout.setBackgroundColor(Color.parseColor("#EEEEEE"))
            sharedPreferences.edit().putString("theme", "light").apply()

            for (i in 0 until sidebarLayout.childCount) {
                val view = sidebarLayout.getChildAt(i)
                if (view is Button || view is TextView) {
                    view.setTextColor(Color.BLACK)
                    binding.Dark.setTextColor(Color.BLACK)
                    binding.Light.setTextColor(Color.BLACK)
                }
            }
        }

        // Navigate to About
        binding.About.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }

        binding.Remote.setOnClickListener{
            startActivity(Intent(this, RemoteActivity::class.java))
        }
    }
}
