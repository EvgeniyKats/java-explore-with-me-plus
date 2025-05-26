package ru.practicum.main.service.comment.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.main.service.comment.dto.GetCommentDto;
import ru.practicum.main.service.comment.enums.CommentSortType;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/comments")
@Slf4j
public class PublicCommentController {

    @GetMapping("/{id}")
    public ResponseEntity<GetCommentDto> getComment(@PathVariable @Positive final String eventId,
                                                    @PathVariable @Positive final String id) {
        log.info("Получение комментария по id и eventId : {}, {}", id, eventId);
        GetCommentDto comment = new GetCommentDto();
        log.info("Отдан комментарий с телом {}", comment);
        return ResponseEntity.ok(comment);
        //todo: заменить на сервис
    }

    @GetMapping
    public ResponseEntity<List<GetCommentDto>> getComments(@PathVariable @Positive final String eventId,
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
