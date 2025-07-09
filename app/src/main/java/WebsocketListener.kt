package com.example.rovdemo

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WebsocketListener(private val viewModel: MainViewModel) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        viewModel.setStatus(true)
        webSocket.send("Device\nConnected!!")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        viewModel.setMessage(Pair(false, text))
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        viewModel.setStatus(false)
        webSocket.close(1000, null)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        viewModel.setStatus(false)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        viewModel.setStatus(false)
    }
}
