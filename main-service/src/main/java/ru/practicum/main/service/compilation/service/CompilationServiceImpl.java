package ru.practicum.main.service.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.compilation.MapperCompilation;
import ru.practicum.main.service.compilation.dto.CompilationDto;
import ru.practicum.main.service.compilation.dto.NewCompilationDto;
import ru.practicum.main.service.compilation.dto.UpdateCompilationRequest;
import ru.practicum.main.service.compilation.model.Compilation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final MapperCompilation mapperCompilation;

    @Override
    public List<CompilationDto> getCompilations(GetCompilationsParam param) {
        //TODO
        return List.of();
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        //TODO
        CompilationDto dto = new CompilationDto();
        dto.setId(compId);
        return dto;
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        // TODO
        Compilation compilation = mapperCompilation.toCompilation(newCompilationDto);
        compilation.setId(1L);
        return mapperCompilation.toCompilationDto(compilation);
    }

    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId) {
        // TODO
        Compilation compilation = mapperCompilation.toCompilation(updateCompilationRequest);
        compilation.setId(compId);
        return mapperCompilation.toCompilationDto(compilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        // TODO
    }
}
