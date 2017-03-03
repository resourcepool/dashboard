package io.resourcepool.dashboard.rest;

import io.resourcepool.dashboard.dto.FeedDto;
import io.resourcepool.dashboard.dto.mapper.FeedDtoMapper;
import io.resourcepool.dashboard.exception.ResourceNotFoundException;
import io.resourcepool.dashboard.model.metadata.Feed;
import io.resourcepool.dashboard.security.annotation.RequireValidApiKey;
import io.resourcepool.dashboard.security.annotation.RequireValidUser;
import io.resourcepool.dashboard.service.FeedService;
import io.resourcepool.dashboard.validator.FeedDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@RestController
@RequireValidUser
@RequestMapping("/feed")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @Autowired
    private FeedDtoValidator validator;

    @Autowired
    private FeedDtoMapper mapper;

    /**
     * Get all Bundle.
     *
     * @return List of BundleDto
     */
    @RequestMapping(method = RequestMethod.GET)
    @RequireValidApiKey
    public List<FeedDto> getAll() {
        List<FeedDto> feeds = mapper.toListDto(feedService.getAll());
        return feeds;
    }

    /**
     * Save a new feed and add a new revision.
     *
     * @param feedDto feed to save
     */
    @RequestMapping(method = RequestMethod.POST)
    public FeedDto save(@RequestBody FeedDto feedDto) {
        validator.validate(feedDto);
        Feed feed = mapper.fromDto(feedDto);
        feedService.save(feed);
        return mapper.toDto(feed);
    }

    /**
     * Get a particular Feed by its id.
     *
     * @param uuid id to find
     * @return Feed found if feed exists
     */
    @RequestMapping(value = "{uuid}", method = RequestMethod.GET)
    @RequireValidApiKey
    public FeedDto get(@PathVariable("uuid") String uuid) {
        Feed result = feedService.get(uuid);
        if (result == null) {
            throw new ResourceNotFoundException("Feed not found");
        } else {
            return mapper.toDto(result);
        }
    }

    /**
     * Update a feed
     *
     * @param feedDto Feed to update
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public FeedDto update(@RequestBody FeedDto feedDto) {
        validator.validate(feedDto);

        if (feedDto.getUuid() == null) {
            throw new IllegalArgumentException("A valid uuid is required to edit a feed.");
        }
        Feed feed = mapper.fromDto(feedDto);

        feedService.update(feed);

        return mapper.toDto(feed);
    }

    /**
     * Delete a feed by its id.
     *
     * @param uuid id to delete
     */
    @RequestMapping(value = "{uuid}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("uuid") String uuid) {
        Feed result = feedService.get(uuid);
        if (result == null) {
            throw new ResourceNotFoundException("Bound not found");
        } else {
            feedService.delete(uuid);
        }

    }
}
