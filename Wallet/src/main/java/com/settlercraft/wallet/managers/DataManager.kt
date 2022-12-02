package com.settlercraft.wallet.managers

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource
import com.mysql.cj.jdbc.MysqlDataSource
import com.settlercraft.wallet.Wallet
import org.bukkit.entity.Player
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

/*
val host = main.config.getString("mysql.host")
        val port = main.config.getInt("mysql.port")
        val database = main.config.getString("mysql.database")
        val username = main.config.getString("mysql.username")
        val password = main.config.getString("mysql.password")
 */
object DataManager {
    private val dataSource: MysqlDataSource = MysqlConnectionPoolDataSource()
    private val main = Wallet.instance!!
    var connection: Connection? = null
    val isConnected: Boolean
        get() = connection != null

    fun connect() {
        val host = main.config.getString("mysql.host")
        val port = main.config.getInt("mysql.port")
        val database = main.config.getString("mysql.name")
        val username = main.config.getString("mysql.user")
        val password = main.config.getString("mysql.password")

        println("Connecting to database...")
        println("Host: $host")
        println("Port: $port")
        println("Database: $database")
        println("Username: $username")
        println("Password: $password")

        if (!isConnected) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://$host:$port/$database?useSSL=false", username, password)
                println("Connected to database!")
                makeTable()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    fun disconnect() {
        if (isConnected) {
            try {
                connection!!.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    fun getMoney(uuid: UUID): Double {
        if (isConnected) {
            try {
                val statement = connection!!.prepareStatement("SELECT * FROM wallet WHERE uuid = ?")
                statement.setString(1, uuid.toString())
                val results = statement.executeQuery()
                if (results.next()) {
                    return results.getDouble("balance")
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
        return 0.0
    }

    fun registerIfAbsent(uuid: UUID, money: Double) {
        if (isConnected) {
            try {
                if (isRegistered(uuid)) return
                val statement = connection!!.prepareStatement("INSERT INTO wallet (uuid, balance) VALUES (?, ?)")
                statement.setString(1, uuid.toString())
                statement.setDouble(2, money)
                statement.executeUpdate()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    private fun isRegistered(uuid: UUID): Boolean {
        if (isConnected) {
            try {
                val statement = connection!!.prepareStatement("SELECT * FROM wallet WHERE uuid = ?")
                statement.setString(1, uuid.toString())
                val results = statement.executeQuery()
                if (results.next()) {
                    return true
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
        return false
    }

    fun getColWhere(col: String, where: String, value: Any): Any? {
        if (isConnected) {
            try {
                val statement = connection!!.prepareStatement("SELECT * FROM wallet WHERE $where = ?")
                statement.setObject(1, value)
                val results = statement.executeQuery()
                if (results.next()) {
                    return results.getObject(col)
                }
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
        return null
    }

    fun setColWhere(col: String, where: String, value: Any, newValue: Any) {
        if (isConnected) {
            try {
                val statement = connection!!.prepareStatement("UPDATE wallet SET $col = ? WHERE $where = ?")
                statement.setObject(1, newValue)
                statement.setObject(2, value)
                statement.executeUpdate()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    private fun makeTable() {
        if (isConnected) {
            try {
                val statement = connection!!.createStatement()
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS wallet (uuid VARCHAR(255), balance DOUBLE)")
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }
}