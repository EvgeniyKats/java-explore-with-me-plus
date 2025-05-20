package client;

import exception.RequestException;
import exception.NullBodyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static util.StatsServerPaths.PATH_BASE;
import static util.StatsServerPaths.PATH_HIT;
import static util.StatsServerPaths.PATH_STAT;

@Slf4j
@Component
public class StatsClientRest implements StatsClient {
    private final RestClient restClient;

    public StatsClientRest(@Value("${stats.url:" + PATH_BASE + "}") String statsServerUrl) {
        log.info("stats.url = {}", statsServerUrl);
        restClient = RestClient.builder()
                .baseUrl(statsServerUrl)
                .build();
    }

    @Override
    public void createHit(EndpointHitDto hitDto) throws RequestException {
        log.info("Отправка hit app = {}, uri = {}, ip = {}, created = {}",
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                hitDto.getCreated());

        log.trace("Отправка запроса создания hit");
        ResponseEntity<Void> response = restClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH_HIT)
                        .build())
                .body(hitDto)
                .contentType(APPLICATION_JSON)
                .retrieve()
                .onStatus(httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                        (req, resp) -> {
                            log.trace("Произошла ошибка при отправке Hit");
                            throw new RequestException(req.getMethod().name(),
                                    req.getURI().toString(),
                                    resp.getStatusCode().value(),
                                    convertBody(resp.getBody()));
                        })
                .toBodilessEntity();
        log.trace("Ответ при создании hit получен без ошибок");

        log.info("Hit был успешно отправлен на сервер, response code: {}", response.getStatusCode());
    }

    @Override
    public List<ViewStatsDto> getStat(StatParam statParam) throws RequestException {
        log.info("Получение статистики с параметрами: start = {}, end = {}, uris = {}, unique = {}",
                statParam.getStart(),
                statParam.getEnd(),
                statParam.getUris(),
                statParam.getUnique());

        log.trace("Отправка запроса на получение статистики");
        List<ViewStatsDto> response = restClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path(PATH_STAT)
                            .queryParam("start", statParam.getStart())
                            .queryParam("end", statParam.getEnd());

                    if (statParam.getUris() != null) {
                        uriBuilder.queryParam("uris", statParam.getUris());
                    }

                    if (statParam.getUnique() != null) {
                        uriBuilder.queryParam("unique", statParam.getUnique());
                    }

                    return uriBuilder.build();
                })
                .accept(APPLICATION_JSON)
                .retrieve()
                .onStatus(code -> code.is4xxClientError() || code.is5xxServerError(),
                        (req, resp) -> {
                            log.trace("Произошла ошибка при получении статистики");
                            throw new RequestException(req.getMethod().name(),
                                    req.getURI().toString(),
                                    resp.getStatusCode().value(),
                                    convertBody(resp.getBody()));
                        })
                .body(new ParameterizedTypeReference<>() {
                });
        log.trace("Ответ при запросе статистике получен без ошибок");

        log.debug("response == null ? {}", response == null);
        if (response == null) {
            throw new NullBodyException();
        }

        log.info("Статистика была успешно получена, count = {}", response.size());
        return response;
    }

    private String convertBody(InputStream inputStream) {
        try (inputStream) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            String msg = "Не удалось конвертировать тело по причине: " + e.getMessage()
                         + " trace: " + Arrays.toString(e.getStackTrace());
            log.warn(msg);
            return msg;
        }
    }
}
