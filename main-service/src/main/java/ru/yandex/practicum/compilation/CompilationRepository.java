package ru.yandex.practicum.compilation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query(value = "SELECT compilation " +
            "FROM Compilation AS compilation " +
            "LEFT JOIN FETCH compilation.events AS events " +
            "LEFT JOIN FETCH events.category AS category " +
            "LEFT JOIN FETCH events.initiator AS initiator " +
            "LEFT JOIN FETCH events.location AS location " +
            "WHERE (:pinned IS NULL OR compilation.pinned = :pinned)")
    List<Compilation> findAllFiltered(Boolean pinned, Pageable pageable);
}
