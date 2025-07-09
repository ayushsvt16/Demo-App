package com.example.rovdemo

import android.os.*
import android.view.MotionEvent
import android.view.View
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

class RemoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRemoteBinding
    private lateinit var websocketListener: WebsocketListener
    private lateinit var viewModel: MainViewModel
    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null
    private var isConnected = false

    private val handler = Handler(Looper.getMainLooper())
    private var repeatRunnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRemoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        websocketListener = WebsocketListener(viewModel, { socket ->
            webSocket = socket
            sendConnectRegisterMessage(socket)
        }, this)

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

        setContinuousTouch(binding.forwardbtn, "forward")
        setContinuousTouch(binding.backwardbtn, "backward")
        setContinuousTouch(binding.upbtn, "up")
        setContinuousTouch(binding.downbtn, "down")
        setContinuousTouch(binding.leftbtn, "left")
        setContinuousTouch(binding.rightbtn, "right")
        setContinuousTouch(binding.ccwbtn, "anticlockwise")
        setContinuousTouch(binding.cwbtn, "clockwise")
    }

    private fun setContinuousTouch(button: View, direction: String) {
        button.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (isConnected) {
                        repeatRunnable = object : Runnable {
                            override fun run() {
                                sendJson("movement", "app", mapOf("direction" to direction))
                                handler.postDelayed(this, 150)
                            }
                        }
                        handler.post(repeatRunnable!!)
                    } else {
                        Toast.makeText(this, "Connect first!", Toast.LENGTH_SHORT).show()
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    repeatRunnable?.let { handler.removeCallbacks(it) }
                }
            }
            true
        }
    }

    private fun createRequest(): Request {
        //val websocketUrl = "wss://s14909.blr1.piesocket.com/v3/1?api_key=Hi4AGeft6p6ByGmqyS7Jb0ozwS3uJw5TzsBd7wtq&notify_self=1"
        val websocketUrl = "ws://172.16.64.35:8000"
        return Request.Builder().url(websocketUrl).build()
    }

    private fun sendConnectRegisterMessage(socket: WebSocket) {
        val json = JSONObject()
        json.put("type", "register")
        json.put("client", "app")
        json.put("reason", "Opened Manually")
        socket.send(json.toString())
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
