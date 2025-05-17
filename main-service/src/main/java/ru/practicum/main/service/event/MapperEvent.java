package ru.practicum.main.service.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import ru.practicum.main.service.event.dto.*;
import ru.practicum.main.service.event.enums.EventState;
import ru.practicum.main.service.event.model.Event;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapperEvent {
    @Mapping(source = "category", target = "category.id")
    Event toEvent(NewEventDto newEventDto);

    @Mapping(source = "category", target = "category.id")
    @Mapping(source = "stateAction", target = "state", qualifiedByName = "stateFromAdminAction")
    Event toEvent(UpdateEventAdminRequest updateEventAdminRequest);

    @Mapping(source = "category", target = "category.id")
    @Mapping(source = "stateAction", target = "state", qualifiedByName = "stateFromUserAction")
    Event toEvent(UpdateEventUserRequest updateEventUserRequest);

    EventShortDto toEventShortDto(Event event);

    EventFullDto toEventFullDto(Event event);

    @Named("stateFromAdminAction")
    default EventState stateFromAdminAction(UpdateEventAdminRequest.StateAction action) {
        if (action == null) {
            return null;
        }

        if (action == UpdateEventAdminRequest.StateAction.PUBLISH_EVENT) {
            return EventState.PUBLISHED;
        } else {
            return EventState.REJECTED;
        }
    }

    @Named("stateFromUserAction")
    default EventState stateFromUserAction(UpdateEventUserRequest.StateAction action) {
        if (action == null) {
            return null;
        }

        if (action == UpdateEventUserRequest.StateAction.SEND_TO_REVIEW) {
            return EventState.PENDING;
        } else {
            return EventState.CANCELED;
        }
    }
}
