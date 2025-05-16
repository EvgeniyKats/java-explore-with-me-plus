package ru.practicum.main.service.compilation.service;

import lombok.Builder;
import org.springframework.data.domain.Pageable;

@Builder
public class GetCompilationsParam {
    private Boolean pinned;
    private Pageable pageable;
}
