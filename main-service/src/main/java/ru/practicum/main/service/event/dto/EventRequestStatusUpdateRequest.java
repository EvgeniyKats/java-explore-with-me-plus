package ru.practicum.main.service.event.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class EventRequestStatusUpdateRequest {
    private Set<Long> requestIds;

    private Status status;

    public enum Status {
        CONFIRMED, REJECTED
    }
}
