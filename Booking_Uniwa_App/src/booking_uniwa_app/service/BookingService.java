package booking_uniwa_app.service;

import booking_uniwa_app.model.Booking;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Υπηρεσία διαχείρισης κρατήσεων και στατιστικών.
 */
public class BookingService {
    private static final String FILENAME = "bookings.csv";
    private final CsvService csvService;
    private List<Booking> bookings;

    public BookingService(CsvService csvService) {
        this.csvService = csvService;
        this.bookings = new ArrayList<>();
    }

    public void loadData() {
        List<String[]> records = csvService.readCsv(FILENAME);
        for (String[] record : records) {
            if(record.length < 2) continue;
            bookings.add(new Booking(record[0], record[1]));
        }
    }

    public void saveData() {
        List<String[]> data = new ArrayList<>();
        for (Booking booking : bookings) {
            data.add(booking.toCsvString().split(";"));
        }
        csvService.writeCsv(FILENAME, data);
    }

    public boolean addBooking(Booking booking) {
        boolean exists = bookings.stream().anyMatch(b -> 
                b.getCustomerId().equalsIgnoreCase(booking.getCustomerId()) &&
                b.getPerformanceId().equalsIgnoreCase(booking.getPerformanceId()));
        
        if (exists) {
            return false;
        }
        bookings.add(booking);
        return true;
    }

    public boolean hasBookingsForPerformance(String performanceId) {
        return bookings.stream().anyMatch(b -> b.getPerformanceId().equalsIgnoreCase(performanceId));
    }
    
    public boolean hasBookingsForCustomer(String customerId) {
        return bookings.stream().anyMatch(b -> b.getCustomerId().equalsIgnoreCase(customerId));
    }
    
    public Map<String, Long> getStatistics() {
        return bookings.stream()
                .collect(Collectors.groupingBy(Booking::getPerformanceId, Collectors.counting()));
    }
}
