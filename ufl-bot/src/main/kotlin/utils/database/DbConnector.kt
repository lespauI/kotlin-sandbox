package kt.sandbox.utils.database

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

@Component
open class DbConnector @Autowired constructor() {
    /**
     * Connect to a sample database
     */
    var conn: Connection? = null
    val logger = LoggerFactory.getLogger(javaClass)

    fun connect() {

        try {
            val url = "jdbc:sqlite:ufl-bot/daddyBotDB.db"
            conn = DriverManager.getConnection(url)
            logger.info("Connection to SQLite has been established.")
        } catch (e: SQLException) {
            logger.error(e.message)
        }
    }

    fun close() {
        try {
            conn?.close()
        } catch (ex: SQLException) {
            logger.error(ex.message)
        }
    }

    fun getConnection(): Connection? {
        return conn
    }

}