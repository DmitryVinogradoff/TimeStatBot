package ru.dmitryvinogradov.Histograms;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Histogram
{
    private List<String> nameTask = new ArrayList<>();
    private List<Integer> timeTask = new ArrayList<>();

    public void setNameTask(String name) {
        this.nameTask.add(name);
    }

    public void setTimeTask(Integer time) {
        this.timeTask.add(time);
    }

    public Histogram() {
    }

    public InputStream generateHistogram(String period) throws IOException{
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        Iterator<Integer> iterator = timeTask.iterator();
        for(String name : nameTask){
            dataset.setValue(name, iterator.next());
        }
        JFreeChart histogram = ChartFactory.createPieChart(period, dataset, true, true, false);
        PiePlot plot = (PiePlot) histogram.getPlot();
        plot.setSimpleLabels(true);
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                "{2}", new DecimalFormat("0"), new DecimalFormat("0%"));
        plot.setLabelGenerator(gen);

        BufferedImage bufferedImage = histogram.createBufferedImage(450, 400);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", os);
        InputStream is = new ByteArrayInputStream(os.toByteArray());
        return is;
    }


}
