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
import org.json.JSONObject

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
        websocketListener = WebsocketListener(viewModel) { socket ->
            sendRegisterMessage(socket)
        }

        viewModel.socketStatus.observe(this, Observer { connected ->
            isConnected = connected
            binding.stat.text = if (connected) "Connected" else "Disconnected"
        })

        binding.Switch.setOnClickListener {
            if (!isConnected) {
                webSocket = okHttpClient.newWebSocket(createRequest(), websocketListener)
                Toast.makeText(this, "Connecting WebSocket...", Toast.LENGTH_SHORT).show()
            } else {
                sendJson("disconnect", "app", mapOf("reason" to "Closed Manually"))
                webSocket?.close(1000, "Closed Manually")
                Toast.makeText(this, "Disconnecting WebSocket...", Toast.LENGTH_SHORT).show()
            }
        }

        // Movement Commands
        binding.upbtn.setOnClickListener {
            sendJson("movement", "app", mapOf("direction" to "up"))
            Toast.makeText(this, "Moving up...", Toast.LENGTH_SHORT).show()
        }
        binding.downbtn.setOnClickListener {
            sendJson("movement", "app", mapOf("direction" to "down"))
            Toast.makeText(this, "Moving down...", Toast.LENGTH_SHORT).show()
        }
        binding.forwardbtn.setOnClickListener {
            sendJson("movement", "app", mapOf("direction" to "forward"))
            Toast.makeText(this, "Moving forward...", Toast.LENGTH_SHORT).show()
        }
        binding.backwardbtn.setOnClickListener {
            sendJson("movement", "app", mapOf("direction" to "backward"))
            Toast.makeText(this, "Moving backward...", Toast.LENGTH_SHORT).show()
        }
        binding.leftbtn.setOnClickListener {
            sendJson("movement", "app", mapOf("direction" to "left"))
            Toast.makeText(this, "Moving left...", Toast.LENGTH_SHORT).show()
        }
        binding.rightbtn.setOnClickListener {
            sendJson("movement", "app", mapOf("direction" to "right"))
            Toast.makeText(this, "Moving right...", Toast.LENGTH_SHORT).show()
        }
        binding.ccwbtn.setOnClickListener {
            sendJson("movement", "app", mapOf("direction" to "anticlockwise"))
            Toast.makeText(this, "Rotating CCW...", Toast.LENGTH_SHORT).show()
        }
        binding.cwbtn.setOnClickListener {
            sendJson("movement", "app", mapOf("direction" to "clockwise"))
            Toast.makeText(this, "Rotating CW...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createRequest(): Request {
        val websocketUrl =
            "wss://s14909.blr1.piesocket.com/v3/1?api_key=Hi4AGeft6p6ByGmqyS7Jb0ozwS3uJw5TzsBd7wtq&notify_self=1"
        return Request.Builder().url(websocketUrl).build()
    }

    private fun sendRegisterMessage(socket: WebSocket) {
        val registerJson = JSONObject()
        registerJson.put("type", "register")
        registerJson.put("client", "app")
        socket.send(registerJson.toString())
    }

    private fun sendJson(type: String, client: String, extra: Map<String, Any>) {
        val json = JSONObject()
        json.put("type", type)
        json.put("client", client)
        for ((key, value) in extra) {
            json.put(key, value)
        }
        webSocket?.send(json.toString())
    }
}
