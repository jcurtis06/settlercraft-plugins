package com.settlercraft.settlercore.data

import com.settlercraft.settlercore.SettlerCore
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object Database {
    private val host = SettlerCore.instance!!.config.getString("mysql.host")
    private val port = SettlerCore.instance!!.config.getInt("mysql.port")
    private val database = SettlerCore.instance!!.config.getString("mysql.database")
    private val username = SettlerCore.instance!!.config.getString("mysql.username")
    private val password = SettlerCore.instance!!.config.getString("mysql.password")

    var connection: Connection = DriverManager.getConnection("jdbc:mysql://$host:$port/$database?useSSL=false", username, password)

    private val isConnected: Boolean
        get() = !connection.isClosed

    fun connect() {
        if (!isConnected) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://$host:$port/$database?useSSL=false", username, password)
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    fun disconnect() {
        if (isConnected) {
            try {
                connection.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    fun getColWhere(table: String, col: String, where: String, whereVal: Any): Any? {
        connect()
        try {
            val statement = connection.createStatement()
            val result = statement.executeQuery("SELECT $col FROM $table WHERE $where = '$whereVal'")
            if (result.next()) {
                disconnect()
                return result.getObject(col)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        disconnect()
        return null
    }

    fun setColWhere(table: String, col: String, where: String, whereVal: Any, newVal: Any) {
        connect()
        try {
            val statement = connection.createStatement()
            statement.executeUpdate("UPDATE $table SET $col = $newVal WHERE $where = '$whereVal'")
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        disconnect()
    }

    fun insert(table: String, col: String, value: Any) {
        connect()
        try {
            val statement = connection.createStatement()
            statement.executeUpdate("INSERT INTO $table ($col) VALUES ('$value')")
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        disconnect()
    }

    fun doesExist(table: String, col: String, value: Any): Boolean {
        connect()
        try {
            val statement = connection.createStatement()
            println("SELECT * FROM $table WHERE $col = $value")
            val result = statement.executeQuery("SELECT * FROM $table WHERE $col='$value'")
            if (result.next()) {
                disconnect()
                return true
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        disconnect()
        return false
    }
}