package ru.yandex.practicum.event.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.event.Event;
import ru.yandex.practicum.event.EventMapper;
import ru.yandex.practicum.event.EventRepository;
import ru.yandex.practicum.event.dto.EventInfoDto;
import ru.yandex.practicum.event.dto.EventRequestDto;
import ru.yandex.practicum.event.service.EventServicePrivate;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServicePrivateImpl implements EventServicePrivate {
    final EventRepository eventRepository;
    final UserRepository userRepository;

    @Override
    public EventInfoDto create(long initiatorId, EventRequestDto eventRequestDto) {
        userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException("User with id=" + initiatorId + " was not found"));

        return null;
    }

    @Override
    public List<EventInfoDto> getByInitiatorIdFiltered(long initiatorId, Pageable pageable) {
        // В спецификации написано только "В случае, если по заданным фильтрам не найдено ни одного события, возвращает пустой список"
        // про валидацию id не написано, но мне кажется, она здесь нужна
        userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException("User with id=" + initiatorId + " was not found"));

        List<Event> eventList = eventRepository.findAllByInitiatorId(initiatorId, pageable);
        return EventMapper.modelListToDtoList(eventList);
    }

    @Override
    public EventInfoDto getById(int compId) {
        return null;
        // Обратите внимание:\n- событие должно быть опубликовано - информация о событии должна включать в себя
        // количество просмотров и количество подтвержденных запросов - информацию о том, что по этому эндпоинту был осуществлен
        // и обработан запрос, нужно сохранить в сервисе статистики В случае, если события с заданным id не найдено, возвращает статус код 404
    }
}
