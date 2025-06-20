package booking_uniwa_app.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Κεντρική υπηρεσία για την ανάγνωση και εγγραφή δεδομένων σε αρχεία CSV.
 */
public class CsvService {
    private static final String DATA_DIR = "data";

    public CsvService() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }
    }

    public List<String[]> readCsv(String filename) {
        List<String[]> records = new ArrayList<>();
        File file = new File(DATA_DIR + File.separator + filename);
        if (!file.exists()) {
            return records;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    records.add(line.split(";"));
                }
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση του αρχείου " + filename + ": " + e.getMessage());
        }
        return records;
    }

    public void writeCsv(String filename, List<String[]> data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_DIR + File.separator + filename))) {
            for (String[] record : data) {
                writer.println(String.join(";", record));
            }
        } catch (IOException e) {
            System.err.println("Σφάλμα κατά την εγγραφή στο αρχείο " + filename + ": " + e.getMessage());
        }
    }
}
