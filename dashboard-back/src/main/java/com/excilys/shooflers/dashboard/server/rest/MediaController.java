package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.dao.MediaDao;
import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.MediaDtoMapperImpl;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@RequireValidApiKey
@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private MediaDao mediaDao;

    @Autowired
    private MediaDtoMapperImpl mediaDtoMapper;

    @RequestMapping(method = RequestMethod.GET)
    public List<MediaMetadataDto> getAll() {
        return mediaDao.getAll().stream().map(mediaDtoMapper::toDto).collect(Collectors.toList());
    }

    @RequestMapping(value = "{uuid}", method = RequestMethod.GET)
    public MediaMetadataDto get(@RequestParam("uuid") String uuid) {
        return mediaDtoMapper.toDto(mediaDao.get(uuid));
    }
}
