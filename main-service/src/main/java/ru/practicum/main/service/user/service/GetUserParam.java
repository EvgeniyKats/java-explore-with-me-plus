package ru.practicum.main.service.user.service;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Builder
public class GetUserParam {
    private List<Long> ids;
    private Pageable pageable;
}
