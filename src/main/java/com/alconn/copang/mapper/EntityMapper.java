package com.alconn.copang.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


public interface EntityMapper <D, E> {

    E toEntityClass(D dto, Class<E> entity);

    E toEntity(final D dto);

    D toDto(final E entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto( D dto,@MappingTarget E entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromEntity(E entity, @MappingTarget D dto);
}
