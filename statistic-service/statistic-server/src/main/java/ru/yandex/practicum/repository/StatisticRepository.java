package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.StatisticInfoDto;
import ru.yandex.practicum.model.Statistic;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    @Query("SELECT new ru.yandex.practicum.StatisticInfoDto(s.app, s.uri, COUNT(s.ip) AS quantity) " +
            "FROM statistics AS s " +
            "WHERE (s.created BETWEEN :start AND :end) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY quantity DESC")
    ArrayList<StatisticInfoDto> getAllStatistic(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.yandex.practicum.StatisticInfoDto(s.app, s.uri, COUNT(DISTINCT s.ip) AS quantity) " +
            "FROM statistics AS s " +
            "WHERE (s.created BETWEEN :start AND :end) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY quantity DESC")
    ArrayList<StatisticInfoDto> getAllStatisticUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.yandex.practicum.StatisticInfoDto(s.app, s.uri, COUNT(s.ip) AS quantity) " +
            "FROM statistics AS s " +
            "WHERE (s.created BETWEEN :start AND :end) AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY quantity DESC")
    ArrayList<StatisticInfoDto> getStatisticInUris(LocalDateTime start, LocalDateTime end, ArrayList<String> uris);

    @Query("SELECT new ru.yandex.practicum.StatisticInfoDto(s.app, s.uri, COUNT(DISTINCT s.ip) AS quantity) " +
            "FROM statistics AS s " +
            "WHERE (s.created BETWEEN :start AND :end) AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY quantity DESC")
    ArrayList<StatisticInfoDto> getStatisticInUrisUnique(LocalDateTime start, LocalDateTime end, ArrayList<String> uris);
}
