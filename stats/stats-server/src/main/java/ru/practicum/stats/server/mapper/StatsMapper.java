package ru.practicum.stats.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.server.model.EndpointHit;

@Mapper(componentModel = "spring")
public interface StatsMapper {

    @Mapping(target = "timestamp", expression = "java(source.getTimestamp().toLocalDateTime())")
    EndpointHit mapToModel(EndpointHitDto endpointHitDto);

    @Mapping(target = "timestamp", expression = "java(java.sql.Timestamp.valueOf(source.getTimestamp()))")
    EndpointHitDto mapToDto(EndpointHit endpointHit);

}
