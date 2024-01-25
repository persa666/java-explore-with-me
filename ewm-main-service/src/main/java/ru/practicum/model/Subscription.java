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
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "author_of_content")
    private User authorOfContent;

    @OneToOne
    @JoinColumn(name = "subscriber_id")
    private User subscriber;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    public Subscription(User authorOfContent, User subscriber) {
        this.authorOfContent = authorOfContent;
        this.subscriber = subscriber;
    }
}
