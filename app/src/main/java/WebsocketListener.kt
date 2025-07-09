package com.example.rovdemo

import android.util.Log
import android.widget.Toast
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebsocketListener(
    private val viewModel: MainViewModel,
    private val onOpenCallback: (WebSocket) -> Unit,
    private val activity: RemoteActivity
) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.d("WebSocket", "Connected to server")
        activity.runOnUiThread {
            Toast.makeText(activity, "Connected to WebSocket server", Toast.LENGTH_SHORT).show()
        }
        viewModel.setStatus(true)
        onOpenCallback(webSocket)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("WebSocket", "Received: $text")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d("WebSocket", "Closing: Code=$code, Reason=$reason")
        activity.runOnUiThread {
            Toast.makeText(activity, "WebSocket Closing: $reason", Toast.LENGTH_SHORT).show()
        }
        viewModel.setStatus(false)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d("WebSocket", "Closed: Code=$code, Reason=$reason")
        activity.runOnUiThread {
            Toast.makeText(activity, "WebSocket Closed: $reason", Toast.LENGTH_SHORT).show()
        }
        viewModel.setStatus(false)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("WebSocket", "Connection failed: ${t.message}", t)
        activity.runOnUiThread {
            Toast.makeText(activity, "WebSocket Error: ${t.message}", Toast.LENGTH_LONG).show()
        }
        viewModel.setStatus(false)
    }
}
