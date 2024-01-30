package ru.yandex.practicum.event.model;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private long location_id;

    @Column(name = "lat")
    private float lat;

    @Column(name = "lon")
    private float lon;
}
