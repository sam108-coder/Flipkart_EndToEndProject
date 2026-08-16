CREATE DATABASE IF NOT EXISTS automation_db;

USE automation_db;

CREATE TABLE IF NOT EXISTS test_execution (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    execution_id    VARCHAR(50)  NOT NULL,
    test_name       VARCHAR(255) NOT NULL,
    scenario_name   VARCHAR(255) NOT NULL,
    status          VARCHAR(10)  NOT NULL,
    browser         VARCHAR(20)  NOT NULL,
    environment     VARCHAR(10)  NOT NULL,
    duration_ms     BIGINT       NOT NULL,
    error_message   TEXT         NULL,
    executed_at     DATETIME     DEFAULT CURRENT_TIMESTAMP
);
