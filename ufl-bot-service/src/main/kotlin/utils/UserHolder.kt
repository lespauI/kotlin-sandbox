package kt.sandbox.utils

import kt.sandbox.data.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.collections.HashMap


@Component
class UserHolder @Autowired constructor() {

    var counter = AtomicLong()

    var userList: HashMap<String, User>? = HashMap<String, User>()

    init {
        refreshUserList()
    }

    fun refreshUserList() {
        this.userList!!["testUser1"] = User("testUser1", "qwerty")
            .apply {
                this.age = 25
                this.bday = "1989-12-04"
                this.email = "qwer@qq.com"
                this.gender = "M"
                this.id = counter.incrementAndGet()
            }
        this.userList!!["testUser2"] = User("testUser2", "123")
            .apply {
                this.age = 12
                this.bday = "1989-11-04"
                this.email = "qwer@qq.uk"
                this.gender = "F"
                this.id = counter.incrementAndGet()
            }
        this.userList!!["testUser3"] = User("testUser3", "123")
            .apply {
                this.age = -12
                this.bday = "1989-04-12"
                this.email = "qwer@qq.uk"
                this.gender = "qwe"
                this.id = counter.incrementAndGet()
            }
    }

    fun addUser(user: User) {
        if (!userList!!.containsKey(user.username)) {
            this.userList!![user.username] = user.apply { this.id = counter.incrementAndGet() }

        }
        else throw Exception("User $user.username alrady exist")
    }

    @Throws(Exception::class)
    fun getUser(username: String): User {
        return Optional.ofNullable(userList?.get(username)).orElseThrow { Exception("User not exist") }
    }

    @Throws(Exception::class)
    fun getUser(id: Long): User {
        return userList!!
            .filter{ it.value.id == id }
            .values.first()

    }

}

