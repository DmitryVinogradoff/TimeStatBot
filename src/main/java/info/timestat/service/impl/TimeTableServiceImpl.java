package info.timestat.service.impl;

import info.timestat.entity.TimeTable;
import info.timestat.repository.TimeTableRepository;
import info.timestat.service.TimeTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class TimeTableServiceImpl implements TimeTableService {
    @Autowired
    private TimeTableRepository timeTableRepository;

    @Override
    public Iterable<TimeTable> getAll() { return timeTableRepository.findAll(); }

    @Override
    public TimeTable save(TimeTable timeTable) { return timeTableRepository.save(timeTable); }

    @Override
    public void delete(long id) { timeTableRepository.deleteById(id); }

    @Override
    public Optional<TimeTable> getById(Long id) {
        return timeTableRepository.findById(id);
    }

    @Override
    public List<TimeTable> getByIdTask(Long id) {
        return timeTableRepository.findByIdTask(id);
    }

    @Override
    public List<TimeTable> getByIdTaskAndStartedAtAfter(Long id, Timestamp startedAt) {
        return timeTableRepository.findByIdTaskAndStartedAtIsAfter(id, startedAt);
    }
}
