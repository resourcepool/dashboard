package com.excilys.shooflers.dashboard.server.dao.util;

import com.excilys.shooflers.dashboard.server.model.Revision;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public class YamlUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(YamlUtils.class);
  
  public static void store(Object o, File dest) {
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    FileOutputStream fos = null;
    try {
      if (!dest.exists()) {
        dest.createNewFile();
      }
      fos = new FileOutputStream(dest);
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
  
  public static <T> T read(File dataFile, Class<T> clazz) {
    try {
      ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
      InputStream data = new FileInputStream(dataFile);
      T o = mapper.readValue(data, clazz);
      return o;
    } catch (IOException e) {
      LOGGER.warn("Error while retrieving file", e);
      throw new IllegalStateException(e);
    }
  }
}
