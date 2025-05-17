package ru.practicum.main.service.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.main.service.request.dto.ParticipationRequestDto;
import ru.practicum.main.service.request.model.Request;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapperRequest {
    @Mapping(source = "event.id", target = "event")
    @Mapping(source = "requester.id", target = "requester")
    ParticipationRequestDto toParticipationRequestDto(Request request);
}
