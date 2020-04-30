package data

import kt.sandbox.data.User
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.*
import javax.validation.constraints.Email
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class UserTest {
    var user: User? = null;

    @BeforeEach
    fun setUp() {
        user = User("test", "test_pass")
    }

    @AfterEach
    fun tearDown() {
        user = null
    }

    @Test
    fun getEmail() {
        assertNull(user!!.email)
    }

    @Test
    fun setEmail() {
        val email = "zzz@zqqq.co"
        user!!.email = email
        assertEquals(email, user!!.email.toString())
    }

    @Tag("Negative")
    @ParameterizedTest
    @ValueSource(strings = ["qqq@qq", "qwerty.cpm", "qwe@qwe.d", "@asd.com", "-1"])
    fun `Email with wrong format should throw IllegalArgumentException`(email: String) {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            user!!.email = email
        }
    }


    @Test
    fun getGender() {
    }

    @Test
    fun setGender() {
    }

    @Test
    fun getBday() {
    }

    @Test
    fun setBday() {
    }

    @Test
    fun getAge() {
        assertTrue(user?.age == 0);
    }

    @ParameterizedTest
    @ValueSource(ints = intArrayOf(1, 5, 15, 0, -1))
    fun setAge(age: Int) {
        user?.age = age
        if (age > 0)
            assertTrue(user?.age == age)
        else assertTrue(user?.age == 0)
    }

    @Test
    fun getId() {
        user!!.id = 1
        assertEquals( 1, user?.id)
    }

    @Test
    fun getUsername() {
    }

    @Test
    fun getPassword() {
    }

    @Test
    operator fun component1() {
    }

    @Test
    operator fun component2() {
    }

    @Test
    operator fun component3() {
    }

    @Test
    fun copy() {
    }
}