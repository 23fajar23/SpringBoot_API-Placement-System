package com.placement.Placement.service.impl;

import com.placement.Placement.helper.convert.dto.Dto;
import com.placement.Placement.helper.convert.entity.Entity;
import com.placement.Placement.helper.response.Response;
import com.placement.Placement.model.entity.Education;
import com.placement.Placement.model.request.EducationRequest;
import com.placement.Placement.model.response.EducationResponse;
import com.placement.Placement.repository.EducationRepository;
import com.placement.Placement.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {
    private final EducationRepository educationRepository;

    @Override
    public ResponseEntity<Object> getAll() {
        List<EducationResponse> responseList = educationRepository
                .findAll()
                .stream()
                .map(Entity::convertToDto)
                .toList();
        return Response.responseData(HttpStatus.OK, "Successfully get all educations", responseList);
    }

    @Override
    public ResponseEntity<Object> getById(String id) {
        Education education = educationRepository.findById(id).orElse(null);
        if (education !=  null) {
            return Response.responseData(HttpStatus.OK, "Successfully get education", education);
        }else{
            return Response.responseData(HttpStatus.NOT_FOUND, "Education not found", null);
        }
    }

    @Override
    public EducationResponse findById(String id) {
        Education education = educationRepository.findById(id).orElse(null);
        if (education !=  null) {
            return Entity.convertToDto(education);
        }

        return null;
    }

    @Override
    public ResponseEntity<Object> create(EducationRequest educationRequest) {
        Education education = educationRepository.findByName(educationRequest.getEducation()).orElse(null);
        if (education == null)
        {
            education = Dto.convertToEntity(educationRequest);
            educationRepository.save(education);
            return Response.responseData(HttpStatus.OK, "Successfully create education", education);
        }else{
            return Response.responseData(HttpStatus.FOUND, "Education already exist", null);
        }
    }

    @Override
    public ResponseEntity<Object> update(EducationRequest educationRequest) {
        Education education = educationRepository.findById(educationRequest.getId()).orElse(null);
        Education available = educationRepository.findByName(educationRequest.getEducation()).orElse(null);

        if (education != null) {
            if (available == null){
                education.setEducation(educationRequest.getEducation());
                education.setValue(educationRequest.getValue());
                educationRepository.save(education);
                return Response.responseData(HttpStatus.OK, "Successfully update education", education);
            }else{
                return Response.responseData(HttpStatus.FOUND, "Education Name already exist", null);
            }
        }

        return Response.responseData(HttpStatus.NOT_FOUND, "Education not found", null);
    }

    @Override
    public ResponseEntity<Object> remove(String id) {
        Education education = educationRepository.findById(id).orElse(null);

        if (education != null) {
            educationRepository.delete(education);
            return Response.responseData(HttpStatus.OK, "Successfully remove education", null);
        }else{
            return Response.responseData(HttpStatus.NOT_FOUND, "Education not found", null);
        }
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
