package com.excilys.shooflers.dashboard.server.rest.utils;


import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import com.excilys.shooflers.dashboard.server.service.exception.UploadingFileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {

    // Path where media file are stored
    private static final String FOLDER_MEDIA = System.getProperty("user.dir") + "/public";

    /**
     * Save a file.
     * @param file file to save
     * @return Result of the operation
     */
    public static boolean saveFile(MultipartFile file, MediaMetadataDto mediaMetadataDto, String baseUrl) {
        // If extension are invalid, abort
        if (MediaType.getMediaType(file.getContentType()) == MediaType.NONE) {
            throw new UploadingFileException("Unrecognized extension");
        }

        try {
            byte[] bytes = file.getBytes();

            // File name for the media : uuid + extension
            String fileName = mediaMetadataDto.getUuid() + MediaType.getExtension(file.getContentType());

            // File is stored in media folder. Media contains bundle folder
            String dirBundleDest = FOLDER_MEDIA + "/" + mediaMetadataDto.getUuidBundle() + "/" + fileName;
            File dirBundle = new File(dirBundleDest);
            dirBundle.getParentFile().mkdirs();

            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(dirBundle));
            stream.write(bytes);
            stream.close();

            // Url to access the file
            mediaMetadataDto.setUrl("/" + mediaMetadataDto.getUuidBundle() + "/" + fileName);
            mediaMetadataDto.setMediaType(file.getContentType());
            return true;
        } catch (IOException e) {
            throw new UploadingFileException("Error uploading file");
        }
    }
}
