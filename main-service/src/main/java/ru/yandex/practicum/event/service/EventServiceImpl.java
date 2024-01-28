package ru.yandex.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.event.EventDto;
import ru.yandex.practicum.event.EventRepository;
import ru.yandex.practicum.event.EventSort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    final EventRepository eventRepository;

    @Override
    public List<EventDto> getFiltered(String text, int[] categories, boolean paid, String rangeStart, String rangeEnd, boolean onlyAvailable, EventSort eventSort, int offset, int size) {
        return null;
        //Обратите внимание: \n- это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
        // - текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
        // - если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события,
        // которые произойдут позже текущей даты и времени - информация о каждом событии должна включать в себя количество просмотров
        // и количество уже одобренных заявок на участие - информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
        // нужно сохранить в сервисе статистики В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список
    }

    @Override
    public EventDto getById(int compId) {
        return null;
        // Обратите внимание:\n- событие должно быть опубликовано - информация о событии должна включать в себя
        // количество просмотров и количество подтвержденных запросов - информацию о том, что по этому эндпоинту был осуществлен
        // и обработан запрос, нужно сохранить в сервисе статистики В случае, если события с заданным id не найдено, возвращает статус код 404
    }
}