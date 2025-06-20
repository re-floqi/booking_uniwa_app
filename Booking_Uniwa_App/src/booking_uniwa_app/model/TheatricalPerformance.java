package booking_uniwa_app.model;

import java.time.LocalDate;

/**
 * Αναπαριστά μια θεατρική παράσταση.
 * Κληρονομεί όλα τα κοινά πεδία από την κλάση Performance και προσθέτει τον πρωταγωνιστή.
 */
public class TheatricalPerformance extends Performance {
    private String leadActor;

    public TheatricalPerformance(String id, String title, String venue, LocalDate performanceDate, String leadActor) {
        super(id, title, venue, performanceDate);
        this.leadActor = leadActor;
    }

    @Override
    public String getPrincipalArtist() {
        return this.leadActor;
    }
    
    @Override
    public void setPrincipalArtist(String artist) {
        this.leadActor = artist;
    }
    
    @Override
    public String getType() {
        return "THEATER";
    }

    @Override
    public String toString() {
        return "[ΘΕΑΤΡΙΚΗ] " + super.toString() + ", Πρωταγωνιστής: " + leadActor;
    }

    @Override
    public String toCsvString() {
        return getType() + ";" + super.toCsvString() + ";" + getPrincipalArtist();
    }
}
