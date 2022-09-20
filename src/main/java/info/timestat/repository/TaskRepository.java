package info.timestat.repository;

import info.timestat.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository <Task, Long> {
    List<Task> findByIdUserTelegramAndDel(Long id, boolean isDel);
    Optional<Task> findByIdUserTelegramAndName(Long id, String name);
    Task findTopByIdUserTelegramOrderByIdDesc(Long id);

}
