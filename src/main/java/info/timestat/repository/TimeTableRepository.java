package info.timestat.repository;

import info.timestat.entity.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {
    List<TimeTable> findByIdTask(Long id);
}
