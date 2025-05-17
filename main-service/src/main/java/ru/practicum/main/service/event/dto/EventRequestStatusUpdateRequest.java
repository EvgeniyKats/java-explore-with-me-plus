package ru.practicum.main.service.event.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;

    private Status status;

    public enum Status {
        CONFIRMED, REJECTED
    }
}
