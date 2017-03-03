package io.resourcepool.dashboard.dao.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public class YamlUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(YamlUtils.class);

    public static void store(Object o, Path dest) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        OutputStream fos = null;
        try {
            if (!Files.exists(dest)) {
                Files.createDirectories(dest.getParent());
                Files.createFile(dest);
            }
            fos = Files.newOutputStream(dest);
            mapper.writeValue(fos, o);
        } catch (IOException e) {
            LOGGER.warn("Error while storing file", e);
            throw new IllegalStateException(e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOGGER.warn("Error while closing file", e);
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    public static <T> T read(Path dataFile, Class<T> clazz) {
        try {
            if (!Files.exists(dataFile)) {
                return null;
            }
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            InputStream data = Files.newInputStream(dataFile);
            return mapper.readValue(data, clazz);
        } catch (IOException e) {
            LOGGER.warn("Error while retrieving file", e);
            throw new IllegalStateException(e);
        }
    }

    public static boolean delete(Path dataFile) {
        try {
            Files.delete(dataFile);
            return true;
        } catch (IOException e) {
            LOGGER.error("exception in YamlUtils#delete", e);
            return false;
        }
    }
}