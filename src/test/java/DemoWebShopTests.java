import com.codeborne.selenide.Condition;
import io.restassured.response.Response;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.get;


public class DemoWebShopTests {


    // test for a new user, always 1 item in the cart
    @Test
    void addToCartTest() {
        Response response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("product_attribute_16_5_4=14&product_attribute_16_6_5=15&" +
                                "product_attribute_16_3_6=19&product_attribute_16_4_7=44&" +
                                "product_attribute_16_8_8=22&addtocart_16.EnteredQuantity=1")
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/details/16/1")
                        .then()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                        .body("updatetopcartsectionhtml", is("(1)"))
                        .extract().response();

        System.out.println("Response: " + response.path("updatetopcartsectionhtml"));


    }

    // with cookie

    @Test
    void addToCartWithCookieTest() {
        Response response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("product_attribute_16_5_4=14&product_attribute_16_6_5=15&" +
                                "product_attribute_16_3_6=19&product_attribute_16_4_7=44&" +
                                "product_attribute_16_8_8=22&addtocart_16.EnteredQuantity=1")
                        .cookie("Nop.customer=4501cf08-78ac-4d9b-8381-79dfdb3f85a9;")
                        .when()
                        .post("/addproducttocart/details/74/1")
                        .then()
                        .statusCode(200)
                        .body("success", is(true))
                        .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                        .body("updatetopcartsectionhtml", is("(1)"))
                        // get response
                        .extract().response();

        System.out.println("Response: " + response.path("updatetopcartsectionhtml"));


    }

    // API and UI

    @Test
    void addProductToCartWithCookieAPIandUITest() {
        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_74_5_26=82" +
                        "&product_attribute_74_6_27=85" +
                        "&product_attribute_74_3_28=87" +
                        "&product_attribute_74_8_29=88" +
                        "&product_attribute_74_8_29=89" +
                        "&product_attribute_74_8_29=90" +
                        "&addtocart_74.EnteredQuantity=2")
                .cookie("Nop.customer=69589107-6373-41bd-891d-47fb44277adc;")
                .when()
                .post("http://demowebshop.tricentis.com/addproducttocart/details/31/1")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
                .extract().response();
// Add cookie to browser
        open("http://demowebshop.tricentis.com/Themes/DefaultClean/Content/images/logo.png");
        getWebDriver().manage().addCookie(
                new Cookie("Nop.customer", "69589107-6373-41bd-891d-47fb44277adc"));
        step("Check that item has been added to Shopping Cart by UI", () -> {
            open("http://demowebshop.tricentis.com/cart");
            $(".cart-item-row").shouldBe(Condition.visible);
            $(".product-name").shouldHave(text("Build your own expensive computer"));
        });
    }


    @Test
    @Tag("demowebshop")
    @DisplayName("Successful registration to demowebshop (API + UI)")
    void registration() {
        step("Register user by API", () -> {
            given()
                    .contentType("application/x-www-form-urlencoded")
                    .body("__RequestVerificationToken=EOKFnxmSoeEnFm-" +
                            "ghPa0CZir4ETJXEXwMfTdzZIgoS55Tm0oKwyiDESnXdIbi-6DgkSCWGC_" +
                            "HPyc5ka4gNvFApBjYYmWJrNVgUYFgl66VzA1&Gender=" +
                            "F&FirstName=Sveta&LastName=Andreeva&Email=sve7987%40yandex." +
                            "ru&Password=123456&ConfirmPassword=123456&register-button=Register")
                    .when()
                    .post("http://demowebshop.tricentis.com/register")
                    .then()
                    .statusCode(302);
            get("http://demowebshop.tricentis.com/registerresult/1")
                    .then()
                    .statusCode(200);
        });

        step("Check that registration successed by UI", () -> {
            open("http://demowebshop.tricentis.com/registerresult/1");
            $(".result").shouldHave(Condition.text("Your registration completed"));
        });
    }


}


