package booking_uniwa_app.model;

/**
 * Αναπαριστά μια κράτηση εισιτηρίων.
 * Συνδέει έναν πελάτη (customerId) με μια παράσταση (performanceId).
 */
public class Booking {
    private String customerId;
    private String performanceId;

    public Booking(String customerId, String performanceId) {
        this.customerId = customerId;
        this.performanceId = performanceId;
    }

    public String getCustomerId() { return customerId; }
    public String getPerformanceId() { return performanceId; }

    @Override
    public String toString() {
        return String.format("Κράτηση για Πελάτη [%s] σε Παράσταση [%s]", customerId, performanceId);
    }
    
    public String toCsvString() {
        return String.join(";", customerId, performanceId);
    }
}
