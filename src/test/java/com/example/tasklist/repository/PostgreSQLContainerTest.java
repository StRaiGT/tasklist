package com.example.tasklist.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PostgreSQLExtension.class)
public class PostgreSQLContainerTest {
    @Test
    void canStartPostgresDB() {
        assertThat(PostgreSQLExtension.postgreSQLContainer.isCreated()).isTrue();
        assertThat(PostgreSQLExtension.postgreSQLContainer.isRunning()).isTrue();
    }
}
