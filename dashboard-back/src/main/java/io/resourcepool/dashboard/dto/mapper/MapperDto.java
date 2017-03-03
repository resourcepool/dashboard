package io.resourcepool.dashboard.dto.mapper;

import java.util.ArrayList;
import java.util.List;

public interface MapperDto<T, U> {

    U toDto(T model);
    T fromDto(U dto);

    default List<U> toListDto(List<T> model) {
        List<U> listDto = new ArrayList<>();
        model.stream().forEach(e -> listDto.add(toDto(e)));
        return listDto;
    }

    default List<T> fromListDto(List<U> listDto) {
        List<T> listModels = new ArrayList<>();
        listDto.stream().forEach(e -> listModels.add(fromDto(e)));
        return listModels;
    }
}
