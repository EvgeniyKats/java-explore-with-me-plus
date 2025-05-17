package client;

import exception.NullBodyException;
import exception.RequestException;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.util.List;

public interface StatsClient {
    /**
     * @param hitDto             - тело запроса
     * @throws RequestException  - генерируется, если статус ответа 4xx/5xx
     */
    void createHit(EndpointHitDto hitDto) throws RequestException;

    /**
     * @param statParam       - данные для формирования запроса
     * @throws RequestException  - генерируется, если статус ответа 4xx/5xx
     * @throws NullBodyException - генерируется, если тело ответа пустое
     */
    List<ViewStatsDto> getStat(StatParam statParam) throws RequestException, NullBodyException;
}
