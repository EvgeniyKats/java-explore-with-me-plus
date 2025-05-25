package ru.practicum.main.service.comment.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.comment.dto.GetCommentDto;
import ru.practicum.main.service.comment.enums.CommentSortType;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/comments")
@Slf4j
public class PublicCommentController {

    @GetMapping("/{id}")
    public ResponseEntity<GetCommentDto> getComment(@PathVariable final String eventId,
                                                    @PathVariable final String id) {
        log.info("Получение комментария по id и eventId : {}, {}", id, eventId);
        GetCommentDto comment = new GetCommentDto();
        log.info("Отдан комментарий с телом {}", comment);
        return ResponseEntity.ok(comment);
        //todo: заменить на сервис
    }

    @GetMapping
    public ResponseEntity<List<GetCommentDto>> getComments(@PathVariable final String eventId,
                                                           @RequestParam(value = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                                           @RequestParam(value = "size", required = false, defaultValue = "10") @Min(1) Integer size,
                                                           @RequestParam(value = "sort", required = false, defaultValue = "COMMENTS_NEW")
                                                           CommentSortType sort) {
        log.info("Получение комментариев на мероприятие {}", eventId);
        List<GetCommentDto> comments = new ArrayList<>();
        log.info("Сформирован ответ с телом: {}", comments);
        return ResponseEntity.ok(comments);
        //todo: заменить на сервис
    }
}
