package kt.sandbox.utils.database

import org.springframework.beans.factory.annotation.Autowired
import java.sql.Connection
import java.sql.ResultSet

class DbProcessor @Autowired constructor( val conn : Connection) {

    var stmt = null




}