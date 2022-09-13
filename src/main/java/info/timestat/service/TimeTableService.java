package info.timestat.service;

import info.timestat.entity.TimeTable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface TimeTableService {
    Iterable<TimeTable> getAll();
    TimeTable save(TimeTable timeTable);
    void delete(long id);
    Optional<TimeTable> getById(Long id);
    List<TimeTable> getByIdTask(Long id);
    List<TimeTable> getByIdTaskAndStartedAtAfter(Long id, Timestamp startedAt);
}
