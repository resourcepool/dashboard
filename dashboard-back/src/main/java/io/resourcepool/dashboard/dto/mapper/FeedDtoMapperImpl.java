package io.resourcepool.dashboard.dto.mapper;

import io.resourcepool.dashboard.dao.BundleDao;
import io.resourcepool.dashboard.dto.FeedDto;
import io.resourcepool.dashboard.model.metadata.FeedMetaData;
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
