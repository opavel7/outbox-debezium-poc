package com.example.outbox.config;


import io.debezium.connector.postgresql.PostgresConnector;
import io.debezium.connector.postgresql.PostgresConnectorConfig;
import io.debezium.embedded.EmbeddedEngine;
import org.apache.kafka.connect.storage.FileOffsetBackingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class DebeziumConnectorConfig {

    @Value("${customer.datasource.host}")
    private String customerDbHost;

    @Value("${customer.datasource.database}")
    private String customerDbName;

    @Value("${customer.datasource.port}")
    private String customerDbPort;

    @Value("${customer.datasource.username}")
    private String customerDbUsername;

    @Value("${customer.datasource.password}")
    private String customerDbPassword;

    @Bean
    public io.debezium.config.Configuration customerConnector() {
        return io.debezium.config.Configuration.empty().withSystemProperties(Function.identity()).edit()
                .with(EmbeddedEngine.CONNECTOR_CLASS, PostgresConnector.class)
                .with(EmbeddedEngine.ENGINE_NAME, "test")
                .with(EmbeddedEngine.OFFSET_STORAGE, FileOffsetBackingStore.class)
                .with(EmbeddedEngine.OFFSET_STORAGE_FILE_FILENAME,"/tmp/offsets.dat")
                .with(EmbeddedEngine.OFFSET_FLUSH_INTERVAL_MS, 60000)

                .with(PostgresConnectorConfig.HOSTNAME, customerDbHost)
                .with(PostgresConnectorConfig.DATABASE_NAME, customerDbName)
                .with(PostgresConnectorConfig.USER, customerDbUsername)
                .with(PostgresConnectorConfig.PASSWORD, customerDbPassword)
                .with(PostgresConnectorConfig.PORT, customerDbPort)
                .with(PostgresConnectorConfig.TOPIC_PREFIX,"test")

                //pgoutput - default plug-in provided by PostgreSQL
                .with(PostgresConnectorConfig.PLUGIN_NAME, "pgoutput")

                // Send JSON without schema
                .with("schemas.enable", false)
                .build();
    }
}
