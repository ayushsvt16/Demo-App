package com.example.rovdemo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.rovdemo.databinding.ActivityRemoteBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket

class remote : AppCompatActivity() {
    private lateinit var binding: ActivityRemoteBinding
    private lateinit var websocketListener: WebsocketListener
    private lateinit var viewModel: MainViewModel
    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null
    private var isConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRemoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        websocketListener = WebsocketListener(viewModel)

        // Observe only connection status
        viewModel.socketStatus.observe(this, Observer { connected ->
            isConnected = connected
            binding.stat.text = if (connected) "Connected" else "Disconnected"
        })

        // Remove this block if you don’t want to show all received messages
        // viewModel.message.observe(this, Observer { message -> ... })

        binding.Switch.setOnClickListener {
            if (!isConnected) {
                webSocket = okHttpClient.newWebSocket(createRequest(), websocketListener)
                Toast.makeText(this, "Connecting WebSocket...", Toast.LENGTH_SHORT).show()
            } else {
                webSocket?.send("Device Disconnected!!")
                webSocket?.close(1000, "Closed Manually")
                Toast.makeText(this, "Disconnecting WebSocket...", Toast.LENGTH_SHORT).show()
            }
        }

        // Send movement commands via WebSocket only
        binding.upbtn.setOnClickListener {
            webSocket?.send("up")
            Toast.makeText(this, "Moving up...", Toast.LENGTH_SHORT).show()
        }
        binding.downbtn.setOnClickListener {
            webSocket?.send("down")
            Toast.makeText(this, "Moving down...", Toast.LENGTH_SHORT).show()
        }
        binding.forwardbtn.setOnClickListener {
            webSocket?.send("forward")
            Toast.makeText(this, "Moving forward...", Toast.LENGTH_SHORT).show()
        }
        binding.leftbtn.setOnClickListener {
            webSocket?.send("left")
            Toast.makeText(this, "Moving left...", Toast.LENGTH_SHORT).show()
        }
        binding.rightbtn.setOnClickListener {
            webSocket?.send("right")
            Toast.makeText(this, "Moving right...", Toast.LENGTH_SHORT).show()
        }
        binding.backwardbtn.setOnClickListener {
            webSocket?.send("backward")
            Toast.makeText(this, "Moving back...", Toast.LENGTH_SHORT).show()
        }
        binding.ccwbtn.setOnClickListener {
            webSocket?.send("ccw")
            Toast.makeText(this, "Rotating CCW...", Toast.LENGTH_SHORT).show()
        }
        binding.cwbtn.setOnClickListener {
            webSocket?.send("cw")
            Toast.makeText(this, "Rotating CW...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createRequest(): Request {
        val websocketUrl =
            "wss://s14909.blr1.piesocket.com/v3/1?api_key=Hi4AGeft6p6ByGmqyS7Jb0ozwS3uJw5TzsBd7wtq&notify_self=1"
        return Request.Builder().url(websocketUrl).build()
    }
}
