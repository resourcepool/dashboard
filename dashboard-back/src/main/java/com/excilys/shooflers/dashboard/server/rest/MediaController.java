package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.dto.MediaMetadataDto;
import com.excilys.shooflers.dashboard.server.model.Revision;
import com.excilys.shooflers.dashboard.server.model.type.MediaType;
import com.excilys.shooflers.dashboard.server.property.DashboardProperties;
import com.excilys.shooflers.dashboard.server.rest.utils.FileHelper;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import com.excilys.shooflers.dashboard.server.service.BundleService;
import com.excilys.shooflers.dashboard.server.service.MediaService;
import com.excilys.shooflers.dashboard.server.service.RevisionService;
import com.excilys.shooflers.dashboard.server.service.exception.BundleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private RevisionService revisionService;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private DashboardProperties props;

    @Autowired
    private BundleService bundleService;

    @RequireValidApiKey
    @RequestMapping(value = "{uuidbundle}/{uuid}", method = RequestMethod.GET)
    public MediaMetadataDto get(@PathVariable("uuidbundle") String uuidBundle, @PathVariable("uuid") String uuid) {
        return mediaService.get(uuid, uuidBundle);
    }

    @RequireValidApiKey
    @RequestMapping(value = "{uuidbundle}", method = RequestMethod.GET)
    public List<MediaMetadataDto> getAllByBundle(@PathVariable("uuidbundle") String uuidBundle) {
        return mediaService.getByBundle(uuidBundle);
    }

    @RequireValidUser
    @RequestMapping(method = RequestMethod.POST)
    public MediaMetadataDto save(@RequestParam("media") String json, @RequestParam("file") MultipartFile multipartFile) {
        MediaMetadataDto media = mediaService.fromJson(json);

        // Save media
        MediaType mediaType = MediaType.getMediaType(media.getMediaType());

        // If bundle doesn't exist, abort
        if (bundleService.get(media.getUuidBundle()) == null) {
            throw new BundleNotFoundException("Bundle not found, media not added");
        }

        // Not expected a file to save, directly create media with url set
        if (mediaType == MediaType.WEB_SITE || mediaType == MediaType.WEB_VIDEO) {
            media = mediaService.save(media);
        } else {
            media.setUuid(UUID.randomUUID().toString());
            if (FileHelper.saveFile(multipartFile, media, props.getBaseUrl())) {
                media = mediaService.save(media);
            }
        }

        // Create a new revision
        media.setRevision(revisionService.add(Revision.Action.ADD, media.getUuid(), Revision.Type.MEDIA, null).getRevision());
        return media;
    }

    @RequireValidUser
    @RequestMapping(value = "{uuidbundle}/{uuid}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("uuidbundle") String uuidBundle, @PathVariable("uuid") String uuid) {
        if (mediaService.delete(uuid, uuidBundle)) {
            // Create a new revision
            revisionService.add(Revision.Action.DELETE, uuid, Revision.Type.MEDIA, null);
        }
    }
}
