package ru.practicum.stats.server.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatDto;
import ru.practicum.stats.dto.StatParamDto;
import ru.practicum.stats.server.service.StatsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService statService;

    @GetMapping("/stats")
    public ResponseEntity<List<StatDto>> getStats(@Valid @RequestBody StatParamDto statParamDto) {
        log.info("Пришел запрос на сервер статистики GET /stats");
        List<StatDto> stats = statService.getStats(statParamDto);
        log.info("Статистика собрана. GET /stats отработал без ошибок");
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    @PostMapping("/hit")
    public ResponseEntity<String> hitStat(@Valid @RequestBody HitDto hitDto) {
        log.info("Пришел запрос на сервис статистики POST /hit");
        statService.saveHit(hitDto);
        log.info("Информация сохранена. POST /hit отработал без ошибок");
        return new ResponseEntity<>("Информация сохранена", HttpStatus.CREATED);
    }

}
