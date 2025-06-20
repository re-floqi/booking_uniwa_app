package booking_uniwa_app.model;

import java.time.LocalDate;

/**
 * Αναπαριστά μια μουσική παράσταση.
 * Κληρονομεί όλα τα κοινά πεδία από την κλάση Performance και προσθέτει τον τραγουδιστή.
 */
public class MusicalPerformance extends Performance {
    private String singer;

    public MusicalPerformance(String id, String title, String venue, LocalDate performanceDate, String singer) {
        super(id, title, venue, performanceDate);
        this.singer = singer;
    }

    @Override
    public String getPrincipalArtist() {
        return this.singer;
    }
    
    @Override
    public void setPrincipalArtist(String artist) {
        this.singer = artist;
    }
    
    @Override
    public String getType() {
        return "MUSIC";
    }

    @Override
    public String toString() {
        return "[ΜΟΥΣΙΚΗ] " + super.toString() + ", Τραγουδιστής: " + singer;
    }

    @Override
    public String toCsvString() {
        return getType() + ";" + super.toCsvString() + ";" + getPrincipalArtist();
    }
}
