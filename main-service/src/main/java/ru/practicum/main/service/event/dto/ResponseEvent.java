package ru.practicum.main.service.event.dto;

public interface ResponseEvent {
    void setConfirmedRequests(int confirmedRequests);

    int getConfirmedRequests();

    void setViews(long views);

    long getViews();
}
