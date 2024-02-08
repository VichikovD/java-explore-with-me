package ru.yandex.practicum.compilation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @EntityGraph(attributePaths = {"events"})
    @Query(value = "SELECT compilation " +
            "FROM Compilation AS compilation " +
            "WHERE (:pinned IS NULL OR compilation.pinned = :pinned)")
    List<Compilation> findFiltered(Boolean pinned, Pageable pageable);
}
