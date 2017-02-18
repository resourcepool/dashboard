package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.exception.ResourceNotFoundException;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class allows to download media content from API.
 *
 * @author Loïc Ortola on 07/06/2016.
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private MediaService mediaService;

    /**
     * Get a media content
     *
     * @param filename the content file name
     * @return the media content if exists, 404 otherwise
     */
    @RequestMapping(value = "/{filename:.*}", method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource get(@PathVariable String filename) {
        Path result = mediaService.getContent(filename);
        if (result == null || !Files.exists(result)) {
            throw new ResourceNotFoundException();
        }
        // TODO adapt to Java NIO when is avaible
        return new FileSystemResource(result.toFile());
    }

}
