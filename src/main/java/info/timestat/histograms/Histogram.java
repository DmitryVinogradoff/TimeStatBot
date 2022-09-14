package info.timestat.histograms;

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
import java.time.Duration;
import java.util.Iterator;
import java.util.Map;

public class Histogram {

    public static InputStream generateHistogram(Map<String, Duration> data, String period) throws IOException{
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();

        for (Map.Entry<String, Duration> entry : data.entrySet()) {
            dataset.setValue(entry.getKey(), entry.getValue().toMillis());
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
        return new ByteArrayInputStream(os.toByteArray());
    }
}
