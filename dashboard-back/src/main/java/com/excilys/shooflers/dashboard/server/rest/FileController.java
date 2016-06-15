package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.exception.ResourceNotFoundException;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

/**
 * This class allows to download media content from API.
 *
 * @author Loïc Ortola on 07/06/2016.
 */
@RestController
@RequireValidApiKey
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
    @RequireValidApiKey
    @ResponseBody
    public FileSystemResource get(@PathVariable String filename) {
        File result = mediaService.getContent(filename);
        if (result == null || !result.exists()) {
            throw new ResourceNotFoundException();
        }
        return new FileSystemResource(result);
    }

}
