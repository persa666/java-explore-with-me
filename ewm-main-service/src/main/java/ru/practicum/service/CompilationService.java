package ru.practicum.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.request.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto replaceCompilationById(Long compId, UpdateCompilationRequest request);

    CompilationDto getCompilationById(Long compId);

    List<CompilationDto> getCompilations(Boolean pinned, PageRequest pageRequest);
}
