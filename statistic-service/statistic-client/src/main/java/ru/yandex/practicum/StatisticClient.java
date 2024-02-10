package ru.yandex.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.yandex.practicum.dto.StatisticFilterDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatisticClient extends BaseClient {
    @Autowired
    public StatisticClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl)).requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    public void create(Statistic statistic) {
        ResponseEntity<Object> response;
        try {
            response = post("/hit", statistic);
            log.debug("StatisticClient method \"public void create(Statistic statistic)\":" +
                    System.lineSeparator() + response.toString());
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    public List<StatisticInfo> getAllByFilter(StatisticFilterDto statisticFilterDto) {
        String encodedStart = URLEncoder.encode(statisticFilterDto.getStart(), StandardCharsets.UTF_8);
        String encodedEnd = URLEncoder.encode(statisticFilterDto.getEnd(), StandardCharsets.UTF_8);

        Map<String, Object> parameters = Map.of("start", encodedStart, "end", encodedEnd, "uris", String.join(",", statisticFilterDto.getUris()), "unique", statisticFilterDto.isUnique());

        ResponseEntity<Object> response = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        Object body = response.getBody();
        try {
            return (ArrayList<StatisticInfo>) body;
        } catch (ClassCastException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            return new ArrayList<>();
        }
    }
}
