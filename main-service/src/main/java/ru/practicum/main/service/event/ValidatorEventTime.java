package ru.practicum.main.service.event;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorEventTime {
    /**
     * @param time - checked time
     * @param hours - count of hours after which the date will be good
     */
    public static boolean isEventTimeBad(LocalDateTime time, int hours) {
        return time.plusHours(hours).isBefore(LocalDateTime.now());
    }
}
