package com.mynerdygarage.works_parts.service;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.parts.dto.PartMapper;
import com.mynerdygarage.parts.dto.PartShortDto;
import com.mynerdygarage.parts.model.Part;
import com.mynerdygarage.parts.repository.PartRepository;
import com.mynerdygarage.work.model.Work;
import com.mynerdygarage.work.repository.WorkRepository;
import com.mynerdygarage.works_parts.model.WorkPart;
import com.mynerdygarage.works_parts.model.WorkPartId;
import com.mynerdygarage.works_parts.repository.WorkPartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkPartServiceImpl implements WorkPartService {

    private final WorkRepository workRepository;
    private final PartRepository partRepository;
    private final WorkPartRepository workPartRepository;

    @Transactional
    @Override
    public void addPartToWork(Long userId, Long partId, Long workId) {

        log.info("-- Adding part with partId={} to work with workId={}", partId, workId);

        Part part = partRepository.findById(partId).orElseThrow(() ->
                new NotFoundException("- partId not found: " + partId));

        if (!userId.equals(part.getUser().getId())) {
            throw new ConflictOnRequestException(
                    "- User with Id=" + userId + " is not buyer of part with id=" + partId);
        }

        Work work = workRepository.findById(workId).orElseThrow(() ->
                new NotFoundException("- workId not found: " + workId));

        if (!userId.equals(work.getUser().getId())) {
            throw new ConflictOnRequestException(
                    "- User with Id=" + userId + " is not initiator of work with id=" + work);
        }

        WorkPartId workPartId = new WorkPartId(workId, partId);

        if (workPartRepository.existsById(workPartId)) {
            throw new ConflictOnRequestException(String.format(
                    "- Composite key with workId=%d and partId=%d already exists", workId, partId));
        }

        WorkPart workPart = new WorkPart();
        workPart.setWorkPartId(workPartId);
        workPart.setWork(work);
        workPart.setPart(part);

        workPartRepository.save(workPart);

        log.info("-- Part with partId={} added to work with workId={}", partId, workId);
    }

    @Override
    public List<PartShortDto> getPartsByWorkId(Long userId, Long workId) {

        log.info("-- Returning parts by workId={}", workId);

        Work work = workRepository.findById(workId).orElseThrow(() ->
                new NotFoundException("- workId not found: " + workId));

        if (!userId.equals(work.getUser().getId())) {
            throw new ConflictOnRequestException(
                    "- User with Id=" + userId + " is not initiator of work with id=" + work);
        }

        List<PartShortDto> foundParts = new ArrayList<>();

        for (WorkPart workPart : workPartRepository.findByWorkPartIdWorkId(workId)) {

            foundParts.add(PartMapper.modelToShortDto(workPart.getPart()));
        }

        log.info("-- List of parts by workId={} returned, size={}", workId, foundParts.size());

        return foundParts;
    }

    @Transactional
    @Override
    public void removePartFromWork(Long userId, Long partId, Long workId) {

        log.info("-- Removing part with partId={} from work with workId={}", partId, workId);

        WorkPartId workPartId = new WorkPartId(workId, partId);
        WorkPart workPartToRemove = workPartRepository.findById(workPartId).orElseThrow(() ->
                new NotFoundException("- workPartId not found: " + workPartId));

        Part part = workPartToRemove.getPart();

        if (!userId.equals(part.getUser().getId())) {
            throw new ConflictOnRequestException(
                    "- User with Id=" + userId + " is not buyer of part with id=" + partId);
        }

        Work work = workPartToRemove.getWork();

        if (!userId.equals(work.getUser().getId())) {
            throw new ConflictOnRequestException(
                    "- User with Id=" + userId + " is not initiator of work with id=" + work);
        }

        workPartRepository.deleteById(workPartId);

        log.info("-- Part with partId={} removed from work with workId={}", partId, workId);
    }
}
