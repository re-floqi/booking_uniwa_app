package booking_uniwa_app.service;

import booking_uniwa_app.model.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Υπηρεσία διαχείρισης για όλους τους τύπους παραστάσεων.
 */
public class PerformanceService {
    private static final String FILENAME = "performances.csv";
    private final CsvService csvService;
    private List<Performance> performances;

    public PerformanceService(CsvService csvService) {
        this.csvService = csvService;
        this.performances = new ArrayList<>();
    }
    
    public void loadData() {
        List<String[]> records = csvService.readCsv(FILENAME);
        for (String[] record : records) {
            try {
                if(record.length < 6) continue;
                String type = record[0];
                String id = record[1];
                String title = record[2];
                String venue = record[3];
                LocalDate date = LocalDate.parse(record[4], DateTimeFormatter.ISO_LOCAL_DATE);
                String artist = record[5];

                if ("THEATER".equalsIgnoreCase(type)) {
                    performances.add(new TheatricalPerformance(id, title, venue, date, artist));
                } else if ("MUSIC".equalsIgnoreCase(type)) {
                    performances.add(new MusicalPerformance(id, title, venue, date, artist));
                }
            } catch (Exception e) {
                System.err.println("Σφάλμα στην ανάγνωση γραμμής παράστασης (παράλειψη): " + String.join(";", record));
            }
        }
    }
    
    public void saveData() {
        List<String[]> data = new ArrayList<>();
        for (Performance performance : performances) {
            data.add(performance.toCsvString().split(";"));
        }
        csvService.writeCsv(FILENAME, data);
    }

    public List<Performance> getAllPerformances() {
        return performances;
    }

    public Optional<Performance> findById(String id) {
        return performances.stream().filter(p -> p.getId().equalsIgnoreCase(id)).findFirst();
    }

    public boolean addPerformance(Performance performance) {
        if (findById(performance.getId()).isPresent()) {
            return false;
        }
        performances.add(performance);
        return true;
    }

    public boolean deletePerformance(String id) {
        return performances.removeIf(p -> p.getId().equalsIgnoreCase(id));
    }
}
