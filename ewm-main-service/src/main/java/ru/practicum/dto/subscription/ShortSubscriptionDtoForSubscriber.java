package ru.practicum.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.user.UserShortDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortSubscriptionDtoForSubscriber {
    private Long id;
    private UserShortDto authorOfContent;
    private String created;
}
