package ru.dmitryvinogradov.DataSet;

import ru.dmitryvinogradov.Models.Tasks;
import ru.dmitryvinogradov.Models.TimeTable;
import ru.dmitryvinogradov.Services.TasksService;
import ru.dmitryvinogradov.Services.TimeTableService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DataSetGenerator {
    private static List<List<Timestamp>> generateDataSetOnFullDay(int iter, LocalDateTime startTime, LocalDateTime stopTime){
        ZoneId zone = ZoneId.of("Europe/Moscow");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(stopTime);
        Timestamp timestampStart = new Timestamp(startTime.toInstant(zoneOffSet).toEpochMilli());
        Timestamp timestampStop = new Timestamp(stopTime.toInstant(zoneOffSet).toEpochMilli());

        List<Timestamp> timeStartList = new LinkedList<>();
        List<Timestamp> timeStopList = new LinkedList<>();

        int i = 0;
        long offset = timestampStart.getTime();
        long diff = timestampStop.getTime() - offset + 1;
        long step = diff / iter;
        while(i < iter){
            Timestamp randStart = new Timestamp(offset + (long)(Math.random() * step));
            Timestamp randStop = new Timestamp(offset + (long)(Math.random() * step));
            if(randStart.before(timestampStop) && randStart.before(randStop) &&
                    ((Timestamp.valueOf(randStop.toString()).getTime()-Timestamp.valueOf(randStart.toString()).getTime())/60000) != 0){
                timeStartList.add(randStart);
                timeStopList.add(randStop);
                i++;
                offset += step;
            }
        }
        List<List<Timestamp>> result = new LinkedList<>();
        result.add(timeStartList);
        result.add(timeStopList);
        return result;
    }

    public static boolean generateDataSetOnMonth(long idUserTelegram){
        List<List<List<Timestamp>>> result = new LinkedList<>();
        TasksService tasksService = new TasksService();
        TimeTableService timeTableService = new TimeTableService();
        if(tasksService.findTestData(idUserTelegram).isEmpty()) {
            String[] tasksName = {"Работа", "Учеба", "Отдых", "Спорт", "Развлечения"};
            long[] idTasks = new long[tasksName.length];
            for (int i = 0; i < tasksName.length; i++) {
                idTasks[i] = tasksService.saveTask(new Tasks(tasksName[i], idUserTelegram, true));
            }

            LocalDateTime nowTime = LocalDateTime.now();
            LocalDateTime startTime = nowTime;
            startTime = startTime.withHour(0).withMinute(0).withSecond(0).withNano(0);

            int taskOnDay = 20;
            for (int i = 30; i > 0; i--) {
                result.add(generateDataSetOnFullDay(taskOnDay, startTime.minusDays(i), startTime.minusDays(i).plusDays(1)));
            }
            result.add(generateDataSetOnFullDay(taskOnDay, startTime, nowTime));
            int i = 0;
            for (List<List<Timestamp>> day : result) {
                List<Timestamp> startTimeList = day.get(0);
                List<Timestamp> stopTimeList = day.get(1);
                Iterator<Timestamp> iter1 = startTimeList.listIterator();
                Iterator<Timestamp> iter2 = stopTimeList.listIterator();
                while (iter1.hasNext() && iter2.hasNext()) {
                    TimeTable timeTable = new TimeTable();

                    timeTable.setIdTask(idTasks[i]);
                    timeTable.setStartedAt(iter1.next());
                    timeTable.setStoppedAt(iter2.next());
                    timeTable.setStatus(false);
                    timeTableService.setTestData(timeTable);
                    i++;
                    if (i == 5) i = 0;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
