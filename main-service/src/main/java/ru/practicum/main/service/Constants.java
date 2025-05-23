package ru.practicum.main.service;

import java.time.LocalDateTime;

public interface Constants {
    String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    String DUPLICATE_USER = "Пользователь с таким Email уже существует";
    String USER_NOT_FOUND = "Пользователь не найден";
    String EVENT_NOT_FOUND = "Событие не найдено";
    String CATEGORY_NOT_FOUND = "Категория не найдена";
    LocalDateTime MIN_START_DATE = LocalDateTime.of(1970, 1, 1, 0, 0);
}
