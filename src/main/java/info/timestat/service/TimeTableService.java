package info.timestat.service;

import info.timestat.entity.TimeTable;

public interface TimeTableService {
    Iterable<TimeTable> getAll();
    TimeTable save(TimeTable timeTable);
    void delete(Long id);
}
