package booking_uniwa_app.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Αφηρημένη βασική κλάση που αναπαριστά μια γενική καλλιτεχνική παράσταση.
 * Λειτουργεί ως "καλούπι" για τις πιο εξειδικευμένες κλάσεις παραστάσεων.
 * Δεν μπορούμε να δημιουργήσουμε αντικείμενο αυτής της κλάσης απευθείας.
 */
public abstract class Performance {
    protected String id;
    protected String title;
    protected String venue;
    protected LocalDate performanceDate;

    public Performance(String id, String title, String venue, LocalDate performanceDate) {
        this.id = id;
        this.title = title;
        this.venue = venue;
        this.performanceDate = performanceDate;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getVenue() { return venue; }
    public LocalDate getPerformanceDate() { return performanceDate; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setVenue(String venue) { this.venue = venue; }
    public void setPerformanceDate(LocalDate performanceDate) { this.performanceDate = performanceDate; }

    /**
     * Αφηρημένη μέθοδος. Κάθε υποκλάση είναι *υποχρεωμένη* να την υλοποιήσει.
     * @return Το όνομα του κύριου καλλιτέχνη.
     */
    public abstract String getPrincipalArtist();
    public abstract void setPrincipalArtist(String artist);
    
    /**
     * Αφηρημένη μέθοδος. Κάθε υποκλάση είναι *υποχρεωμένη* να την υλοποιήσει.
     * @return Ο τύπος της παράστασης (π.χ. "THEATER").
     */
    public abstract String getType();

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("Κωδικός: %s, Τίτλος: %s, Χώρος: %s, Ημερομηνία: %s",
                id, title, venue, performanceDate.format(formatter));
    }

    public String toCsvString() {
        // Η μορφοποίηση της ημερομηνίας σε ISO_LOCAL_DATE (π.χ. 2025-10-20) είναι ιδανική για CSV
        // γιατί αποφεύγει προβλήματα με διαφορετικά τοπικά format.
        return String.join(";", id, title, venue, performanceDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
