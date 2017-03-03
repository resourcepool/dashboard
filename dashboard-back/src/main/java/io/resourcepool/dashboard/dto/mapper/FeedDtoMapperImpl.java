package io.resourcepool.dashboard.dto.mapper;

import io.resourcepool.dashboard.dao.BundleDao;
import io.resourcepool.dashboard.dto.FeedDto;
import io.resourcepool.dashboard.model.metadata.Feed;
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
  public FeedDto toDto(Feed model) {
    if (model == null) {
      return null;
    }
    return FeedDto.builder()
      .uuid(model.getUuid())
      .name(model.getName())
      .bundleTags(model.getBundleTags())
      .build();
  }

  @Override
  public Feed fromDto(FeedDto dto) {
    if (dto == null) {
      return null;
    }
    return Feed.builder()
      .uuid(dto.getUuid())
      .name(dto.getName())
      .bundles(dto.getBundleTags())
      .build();
  }
}
