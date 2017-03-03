package com.excilys.shooflers.dashboard.server.dto.mapper;

import com.excilys.shooflers.dashboard.server.dao.BundleDao;
import com.excilys.shooflers.dashboard.server.dto.FeedDto;
import com.excilys.shooflers.dashboard.server.model.metadata.FeedMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * TODO class details.
 *
 * @author Loïc Ortola on 03/03/2017
 */
@Component
public class FeedDtoMapperImpl implements FeedDtoMapper {
  
  @Autowired
  private BundleDao bundleDao;
  
  @Override
  public FeedDto toDto(FeedMetaData model) {
    return FeedDto.builder()
      .uuid(model.getUuid())
      .name(model.getName())
      .bundleTags(model.getBundleTags())
      .build();
  }

  @Override
  public FeedMetaData fromDto(FeedDto dto) {
    return FeedMetaData.builder()
      .uuid(dto.getUuid())
      .name(dto.getName())
      .bundles(dto.getBundleTags())
      .build();
  }
}
