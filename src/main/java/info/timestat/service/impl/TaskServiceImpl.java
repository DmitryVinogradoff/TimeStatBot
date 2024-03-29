package info.timestat.service.impl;

import info.timestat.entity.Task;
import info.timestat.repository.TaskRepository;
import info.timestat.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void delete(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        optionalTask.get().setDel(true);
        taskRepository.save(optionalTask.get());

    }

    @Override
    public Optional<Task> getById(Long id) { return taskRepository.findById(id); }

    @Override
    public List<Task> getAllByIdUserTelegram(Long id) {
        return taskRepository.findByIdUserTelegramAndDel(id, false);
    }

    @Override
    public Optional<Task> getByIdUserTelegramAndName(Long id, String name) {
        return taskRepository.findByIdUserTelegramAndName(id, name);
    }

    @Override
    public Task getLastAddedTask(Long id) {
        return taskRepository.findTopByIdUserTelegramOrderByIdDesc(id);
    }
}
