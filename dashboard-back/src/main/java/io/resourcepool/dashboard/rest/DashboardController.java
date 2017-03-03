package io.resourcepool.dashboard.rest;

import io.resourcepool.dashboard.dto.ChangesetWrapper;
import io.resourcepool.dashboard.dto.mapper.FeedDtoMapper;
import io.resourcepool.dashboard.security.annotation.RequireValidApiKey;
import io.resourcepool.dashboard.security.annotation.RequireValidUser;
import io.resourcepool.dashboard.service.FeedService;
import io.resourcepool.dashboard.service.impl.RevisionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequireValidUser
@RequireValidApiKey
@RequestMapping(value = "/revision")
public class DashboardController {

  @Autowired
  private RevisionServiceImpl revisionService;

  @Autowired
  private FeedService feedService;

  @Autowired
  private FeedDtoMapper feedDtoMapper;

  /**
   * Get the number of the latest revision
   *
   * @return long representing latest revision
   */
  @RequestMapping(method = RequestMethod.GET)
  public long getDiffs() {
    return revisionService.getLatest();
  }

  @RequestMapping(value = "/{revision}/feed/{feedId}", method = RequestMethod.GET)
  public ChangesetWrapper getDiffs(@PathVariable("revision") long revision, @PathVariable("feedId") String feedId) {
    return new ChangesetWrapper(feedDtoMapper.toDto(feedService.get(feedId)), revisionService.getDiffs(revision));
  }

}