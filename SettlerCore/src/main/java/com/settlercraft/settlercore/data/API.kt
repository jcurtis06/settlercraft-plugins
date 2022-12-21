package com.settlercraft.settlercore.data

import com.settlercraft.settlercore.SettlerCore
import com.settlercraft.settlercore.settler.Settlers
import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import org.bukkit.Bukkit
import org.bukkit.Statistic
import org.json.simple.JSONObject
import java.net.InetSocketAddress
import java.util.UUID

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

        createEndpoint("/api/stats", "GET") { exchange ->
            val key = exchange.requestHeaders["Key"]?.first()

            if (key.equals(apiKey)) {
                println("Getting stats")
                println("Settlers: ${Settlers.settlers.size}")
                println("Num of stats: ${Statistic.values().size}")
                val stats = mutableMapOf<UUID, MutableMap<String, Int>>()

                for (settler in Settlers.settlers) {
                    val uuid = settler.uuid
                    val player = Bukkit.getOfflinePlayer(uuid)
                    // get the kill stat and add it to the map
                    val kills = player.getStatistic(Statistic.PLAYER_KILLS)
                    val deaths = player.getStatistic(Statistic.DEATHS)

                    stats[uuid] = mutableMapOf("kills" to kills, "deaths" to deaths)
                }

                println("Stats: ${JSONObject(stats).toJSONString()}")
                //xchange.sendResponseHeaders(200, 0)
                return@createEndpoint JSONObject(stats).toJSONString()
            } else {
                exchange.sendResponseHeaders(401, 0)
                return@createEndpoint "Invalid Key"
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
                exchange.sendResponseHeaders(405, -1)
            }
            println("reached end of createEndpoint")
            exchange.sendResponseHeaders(405, -1)
            exchange.close()
        }
    }
}