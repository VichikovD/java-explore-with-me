package ru.yandex.practicum.eventRequest.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class EventRequestStatusResult {
    List<EventRequestInfoDto> confirmedRequests = new ArrayList<>();
    List<EventRequestInfoDto> rejectedRequests = new ArrayList<>();

    public void addConfirmed(EventRequestInfoDto requestInfoDto) {
        this.confirmedRequests.add(requestInfoDto);
    }

    public void addRejected(EventRequestInfoDto requestInfoDto) {
        this.rejectedRequests.add(requestInfoDto);
    }

    public void addAllConfirmed(Collection<EventRequestInfoDto> requestInfoDtoCollection) {
        this.confirmedRequests.addAll(requestInfoDtoCollection);
    }

    public void addAllRejected(Collection<EventRequestInfoDto> requestInfoDtoCollection) {
        this.rejectedRequests.addAll(requestInfoDtoCollection);
    }
}
