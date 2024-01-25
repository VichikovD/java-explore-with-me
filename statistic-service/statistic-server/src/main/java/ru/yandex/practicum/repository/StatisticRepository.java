package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.Statistic;
import ru.yandex.practicum.StatisticInfo;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    @Query("SELECT new ru.yandex.practicum.StatisticInfo(s.app, s.uri, COUNT(s.ip) AS quantity) " +
            "FROM statistics AS s " +
            "WHERE (s.timestamp BETWEEN :start AND :end) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY quantity DESC")
    ArrayList<StatisticInfo> getAllStatistic(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.yandex.practicum.StatisticInfo(s.app, s.uri, COUNT(DISTINCT s.ip) AS quantity) " +
            "FROM statistics AS s " +
            "WHERE (s.timestamp BETWEEN :start AND :end) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY quantity DESC")
    ArrayList<StatisticInfo> getAllStatisticUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.yandex.practicum.StatisticInfo(s.app, s.uri, COUNT(s.ip) AS quantity) " +
            "FROM statistics AS s " +
            "WHERE (s.timestamp BETWEEN :start AND :end) AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY quantity DESC")
    ArrayList<StatisticInfo> getStatisticInUris(LocalDateTime start, LocalDateTime end, ArrayList<String> uris);

    @Query("SELECT new ru.yandex.practicum.StatisticInfo(s.app, s.uri, COUNT(DISTINCT s.ip) AS quantity) " +
            "FROM statistics AS s " +
            "WHERE (s.timestamp BETWEEN :start AND :end) AND s.uri IN :uris " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY quantity DESC")
    ArrayList<StatisticInfo> getStatisticInUrisUnique(LocalDateTime start, LocalDateTime end, ArrayList<String> uris);
}
