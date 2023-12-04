package com.example.loatradelife.service;

import com.example.loatradelife.domain.InspectionTime;
import com.example.loatradelife.repository.InspectionTimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InspectionTimeService {
    private final InspectionTimeRepository inspectionTimeRepository;

    @Transactional
    public long createInspectionTime(InspectionTime inspectionTime) {
        long id = -1;

        id = inspectionTimeRepository.save(inspectionTime).getId();

        return id;
    }

    public List<InspectionTime> selectAllInspectionTime() {
        return inspectionTimeRepository.findAll();
    }

    public Optional<InspectionTime> selectOneInspectionTime(Long id) {
        return inspectionTimeRepository.findById(id);
    }

    public Optional<InspectionTime> selectOneRecentInspectionTime() {
        return inspectionTimeRepository.findTopByOrderByIdDesc();
    }

    @Transactional
    public void updateInspectionTime(long id, InspectionTime inspectionTime) {
        inspectionTimeRepository.findById(id).ifPresent(value -> value.updateTimes(inspectionTime.getStartTime(), inspectionTime.getEndTime()));
    }

    @Transactional
    public void deleteInspectionTime(long id) {
        inspectionTimeRepository.deleteById(id);
    }
}