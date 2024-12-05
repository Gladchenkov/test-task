package com.example.demo.controller;

import com.example.demo.config.IntegrationTestConfiguration;
import com.example.demo.model.TimestampDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.example.demo.config.IntegrationTestConfiguration.POSTGRESQL_SERVER_CONTAINER;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.OK;

@ActiveProfiles("test")
@SpringBootTest(classes = IntegrationTestConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimestampControllerTest {

    @Autowired
    private DataSource dataSource;

    @BeforeAll
    public static void beforeAll(@Value("${local.server.port}") int port) {
        RestAssured.port = port;
    }

    @Test
    void testConnectionLoss() throws Exception {

        Thread.sleep(5000);

        List<TimestampDto> timestamps = Arrays.asList(given().contentType(JSON)
                .when()
                .queryParam("page", 0)
                .queryParam("pageSize", 100)
                .get("/timestamps")
                .then()
                .statusCode(equalTo(OK.value()))
                .extract().body()
                .as(TimestampDto[].class));

        // check order before connection loss
        checkTimeoutOrder(timestamps);

        // simulate connection loss
        pauseContainer();
        Thread.sleep(6000);
        unpauseContainer();

        List<TimestampDto> timestamps2 = Arrays.asList(given().contentType(JSON)
                .when()
                .queryParam("page", 0)
                .queryParam("pageSize", 100)
                .get("/timestamps")
                .then()
                .statusCode(equalTo(OK.value()))
                .extract().body()
                .as(TimestampDto[].class));

        // check order after connection loss
        checkTimeoutOrder(timestamps2);
        assertTrue(timestamps.size() < timestamps2.size());
    }

    private void checkTimeoutOrder(List<TimestampDto> timestamps) {
        List<LocalDateTime> responseOrder = timestamps.stream()
                .map(TimestampDto::getTimestamp)
                .toList();

        List<LocalDateTime> expectedAscOrder = responseOrder.stream()
                .sorted(LocalDateTime::compareTo)
                .toList();

        assertEquals(responseOrder, expectedAscOrder);
    }

    private void unpauseContainer() {
        POSTGRESQL_SERVER_CONTAINER.getDockerClient().unpauseContainerCmd(POSTGRESQL_SERVER_CONTAINER.getContainerId()).exec();
    }

    private void pauseContainer() {
        POSTGRESQL_SERVER_CONTAINER.getDockerClient().pauseContainerCmd(POSTGRESQL_SERVER_CONTAINER.getContainerId()).exec();
    }
}