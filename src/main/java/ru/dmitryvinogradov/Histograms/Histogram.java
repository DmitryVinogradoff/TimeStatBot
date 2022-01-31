package ru.dmitryvinogradov.Histograms;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Histogram
{
    private List<String> nameTask = new ArrayList<>();
    private List<Double> timeTask = new ArrayList<>();

    public void setNameTask(String name) {
        this.nameTask.add(name);
    }

    public void setTimeTask(Double time) {
        this.timeTask.add(time);
    }

    public Histogram() {
    }

    public void generateHistogram() throws IOException{
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        Iterator<Double> iterator = timeTask.iterator();
        for(String name : nameTask){
            dataset.setValue(name, iterator.next());
        }

        JFreeChart histogram = ChartFactory.createPieChart3D("Статистика по задачам", dataset);
        histogram = ChartFactory.createRingChart("Статистика по задачам", dataset, false, false, false);

        ChartUtils.saveChartAsPNG(new File("histogram.png"), histogram, 450, 400);
    }
}
