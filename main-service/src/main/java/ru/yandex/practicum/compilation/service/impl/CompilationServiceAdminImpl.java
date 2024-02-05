package ru.yandex.practicum.compilation.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.compilation.CompilationRepository;
import ru.yandex.practicum.compilation.model.Compilation;
import ru.yandex.practicum.compilation.model.CompilationMapper;
import ru.yandex.practicum.compilation.model.dto.CompilationCreateDto;
import ru.yandex.practicum.compilation.model.dto.CompilationInfoDto;
import ru.yandex.practicum.compilation.model.dto.CompilationRequestDto;
import ru.yandex.practicum.compilation.service.CompilationServiceAdmin;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.dto.EventShortInfoDto;
import ru.yandex.practicum.event.model.mapper.EventMapper;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceAdminImpl implements CompilationServiceAdmin {
    final CompilationRepository compilationRepository;
    final EventRepository eventRepository;

    @Override
    public CompilationInfoDto create(CompilationCreateDto compilationDto) {
        Set<Long> eventIdList = compilationDto.getEvents();
        Set<Event> eventList = eventRepository.findAllByIdIn(eventIdList);

        validateEvents(eventIdList, eventList);

        Compilation compilationToSave = CompilationMapper.toModel(compilationDto, eventList);
        Compilation compilationSaved = compilationRepository.save(compilationToSave);

        Set<EventShortInfoDto> eventShortInfoDtoSet = EventMapper.modelSetToShortInfoDtoSet(eventList);
        CompilationInfoDto compilationInfoDto = CompilationMapper.toInfoDto(compilationSaved, eventShortInfoDtoSet);
        // TODO set for Collection confirmedRequests + views;
        return compilationInfoDto;
    }

    @Override
    public CompilationInfoDto update(CompilationRequestDto compilationRequestDto, long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compilationId + " was not found"));

        Set<Long> eventIdList = compilationRequestDto.getEvents();
        Set<Event> eventSet = null;
        if (eventIdList != null) {
            eventSet = eventRepository.findAllByIdIn(eventIdList);
            validateEvents(eventIdList, eventSet);
        }

        CompilationMapper.updateModelWithRequestDtoNotNullFields(compilation, compilationRequestDto, eventSet);
        Compilation compilationSaved = compilationRepository.save(compilation);

        Set<EventShortInfoDto> eventShortInfoDtoSet = EventMapper.modelSetToShortInfoDtoSet(compilationSaved.getEvents());
        CompilationInfoDto compilationInfoDto = CompilationMapper.toInfoDto(compilationSaved, eventShortInfoDtoSet);
        // TODO set for Collection confirmedRequests + views;
        return compilationInfoDto;
    }

    @Override
    public void delete(long compilationId) {
        compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compilationId + " was not found"));
        compilationRepository.deleteById(compilationId);
    }

    private void validateEvents(Set<Long> eventIdList, Set<Event> eventList) {
        long eventsInCompilationQuantity = eventIdList.size();
        if (eventList.size() < eventsInCompilationQuantity) {
            List<Long> missingEventIdList = new ArrayList<>();
            List<Long> foundRequestIdList = eventList.stream()
                    .map(Event::getId)
                    .collect(Collectors.toList());
            for (long requestId : eventIdList) {
                if (!foundRequestIdList.contains(requestId)) {
                    missingEventIdList.add(requestId);
                }
            }
            throw new NotFoundException("Events with ids=" + missingEventIdList + " are not found");
        }
    }
}
