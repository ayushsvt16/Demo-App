package com.example.rovdemo

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebsocketListener(
    private val viewModel: MainViewModel,
    private val onOpenCallback: (WebSocket) -> Unit
) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        viewModel.setStatus(true)
        onOpenCallback(webSocket)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        // If you want to see messages in logs:
        // Log.d("WebSocket", "Received: $text")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        viewModel.setStatus(false)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        viewModel.setStatus(false)
        // Optionally log: t.message
    }
}
