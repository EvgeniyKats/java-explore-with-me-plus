package ru.practicum.main.service.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.service.Constants;
import ru.practicum.main.service.comment.CommentRepository;
import ru.practicum.main.service.comment.MapperComment;
import ru.practicum.main.service.comment.dto.CommentDto;
import ru.practicum.main.service.comment.dto.GetCommentDto;
import ru.practicum.main.service.comment.enums.CommentSortType;
import ru.practicum.main.service.comment.model.Comment;
import ru.practicum.main.service.event.EventRepository;
import ru.practicum.main.service.event.model.Event;
import ru.practicum.main.service.exception.ConflictException;
import ru.practicum.main.service.exception.NotFoundException;
import ru.practicum.main.service.user.UserRepository;
import ru.practicum.main.service.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MapperComment commentMapper;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public GetCommentDto addNewComment(Long userId, Long eventId, CommentDto commentDto) {
        User commentAuthor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(Constants.USER_NOT_FOUND));
        Event commentEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Constants.EVENT_NOT_FOUND));
        Comment comment = commentMapper.toComment(commentDto);
        comment.setAuthor(commentAuthor);
        comment.setEvent(commentEvent);
        comment.setCreated(LocalDateTime.now());
        return commentMapper.toGetCommentDto(commentRepository.save(comment));
    }

    @Override
    public GetCommentDto updateComment(Long userId, Long eventId, Long commentId, CommentDto commentDto) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(Constants.USER_NOT_FOUND);
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(Constants.EVENT_NOT_FOUND);
        }
        Comment commentFromDb = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Constants.COMMENT_NOT_FOUND));
        if (commentFromDb.getCreated().isBefore(LocalDateTime.now().minusDays(1))) {
            throw new ConflictException("Комментарий может быть изменен только в первые 24 часа после создания");
        }
        commentFromDb.setText(commentDto.getText());
        return commentMapper.toGetCommentDto(commentRepository.save(commentFromDb));
    }

    @Override
    public void deleteCommentPrivate(Long userId, Long eventId, Long commentId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(Constants.USER_NOT_FOUND);
        }
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(Constants.EVENT_NOT_FOUND);
        }
        if (!commentRepository.existsById(eventId)) {
            throw new NotFoundException(Constants.COMMENT_NOT_FOUND);
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public void deleteCommentAdmin(Long eventId, Long commentId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(Constants.EVENT_NOT_FOUND);
        }
        if (!commentRepository.existsById(eventId)) {
            throw new NotFoundException(Constants.COMMENT_NOT_FOUND);
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public GetCommentDto getCommentById(Long eventId, Long commentId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(Constants.EVENT_NOT_FOUND);
        }
        Comment commentFromDb = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Constants.COMMENT_NOT_FOUND));
        return commentMapper.toGetCommentDto(commentFromDb);
    }

    @Override
    public List<GetCommentDto> getEventComments(Long eventId, Integer from, Integer size, CommentSortType sortType) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(Constants.EVENT_NOT_FOUND);
        }
        Sort sort = switch (sortType) {
            case COMMENTS_OLD -> Sort.by("created").ascending();
            case COMMENTS_NEW -> Sort.by("created").descending();
        };
        Pageable pageable = PageRequest.of(from, size, sort);
        List<Comment> comments = commentRepository.findByEventId(eventId, pageable);
        return comments.stream().map(commentMapper::toGetCommentDto).toList();
    }
}
