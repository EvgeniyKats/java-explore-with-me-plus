package ru.practicum.main.service.comment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.comment.dto.CommentDto;
import ru.practicum.main.service.comment.dto.GetCommentDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events/{eventId}")
@Validated
@Slf4j
public class PrivateCommentController {

    @PostMapping("/comment")
    public ResponseEntity<GetCommentDto> createComment(@PathVariable("userId") Long userId,
                                        @PathVariable("eventId") Long eventId,
                                        @RequestBody @Valid CommentDto commentDto) {
        //todo: заменить на сервис
        log.info("Create comment for user {} event {} with body {}", userId, eventId, commentDto);
        GetCommentDto comment = new GetCommentDto();
        log.info("Успешное создание комментария {}", comment);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<GetCommentDto> patchComment(@PathVariable("userId") Long userId,
                                                      @PathVariable("eventId") Long eventId,
                                                      @PathVariable("commentId") Long commentId,
                                                      @RequestBody @Valid CommentDto commentDto) {
        //todo: заменить на сервис
        log.info("Patch comment for user {} event {} with id {} and body {}", userId, eventId, commentId, commentDto);
        GetCommentDto comment = new GetCommentDto();
        log.info("Комментарий изменен {}", comment);
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("userId") Long userId,
                                          @PathVariable("eventId") Long eventId,
                                          @PathVariable("commentId") Long commentId) {
        log.info("Delete comment for user {} event {} with id {}", userId, eventId, commentId);
        // todo: вызов сервиса
        log.info("Комментарий с id {} успешно удален", commentId);
    }
}
