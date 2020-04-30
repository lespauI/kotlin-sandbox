package kt.sandbox.data

import java.text.SimpleDateFormat
import java.util.regex.Pattern
import javax.validation.constraints.Email

data class User(val username: String, val password: String) {

    var id : Long? = null

    constructor(
        username: String, password: String, email: String, gender: String, bday: String, age: Int
    ) : this(username, password) {
        this.age = age
        this.email = email
        this.gender = gender
        this.bday = bday
    }

    @Email
    var email: String? = null
        set(value) {
            field = if (value!!.isEmailValid()) value else throw IllegalArgumentException("$value email is incorrect")
        }


    var gender: String? = null
        set(gender) {
            when (gender) {
                "M", "F" -> field = gender
                else -> field = "unknown"
            }
        }

    var bday: String? = null
        set(bday) {
            val format = SimpleDateFormat("yyyy-MM-dd")
            field = format.parse(bday).toString()
        }

    var age: Int = 0
        set(age) {
            when (age) {
                in 0..150 -> field = age
            }
        }

    private fun String.isEmailValid(): Boolean {
        val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,8}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(this)
        return matcher.matches()
    }
}