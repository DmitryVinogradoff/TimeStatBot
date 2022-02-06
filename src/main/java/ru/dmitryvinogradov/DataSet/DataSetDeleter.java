package ru.dmitryvinogradov.DataSet;

import ru.dmitryvinogradov.Models.Tasks;
import ru.dmitryvinogradov.Services.TasksService;
import ru.dmitryvinogradov.Services.TimeTableService;

import java.util.List;

public class DataSetDeleter {
    public static void deleteDataSet(long idUserTelegram){
        TasksService tasksService = new TasksService();
        TimeTableService timeTableService = new TimeTableService();
        List<Tasks> tasks = tasksService.findTestData(idUserTelegram);
        for(Tasks task: tasks){
            tasksService.deleteTask(task.getId());
            timeTableService.deleteByIdTask(task.getId());
        }
    }
}
