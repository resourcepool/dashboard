package com.excilys.shooflers.dashboard.server.rest.utils;


import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {

    private static final String FOLDER_MEDIA = System.getProperty("user.dir") + "/public/";

    /**
     * Save a file.
     * @param file file to save
     * @return Result of the operation
     */
    public static String saveFile(MultipartFile file, String mediaUuid) {
        try {
            byte[] bytes = file.getBytes();
            String fileName = mediaUuid + MediaType.getExtension(file.getContentType());
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(FOLDER_MEDIA + fileName)));
            stream.write(bytes);
            stream.close();
            return fileName;
        } catch (IOException e) {
            return null;
        }
    }
}
