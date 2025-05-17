package ru.practicum.main.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.main.service.user.dto.UserDto;
import ru.practicum.main.service.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

    @Query("SELECT new ru.practicum.main.service.user.dto.UserDto(u.email, u.id, u.name)" +
            " FROM User u WHERE (:ids is NULL OR u.id IN :ids)")
    Page<UserDto> findUsersByIds(List<Long> ids, Pageable pageable);

    Optional<User> findByEmailIgnoreCase(String email);
}
