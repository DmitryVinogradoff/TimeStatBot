package info.timestat.service;

import info.timestat.entity.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAll();
    Task save(Task task);
    void delete(Long id);
}
