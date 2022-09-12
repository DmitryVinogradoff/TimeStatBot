package info.timestat.service;

import info.timestat.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> getAll();
    Task save(Task task);
    void delete(Long id);
    List<Task> getAllByIdUserTelegram(Long id);
    Optional<Task> getByIdUserTelegramAndName(Long id, String name);
}
