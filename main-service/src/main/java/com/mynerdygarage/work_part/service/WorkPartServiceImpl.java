package com.mynerdygarage.work_part.service;

import com.mynerdygarage.error.exception.ConflictOnRequestException;
import com.mynerdygarage.error.exception.NotFoundException;
import com.mynerdygarage.part.dto.PartMapper;
import com.mynerdygarage.part.dto.PartShortDto;
import com.mynerdygarage.part.model.Part;
import com.mynerdygarage.part.repository.PartRepository;
import com.mynerdygarage.util.PageRequestCreator;
import com.mynerdygarage.work.dto.WorkMapper;
import com.mynerdygarage.work.dto.WorkShortDto;
import com.mynerdygarage.work.model.Work;
import com.mynerdygarage.work.repository.WorkRepository;
import com.mynerdygarage.work_part.model.WorkPart;
import com.mynerdygarage.work_part.model.WorkPartId;
import com.mynerdygarage.work_part.repository.WorkPartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
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

        if (!userId.equals(part.getOwner().getId())) {
            throw new ConflictOnRequestException(
                    "- User with Id=" + userId + " is not buyer of part with id=" + partId);
        }

        Work work = workRepository.findById(workId).orElseThrow(() ->
                new NotFoundException("- workId not found: " + workId));

        if (!userId.equals(work.getInitiator().getId())) {
            throw new ConflictOnRequestException(
                    "- User with Id=" + userId + " is not initiator of work with id=" + work);
        }

        WorkPartId workPartId = new WorkPartId();
        workPartId.setWorkId(workId);
        workPartId.setPartId(partId);

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
    public List<PartShortDto> getPartsByWorkId(Long userId, Long workId, int from, int size) {

        log.info("-- Returning parts by workId={}", workId);

        Work work = workRepository.findById(workId).orElseThrow(() ->
                new NotFoundException("- workId not found: " + workId));

        if (!userId.equals(work.getInitiator().getId())) {
            throw new ConflictOnRequestException(
                    "- User with Id=" + userId + " is not initiator of work with id=" + work);
        }

        PageRequest pageRequest = PageRequestCreator.create(from, size, Sort.unsorted());

        Iterable<WorkPart> foundWorkParts = workPartRepository.findByWorkPartIdWorkId(workId, pageRequest);

        List<PartShortDto> foundParts = new ArrayList<>();

        for (WorkPart workPart : foundWorkParts) {

            foundParts.add(PartMapper.modelToShortDto(workPart.getPart()));
        }

        foundParts.sort(Comparator.comparing(PartShortDto::getName));

        log.info("-- List of parts by workId={} returned, size={}", workId, foundParts.size());

        return foundParts;
    }

    @Override
    public List<WorkShortDto> getWorksByPartId(Long userId, Long partId, int from, int size) {

        log.info("-- Returning works by partId={}", partId);

        Part part = partRepository.findById(partId).orElseThrow(() ->
                new NotFoundException("- partId not found: " + partId));

        if (!userId.equals(part.getOwner().getId())) {
            throw new ConflictOnRequestException(
                    "- User with Id=" + userId + " is not owner of part with id=" + part);
        }

        PageRequest pageRequest = PageRequestCreator.create(from, size, Sort.unsorted());

        Iterable<WorkPart> foundWorkParts = workPartRepository.findByWorkPartIdPartId(partId, pageRequest);

        List<WorkShortDto> foundWorks = new ArrayList<>();

        for (WorkPart workPart : foundWorkParts) {

            foundWorks.add(WorkMapper.modelToShortDto(workPart.getWork()));
        }

        foundWorks.sort(Comparator.comparing(WorkShortDto::getTitle));

        log.info("-- List of parts by partId={} returned, size={}", partId, foundWorks.size());

        return foundWorks;
    }

    @Transactional
    @Override
    public void removePartFromWork(Long userId, Long partId, Long workId) {

        log.info("-- Removing part with partId={} from work with workId={}", partId, workId);

        WorkPartId workPartId = new WorkPartId();
        workPartId.setWorkId(workId);
        workPartId.setPartId(partId);
        WorkPart workPartToRemove = workPartRepository.findById(workPartId).orElseThrow(() ->
                new NotFoundException("- workPartId not found: " + workPartId));

        Part part = workPartToRemove.getPart();

        if (!userId.equals(part.getOwner().getId())) {
            throw new ConflictOnRequestException(
                    "- User with Id=" + userId + " is not buyer of part with id=" + partId);
        }

        Work work = workPartToRemove.getWork();

        if (!userId.equals(work.getInitiator().getId())) {
            throw new ConflictOnRequestException(
                    "- User with Id=" + userId + " is not initiator of work with id=" + work);
        }

        workPartRepository.deleteById(workPartId);

        log.info("-- Part with partId={} removed from work with workId={}", partId, workId);
    }
}
