package data.bot

import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import org.springframework.stereotype.Component

@Component
class AdminController {

       fun serviceStatus(): String {
           val requestSpecBuilder = RequestSpecBuilder()
           requestSpecBuilder.setBaseUri("http://localhost")
           requestSpecBuilder.setPort(8080)
           return try {
               given().spec(requestSpecBuilder.build()).get("/admin/healthcheck").then().statusCode(200).extract().response().asString()
           } catch (ae: AssertionError) {
               "Service respond with error " + ae.message
           } catch (e : Exception) {
               "Service not respond with " + e.message
           }
    }
}