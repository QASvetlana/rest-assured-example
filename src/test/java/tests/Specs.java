package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static filters.CustomLogFilters.customLogFilter;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.is;

public class Specs {
    public static RequestSpecification request = with()
            .filter(customLogFilter().withCustomTemplates())
            .contentType("application/x-www-form-urlencoded; charset=UTF-8");


    public static ResponseSpecification response = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectBody("success", is(true))
            .expectBody("message", is("The product has been added to your <a href=\"/cart\">shopping cart</a>"))
            .build();
}
