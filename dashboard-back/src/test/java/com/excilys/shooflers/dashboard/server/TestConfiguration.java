package com.excilys.shooflers.dashboard.server;

import com.google.common.jimfs.Jimfs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.FileSystem;

/**
 * @author VIEGAS Mickael
 */
@Configuration
public class TestConfiguration {
    @Bean
    public FileSystem fileSystem() {
        return Jimfs.newFileSystem();
    }
}
