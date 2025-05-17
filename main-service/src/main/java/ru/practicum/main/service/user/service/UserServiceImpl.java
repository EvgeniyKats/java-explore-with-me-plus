package ru.practicum.main.service.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.Constants;
import ru.practicum.main.service.exception.DuplicateException;
import ru.practicum.main.service.exception.NotFoundException;
import ru.practicum.main.service.user.UserRepository;
import ru.practicum.main.service.user.dto.NewUserRequest;
import ru.practicum.main.service.user.dto.UserDto;
import ru.practicum.main.service.user.model.User;

import java.util.List;

import static ru.practicum.main.service.user.MapperUser.MAPPER_USER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(GetUserParam getUserParam) {
        Page<UserDto> users = userRepository.findUsersByIds(getUserParam.getIds(), getUserParam.getPageable());
        return users.getContent();
    }

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        if (userRepository.findByEmailIgnoreCase(newUserRequest.getEmail()).isPresent()) {
            throw new DuplicateException(Constants.DUPLICATE_USER);
        }

        User user = MAPPER_USER.toUser(newUserRequest);

        user = userRepository.save(user);

        return MAPPER_USER.toUserDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(Constants.USER_NOT_FOUND);
        }

        userRepository.deleteById(userId);
    }
}
