package info.timestat.service;

import info.timestat.entity.Task;

public interface TaskService {
    Iterable<Task> getAll();
    Task save(Task task);
    void delete(Long id);
}
