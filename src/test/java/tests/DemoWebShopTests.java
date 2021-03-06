package tests;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static filters.CustomLogFilters.customLogFilter;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static tests.Specs.request;
import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.get;
import static tests.Specs.response;


public class DemoWebShopTests {


    // test for a new user, always 1 item in the cart
    @Test
    void addToCartTest() {
                given()
                        .spec(request)
                        .body("product_attribute_16_5_4=14&product_attribute_16_6_5=15&" +
                                "product_attribute_16_3_6=19&product_attribute_16_4_7=44&" +
                                "product_attribute_16_8_8=22&addtocart_16.EnteredQuantity=1")
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/details/16/1")
                        .then()
                        .spec(response)
                        .body("updatetopcartsectionhtml", is("(1)"));



    }


    @Test
    void addToCartWithCookieTest() {

                given()
                        .spec(request)
                        .body("product_attribute_72_5_18=53&product_attribute_72_6_19=54&product_attribute_72_3_20=57&addtocart_72.EnteredQuantity=1")
                        .cookie("Nop.customer=957e0607-7b6f-4e93-8308-32ac30a6677c;")
                        .when()
                        .post("http://demowebshop.tricentis.com/addproducttocart/details/72/1")
                        .then()
                        .statusCode(200)
                        .spec(response);
                        //                      .body("updatetopcartsectionhtml", is("(1)"))

    }
    // API and UI

    @Test
    void addProductToCartWithCookieAPIandUITest() {
        given()
                .spec(request)
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
                .spec(response);
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
                    .spec(request)
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