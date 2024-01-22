package ru.yandex.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Service
public class StatisticClient extends BaseClient {
    @Autowired
    public StatisticClient(@Value("${explore-with-me-statistic-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> create(StatisticRequestDto statisticRequestDto) {
        return post("/hit", statisticRequestDto);
    }
    //    https%253A%252F%252Fwww.w3schools.com%252Ftags%252Fref_urlencode.ASP

    public ResponseEntity<Object> getAllByFilter(LocalDateTime start,
                                                 LocalDateTime end,
                                                 ArrayList<String> uris,
                                                 boolean unique) {
        String encodedStart = URLEncoder.encode(start.toString(), StandardCharsets.UTF_8);
        String encodedEnd = URLEncoder.encode(end.toString(), StandardCharsets.UTF_8);

        Map<String, Object> parameters = Map.of(
                "start", encodedStart,
                "end", encodedEnd,
                "uris", uris,
                "unique", unique
        );
        System.out.println("Parameters: " + parameters);
        return get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }
}
