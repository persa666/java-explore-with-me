package ru.practicum.hit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HitDtoForSend {
    private int id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
