package ru.practicum.stats.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class EndpointHitDto {

    @NotEmpty(message = "Поле app не может быть пустым")
    @Size(max = 100, message = "Размерность поля app не может превышать 100 символов")
    String app;
    @NotEmpty(message = "Поле uri не может быть пустым")
    @Size(max = 100, message = "Размерность поля uri не может превышать 100 символов")
    String uri;
    @NotEmpty(message = "Поле ip не может быть пустым")
    @Size(min = 7, max = 15, message = "Размерность поля ip не может быть меньше 7 и больше 15 символов")
    String ip;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Поле timestamp не может быть пустым")
    Timestamp timestamp;
}
