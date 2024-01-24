package ru.yandex.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.yandex.practicum.dto.StatisticFilterDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class StatisticClient extends BaseClient {
    @Autowired
    public StatisticClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl)).requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    public ResponseEntity<Object> create(StatisticRequestDto statisticRequestDto) {
        ResponseEntity<Object> response;
        try {
            response = post("/hit", statisticRequestDto);
        } catch (Exception e) {
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    public ResponseEntity<Object> getAllByFilter(StatisticFilterDto statisticFilterDto) {
        String encodedStart = URLEncoder.encode(statisticFilterDto.getStart(), StandardCharsets.UTF_8);
        String encodedEnd = URLEncoder.encode(statisticFilterDto.getEnd(), StandardCharsets.UTF_8);

        Map<String, Object> parameters = Map.of("start", encodedStart, "end", encodedEnd, "uris", String.join(",", statisticFilterDto.getUris()), "unique", statisticFilterDto.isUnique());

        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}
