package com.example.rovdemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.rovdemo.databinding.ActivityMain1Binding
import com.example.rovdemo.databinding.ActivityRemoteBinding

class remote : AppCompatActivity() {
    lateinit var binding: ActivityRemoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_remote)

        // Inflate using ViewBinding
        binding = ActivityRemoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.upbtn.setOnClickListener{
            Toast.makeText(this, "Moving up...", Toast.LENGTH_SHORT).show()
        }
        binding.downbtn.setOnClickListener{
            Toast.makeText(this, "Moving down...", Toast.LENGTH_SHORT).show()
        }
        binding.forwardbtn.setOnClickListener{
            Toast.makeText(this, "Moving forward...", Toast.LENGTH_SHORT).show()
        }
        binding.leftbtn.setOnClickListener{
            Toast.makeText(this, "Moving left...", Toast.LENGTH_SHORT).show()
        }
        binding.rightbtn.setOnClickListener{
            Toast.makeText(this, "Moving right...", Toast.LENGTH_SHORT).show()
        }
        binding.backwardbtn.setOnClickListener{
            Toast.makeText(this, "Moving back...", Toast.LENGTH_SHORT).show()
        }
        binding.ccwbtn.setOnClickListener{
            Toast.makeText(this, "Moving CCW...", Toast.LENGTH_SHORT).show()
        }
        binding.cwbtn.setOnClickListener{
            Toast.makeText(this, "Moving CW...", Toast.LENGTH_SHORT).show()
        }
    }
}