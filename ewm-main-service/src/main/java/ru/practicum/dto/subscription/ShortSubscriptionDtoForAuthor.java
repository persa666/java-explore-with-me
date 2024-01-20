package ru.practicum.dto.subscription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.user.UserShortDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortSubscriptionDtoForAuthor {
    private Long id;
    private UserShortDto subscriber;
    private String created;
}
