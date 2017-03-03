package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.dto.ChangesetWrapper;
import com.excilys.shooflers.dashboard.server.dto.mapper.FeedDtoMapper;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidApiKey;
import com.excilys.shooflers.dashboard.server.security.annotation.RequireValidUser;
import com.excilys.shooflers.dashboard.server.service.FeedService;
import com.excilys.shooflers.dashboard.server.service.impl.RevisionServiceImpl;
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