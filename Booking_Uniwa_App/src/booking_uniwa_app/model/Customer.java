package booking_uniwa_app.model;


/**
 * Αναπαριστά έναν πελάτη της υπηρεσίας.
 * Μια απλή κλάση δεδομένων (POJO).
 */
public class Customer {
    private String id;
    private String name;

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return String.format("Κωδικός Πελάτη: %s, Όνομα: %s", id, name);
    }
    
    public String toCsvString() {
        return String.join(";", id, name);
    }
}
