package com.excilys.shooflers.dashboard.server.rest;

import com.excilys.shooflers.dashboard.server.dao.BundleDao;
import com.excilys.shooflers.dashboard.server.dto.BundleMetadataDto;
import com.excilys.shooflers.dashboard.server.dto.mapper.BundleMapperImpl;
import com.excilys.shooflers.dashboard.server.model.metadata.BundleMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
@RestController
@RequestMapping("/bundle")
public class BundleController {
  @Autowired
  private BundleDao bundleDao;
  
  @Autowired
  private BundleMapperImpl mapper;
  
  @RequestMapping(method = RequestMethod.GET)
  public List<BundleMetadata> getAll() {
    return bundleDao.getAll();
  }
  
  @RequestMapping(method = RequestMethod.POST)
  public void save(@RequestBody BundleMetadataDto bundle) {
    bundleDao.save(mapper.fromDto(bundle));
  }
}
