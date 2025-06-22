package ru.practicum.shareit.request;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(name = "requestor", nullable = false)
    private Long requestor;

    @Column(name = "time_request", nullable = false)
    private LocalDateTime dateTimeRequest;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
