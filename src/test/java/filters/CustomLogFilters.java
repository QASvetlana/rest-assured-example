package filters;

import io.qameta.allure.restassured.AllureRestAssured;

public class CustomLogFilters {

    private static final AllureRestAssured FILTER = new AllureRestAssured();

    private CustomLogFilters() {
    }

    public static CustomLogFilters customLogFilter() {
        return InitLogFilter.logFilter;
    }

    public AllureRestAssured withCustomTemplates() {
        FILTER.setRequestTemplate("request.ftl");
        FILTER.setResponseTemplate("response.ftl");
        return FILTER;

    }

    private static class InitLogFilter {
        private static final CustomLogFilters logFilter = new CustomLogFilters();
    }
}
