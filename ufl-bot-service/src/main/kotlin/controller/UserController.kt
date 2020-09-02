package kt.sandbox.controller

import kt.sandbox.data.User
import kt.sandbox.utils.UserHolder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController {

    @Autowired
    lateinit var userHolder: UserHolder

    @GetMapping("user/{username}.json")
    fun getUserByName(@PathVariable("username") username: String): ResponseEntity<*> {
        try {
            val user = userHolder!!.getUser(username)
            return ResponseEntity(user, HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity(
                "{ \n\"message\":\"User with username $username not exist in the system\" \n}",
                HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("user/{id}")
    fun getUserById(@PathVariable("id") id: Long): ResponseEntity<*> {
        try {
            val user = userHolder!!.getUser(id)
            return ResponseEntity(user, HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity(
                "{ \n\"message\":\"User with id $id not exist in the system\" \n}",
                HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("users")
    fun getUsers(): ResponseEntity<*> {
        return ResponseEntity(userHolder, HttpStatus.OK)

    }

    @PostMapping("userRefresh")
    fun refreshUsers(): ResponseEntity<*> {
        userHolder!!.refreshUserList()
        return ResponseEntity("{\n\"message\":\"User refresh successfully\" \n}", HttpStatus.NO_CONTENT)
    }

    @PutMapping("user")
    fun insertUser(@RequestBody user: User): ResponseEntity<*> {
        try {
            userHolder!!.addUser(user)
            return ResponseEntity("{\n \"message\":\"User add successfully\" \n}", HttpStatus.OK)
        } catch (e: Exception) {
            return ResponseEntity("{ \n \"error\": \"$e\"\n }", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}