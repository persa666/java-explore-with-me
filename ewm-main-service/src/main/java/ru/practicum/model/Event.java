package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Column(name = "annotation", nullable = false)
    private String annotation;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "created_on", nullable = false)

    private LocalDateTime createdOn;
    @Column(name = "description", nullable = false)

    private String description;
    @Column(name = "event_date", nullable = false)

    private LocalDateTime eventDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")

    private User initiator;
    @OneToOne
    @JoinColumn(name = "location_id")

    private Position position;
    @Column(name = "paid", nullable = false)

    private Boolean paid;
    @Column(name = "participant_limit", nullable = false)

    private Integer participantLimit;
    @Column(name = "published_on")

    private LocalDateTime publishedOn;
    @Column(name = "request_moderation", nullable = false)

    private Boolean requestModeration;
    @Enumerated(value = EnumType.STRING)

    private State state;
    @Column(name = "title", nullable = false)
    private String title;
}
