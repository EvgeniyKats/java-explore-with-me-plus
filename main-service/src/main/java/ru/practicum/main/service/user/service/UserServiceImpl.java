package ru.practicum.main.service.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.main.service.user.dto.NewUserRequest;
import ru.practicum.main.service.user.dto.UserDto;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<UserDto> getUsers(GetUserParam getUserParam) {
        // TODO
        return List.of();
    }

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        // TODO
        UserDto dto = new UserDto();
        dto.setId(1L);
        return dto;
    }

    @Override
    public void deleteUser(Long userId) {
        // TODO
    }
}
