package ru.practicum.main.service.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.category.dto.CategoryDto;
import ru.practicum.main.service.category.dto.NewCategoryDto;
import ru.practicum.main.service.category.mapper.MapperCategory;
import ru.practicum.main.service.category.model.Category;
import ru.practicum.main.service.category.repository.CategoryRepository;
import ru.practicum.main.service.event.EventRepository;
import ru.practicum.main.service.exception.BadRequestException;
import ru.practicum.main.service.exception.DuplicateException;
import ru.practicum.main.service.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MapperCategory mapperCategory;
    private final EventRepository eventRepository;

    private Category getCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Категория с ID '%d' не найдена или недоступна", catId)));
    }

    @Override
    public CategoryDto create(NewCategoryDto newCategoryDto) {
        String catName = newCategoryDto.getName();
        if (catName.isEmpty() || catName.isBlank()) {
            throw new BadRequestException(String.format("Имя категории не может быть- '%s'. Попробуйте снова.", catName));
        }
        if (categoryRepository.existsByName(catName)) {
            throw new DuplicateException(String.format("Категория с именем '%s' уже существует.", catName));
        }
        Category catSave = mapperCategory.toCategory(newCategoryDto);
        return mapperCategory.toCategoryDto(categoryRepository.save(catSave));
    }

    @Override
    public CategoryDto updateById(Long catId, CategoryDto categoryDto) {
        Category category = getCategory(catId);
        String catName = categoryDto.getName();
        if (catName.isEmpty() || catName.isBlank()) {
            throw new BadRequestException(String.format("Имя категории не может быть- '%s'. Попробуйте снова.", catName));
        }
        if (!category.getName().equals(catName) && categoryRepository.existsByName(catName)) {
            throw new DuplicateException(String.format("Категория с именем '%s' уже существует.", catName));
        } else {
            category.setName(catName);
            return mapperCategory.toCategoryDto(categoryRepository.save(category));
        }
    }

    @Override
    public void deleteById(Long catId) {
        getCategory(catId);
        if (eventRepository.existsByCategoryId(catId)) {
            throw new DuplicateException("Существуют события, связанные с категорией");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto getById(Long catId) {
        Category category = getCategory(catId);
        return mapperCategory.toCategoryDto(category);
    }

    @Override
    public List<CategoryDto> getAll(Pageable pageable) {
        return mapperCategory.toCategoryDtoList(categoryRepository.findAll(pageable).toList());
    }
}
