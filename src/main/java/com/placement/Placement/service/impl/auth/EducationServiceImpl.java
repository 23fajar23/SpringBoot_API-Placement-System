package com.placement.Placement.service.impl.auth;

import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.helper.convert.entity.Entity;
import com.placement.Placement.model.entity.Education;
import com.placement.Placement.model.request.EducationRequest;
import com.placement.Placement.model.response.EducationResponse;
import com.placement.Placement.repository.EducationRepository;
import com.placement.Placement.service.auth.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {
    private final EducationRepository educationRepository;

    @Override
    public List<EducationResponse> getAll() {
        return educationRepository
                .findAll()
                .stream()
                .map(Entity::convertToDto)
                .toList();
    }

    @Override
    public EducationResponse getById(String id) {
        Education education = educationRepository.findById(id).orElse(null);
        if (education !=  null) {
            return Entity.convertToDto(education);
        }

        return null;
    }

    @Override
    public EducationResponse create(EducationRequest educationRequest) {
        Education education = Dto.convertToEntity(educationRequest);
        educationRepository.save(education);

        return Entity.convertToDto(education);
    }

    @Override
    public EducationResponse update(EducationRequest educationRequest) {
        Education education = educationRepository.findById(educationRequest.getId()).orElse(null);

        if (education != null) {
            education.setEducation(educationRequest.getEducation());
            education.setValue(educationRequest.getValue());
            educationRepository.save(education);

            return Entity.convertToDto(education);
        }
        return null;
    }

    @Override
    public EducationResponse remove(String id) {
        Education education = educationRepository.findById(id).orElse(null);
        if (education != null) {
            educationRepository.delete(education);
            return Entity.convertToDto(education);
        }

        return null;
    }

    @Override
    public EducationResponse getByName(String name) {
        Education education = educationRepository.findByName(name).orElse(null);
        if (education != null) {
            return Entity.convertToDto(education);
        }

        return null;
    }
}
