package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.hit.HitDto;
import ru.practicum.hit.HitDtoForSend;
import ru.practicum.hit.HitShort;
import ru.practicum.service.StatsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public HitDtoForSend saveHit(@RequestBody HitDto hitDto) {
        return statsService.saveHit(hitDto);
    }

    @GetMapping("/stats")
    public List<HitShort> getStats(@RequestParam String start, @RequestParam String end,
                                   @RequestParam(required = false, defaultValue = "") String[] uris,
                                   @RequestParam(required = false, defaultValue = "false") boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }
}
