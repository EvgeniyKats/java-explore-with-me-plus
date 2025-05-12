package client;

import exception.NullBodyException;
import exception.RequestException;
import ru.practicum.stats.dto.HitDto;
import ru.practicum.stats.dto.StatDto;
import ru.practicum.stats.dto.StatParamDto;

import java.util.List;

public interface StatsClient {
    /**
     * @param hitDto             - тело запроса
     * @throws RequestException  - генерируется, если статус ответа 4xx/5xx
     */
    void createHit(HitDto hitDto) throws RequestException;

    /**
     * @param statParamDto       - данные для формирования запроса
     * @throws RequestException  - генерируется, если статус ответа 4xx/5xx
     * @throws NullBodyException - генерируется, если тело ответа пустое
     */
    List<StatDto> getStat(StatParamDto statParamDto) throws RequestException, NullBodyException;
}
