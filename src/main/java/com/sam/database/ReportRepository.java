package com.sam.database;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ReportRepository {

    public static void saveResult(

            String executionId,
            String testName,
            String scenario,
            String status,
            String browser,
            String environment,
            long duration,
            String errorMessage) {

        String sql = """
                INSERT INTO test_execution
                (
                    execution_id,
                    test_name,
                    scenario_name,
                    status,
                    browser,
                    environment,
                    duration_ms,
                    error_message
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (

                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, executionId);
            statement.setString(2, testName);
            statement.setString(3, scenario);
            statement.setString(4, status);
            statement.setString(5, browser);
            statement.setString(6, environment);
            statement.setLong(7, duration);
            statement.setString(8, errorMessage);

            statement.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
