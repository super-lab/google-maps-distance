package com.bitzl.open.data.distance.heatmap.gather.service;

import com.bitzl.open.data.distance.heatmap.gather.model.TravelInfo;
import com.bitzl.open.data.distance.heatmap.core.model.location.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class ExportService {

    private static final String HEADER = "latitude;longitude;address;distance;distanceText;duration;durationText;status";

    private ExportStrategy exportStrategy;

    @Autowired
    public ExportService(ExportStrategy exportStrategy) {
        this.exportStrategy = exportStrategy;
    }

    public void save(Writer writer, List<Coordinate> origins, TravelInfo travelInfo) throws IOException {
        appendContent(writer, exportStrategy.createRows(origins, travelInfo));
    }

    public void save(String filename, List<Coordinate> origins, TravelInfo travelInfo) throws IOException {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, true), "UTF-8"))) {
            if (fileIsEmpty(filename)) {
                writeHeader(writer);
            }
            appendContent(writer, exportStrategy.createRows(origins, travelInfo));
        }
    }

    private boolean fileIsEmpty(String filename) {
        return new File(filename).length() == 0;
    }

    private void writeHeader(Writer writer) throws IOException {
        writer.write(HEADER);
        writer.write("\n");
    }

    private void appendContent(Writer writer, List<String> rows) throws IOException {
        for (String row : rows) {
            writer.write(row);
            writer.write("\n");
        }
    }
}
