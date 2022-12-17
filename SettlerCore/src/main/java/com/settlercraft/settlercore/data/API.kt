package com.settlercraft.settlercore.data

import com.settlercraft.settlercore.SettlerCore
import com.settlercraft.settlercore.settler.Settlers
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import org.bukkit.Bukkit
import java.net.InetSocketAddress

object API {
    private val apiKey = SettlerCore.instance!!.config.getString("rest.key")
    private val port = SettlerCore.instance!!.config.getInt("rest.port")

    private var server: HttpServer? = null

    fun start() {
        server = HttpServer.create(InetSocketAddress(port), 0)

        createEndpoint("/api/update", "POST") { exchange ->
            val key = exchange.requestHeaders["Key"]?.first()
            val player = exchange.requestHeaders["Player"]?.first()

            if (key.equals(apiKey) && player != null) {
                println("Updating player: $player")

                Settlers.reloadSettler(player)

                exchange.sendResponseHeaders(200, 0)
                return@createEndpoint "Success"
            } else {
                exchange.sendResponseHeaders(401, 0)
                return@createEndpoint "Error"
            }
        }

        server!!.executor = null
        server!!.start()
        Bukkit.getLogger().info("API started on port $port with key $apiKey")
    }

    fun stop() {
        server!!.stop(0)
    }

    private fun createEndpoint(path: String, method: String, handler: (exchange: HttpExchange) -> String) {
        server!!.createContext(path) { exchange ->
            println("Request: ${exchange.requestMethod} ${exchange.requestURI}")
            if (method == exchange.requestMethod) {
                val response = handler(exchange)
                exchange.sendResponseHeaders(200, response.length.toLong())
                val os = exchange.responseBody
                os.write(response.toByteArray())
                os.close()
            } else {
                println("reached else")
                exchange.sendResponseHeaders(405, -1) // 405 Method Not Allowed
            }
            println("reached end of createEndpoint")
            exchange.sendResponseHeaders(405, -1) // idek what this does lol
            exchange.close()
        }
    }
}