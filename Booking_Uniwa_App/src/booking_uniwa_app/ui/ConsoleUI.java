package booking_uniwa_app.ui;

import booking_uniwa_app.model.*;
import booking_uniwa_app.service.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Διαχειρίζεται το User Interface (UI) της κονσόλας.
 */
public class ConsoleUI {
    private final PerformanceService performanceService;
    private final CustomerService customerService;
    private final BookingService bookingService;
    private final Scanner scanner;

    public ConsoleUI(PerformanceService ps, CustomerService cs, BookingService bs) {
        this.performanceService = ps;
        this.customerService = cs;
        this.bookingService = bs;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        clearConsole();
        art();
        System.out.println("Καλώς ήρθατε στην εφαρμογή διαχείρισης κρατήσεων!");
        sleep(2000);

        int choice;
        do {
            clearConsole();
            printMainMenu();
            choice = getIntegerInput();
            switch (choice) {
                case 1: managePerformances(); break;
                case 2: manageCustomers(); break;
                case 3: manageBookings(); break;
                case 4: showStatistics(); break;
                case 0: break;
                default: System.out.println("-> Μη έγκυρη επιλογή. Προσπαθήστε ξανά.");
            }
            // Η παύση γίνεται μετά από κάθε κύρια ενέργεια για να δει ο χρήστης το αποτέλεσμα
             if (choice != 0) pause();
        } while (choice != 0);
    }

    // --- Κύρια Μενού ---

    private void managePerformances() {
        int choice;
        do {
            clearConsole();
            printPerformancesMenu();
            choice = getIntegerInput();
            switch (choice) {
                case 1: addPerformance(); break;
                case 2: updatePerformance(); break;
                case 3: deletePerformance(); break;
                case 4: displayAllPerformances(); break;
                case 0: break;
                default: System.out.println("-> Μη έγκυρη επιλογή.");
            }
            if(choice != 0) pause();
        } while (choice != 0);
    }
    
    private void manageCustomers() {
        int choice;
        do {
            clearConsole();
            printCustomersMenu();
            choice = getIntegerInput();
            switch (choice) {
                case 1: addCustomer(); break;
                case 2: updateCustomer(); break;
                case 3: deleteCustomer(); break;
                case 4: displayAllCustomers(); break;
                case 0: break;
                default: System.out.println("-> Μη έγκυρη επιλογή.");
            }
            if(choice != 0) pause();
        } while (choice != 0);
    }
    
    private void manageBookings() {
        clearConsole();
        System.out.println("--- Δημιουργία Νέας Κράτησης ---");

        System.out.print("Εισάγετε τον κωδικό πελάτη: ");
        String customerId = scanner.nextLine();
        Optional<Customer> customerOpt = customerService.findById(customerId);
        if (customerOpt.isEmpty()) {
            System.out.println("-> Ο πελάτης με αυτόν τον κωδικό δεν βρέθηκε.");
            return;
        }

        System.out.print("Εισάγετε τον κωδικό παράστασης: ");
        String performanceId = scanner.nextLine();
        Optional<Performance> performanceOpt = performanceService.findById(performanceId);
        if (performanceOpt.isEmpty()) {
            System.out.println("-> Η παράσταση με αυτόν τον κωδικό δεν βρέθηκε.");
            return;
        }

        Booking booking = new Booking(customerId, performanceId);
        if (bookingService.addBooking(booking)) {
            System.out.println("-> Η κράτηση καταχωρήθηκε επιτυχώς!");
            System.out.printf("   Πελάτης: %s\n", customerOpt.get().getName());
            System.out.printf("   Παράσταση: %s\n", performanceOpt.get().getTitle());
            bookingService.saveData();
        } else {
            System.out.println("-> Αποτυχία: Ο πελάτης έχει ήδη κράτηση για αυτή την παράσταση.");
        }
    }
    
    // --- Λειτουργίες Παραστάσεων ---

    private void addPerformance() {
        clearConsole();
        System.out.println("--- Προσθήκη Νέας Παράστασης ---");
        System.out.println("1. Θεατρική Παράσταση");
        System.out.println("2. Μουσική Παράσταση");
        System.out.print("Επιλέξτε τύπο: ");
        int typeChoice = getIntegerInput();

        if (typeChoice != 1 && typeChoice != 2) {
            System.out.println("-> Άκυρη επιλογή τύπου.");
            return;
        }

        System.out.print("Κωδικός: "); String id = scanner.nextLine();
        if (performanceService.findById(id).isPresent()) {
            System.out.println("-> Αυτός ο κωδικός υπάρχει ήδη.");
            return;
        }

        System.out.print("Τίτλος: "); String title = scanner.nextLine();
        System.out.print("Χώρος: "); String venue = scanner.nextLine();
        System.out.print("Ημερομηνία (yyyy-MM-dd): ");
        LocalDate date;
        try {
            date = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
            System.out.println("-> Λάθος μορφή ημερομηνίας. Η προσθήκη ακυρώθηκε.");
            return;
        }
        
        Performance newPerformance;
        if (typeChoice == 1) {
            System.out.print("Πρωταγωνιστής: "); String actor = scanner.nextLine();
            newPerformance = new TheatricalPerformance(id, title, venue, date, actor);
        } else {
            System.out.print("Τραγουδιστής: "); String singer = scanner.nextLine();
            newPerformance = new MusicalPerformance(id, title, venue, date, singer);
        }

        if (performanceService.addPerformance(newPerformance)) {
            System.out.println("-> Η παράσταση προστέθηκε επιτυχώς.");
            performanceService.saveData(); // Άμεση αποθήκευση
        }
    }
    
    private void updatePerformance() {
        clearConsole();
        System.out.print("Εισάγετε τον κωδικό της παράστασης προς διόρθωση: ");
        String id = scanner.nextLine();
        Optional<Performance> performanceOpt = performanceService.findById(id);

        if (performanceOpt.isEmpty()) {
            System.out.println("-> Δεν βρέθηκε παράσταση με αυτόν τον κωδικό.");
            return;
        }

        Performance p = performanceOpt.get();
        System.out.println("Βρέθηκε: " + p);
        System.out.println("Αφήστε κενό για να μην αλλάξει μια τιμή.");

        System.out.printf("Τίτλος (%s): ", p.getTitle());
        String newTitle = scanner.nextLine();
        if (!newTitle.isBlank()) p.setTitle(newTitle);

        System.out.printf("Χώρος (%s): ", p.getVenue());
        String newVenue = scanner.nextLine();
        if (!newVenue.isBlank()) p.setVenue(newVenue);

        System.out.printf("Ημερομηνία (%s): ", p.getPerformanceDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        String newDateStr = scanner.nextLine();
        if (!newDateStr.isBlank()) {
            try {
                p.setPerformanceDate(LocalDate.parse(newDateStr, DateTimeFormatter.ISO_LOCAL_DATE));
            } catch (DateTimeParseException e) {
                System.out.println("-> Λάθος μορφή ημερομηνίας. Η τιμή δεν άλλαξε.");
            }
        }
        
        if (p instanceof TheatricalPerformance) {
            System.out.printf("Πρωταγωνιστής (%s): ", p.getPrincipalArtist());
            String newArtist = scanner.nextLine();
            if(!newArtist.isBlank()) p.setPrincipalArtist(newArtist);
        } else if (p instanceof MusicalPerformance) {
            System.out.printf("Τραγουδιστής (%s): ", p.getPrincipalArtist());
            String newArtist = scanner.nextLine();
            if(!newArtist.isBlank()) p.setPrincipalArtist(newArtist);
        }
        
        System.out.println("-> Η παράσταση ενημερώθηκε.");
        performanceService.saveData(); // Άμεση αποθήκευση
    }

    private void deletePerformance() {
        clearConsole();
        System.out.print("Εισάγετε τον κωδικό της παράστασης προς διαγραφή: ");
        String id = scanner.nextLine();
        
        if (bookingService.hasBookingsForPerformance(id)) {
            System.out.println("-> Δεν επιτρέπεται η διαγραφή. Υπάρχουν κρατήσεις για αυτή την παράσταση.");
            return;
        }
        
        if (performanceService.deletePerformance(id)) {
            System.out.println("-> Η παράσταση διαγράφηκε με επιτυχία.");
            performanceService.saveData(); // Άμεση αποθήκευση
        } else {
            System.out.println("-> Δεν βρέθηκε παράσταση με αυτόν τον κωδικό.");
        }
    }

    private void displayAllPerformances() {
        clearConsole();
        System.out.println("--- Λίστα Όλων των Παραστάσεων ---");
        List<Performance> all = performanceService.getAllPerformances();
        if (all.isEmpty()) {
            System.out.println("Δεν υπάρχουν καταχωρημένες παραστάσεις.");
        } else {
            all.forEach(System.out::println);
        }
    }

    // --- Λειτουργίες Πελατών ---

    private void addCustomer() {
         clearConsole();
         System.out.println("--- Προσθήκη Νέου Πελάτη ---");
         System.out.print("Κωδικός: "); String id = scanner.nextLine();
         if (customerService.findById(id).isPresent()) {
             System.out.println("-> Αυτός ο κωδικός πελάτη υπάρχει ήδη.");
             return;
         }
         System.out.print("Ονοματεπώνυμο: "); String name = scanner.nextLine();
         
         Customer newCustomer = new Customer(id, name);
         if (customerService.addCustomer(newCustomer)) {
             System.out.println("-> Ο πελάτης προστέθηκε επιτυχώς.");
             customerService.saveData(); // Άμεση αποθήκευση
         }
    }
    
    private void updateCustomer() {
        clearConsole();
        System.out.print("Εισάγετε τον κωδικό του πελάτη προς διόρθωση: ");
        String id = scanner.nextLine();
        Optional<Customer> customerOpt = customerService.findById(id);

        if (customerOpt.isEmpty()) {
            System.out.println("-> Δεν βρέθηκε πελάτης με αυτόν τον κωδικό.");
            return;
        }

        Customer c = customerOpt.get();
        System.out.println("Βρέθηκε: " + c);
        System.out.printf("Νέο Ονοματεπώνυμο (%s): ", c.getName());
        String newName = scanner.nextLine();
        if (!newName.isBlank()) {
            c.setName(newName);
            System.out.println("-> Ο πελάτης ενημερώθηκε.");
            customerService.saveData(); // Άμεση αποθήκευση
        } else {
            System.out.println("-> Δεν έγινε καμία αλλαγή.");
        }
    }

    private void deleteCustomer() {
        clearConsole();
        System.out.print("Εισάγετε τον κωδικό του πελάτη προς διαγραφή: ");
        String id = scanner.nextLine();
        
        if (bookingService.hasBookingsForCustomer(id)) {
            System.out.println("-> Δεν επιτρέπεται η διαγραφή. Υπάρχουν κρατήσεις για αυτόν τον πελάτη.");
            return;
        }
        
        if (customerService.deleteCustomer(id)) {
            System.out.println("-> Ο πελάτης διαγράφηκε με επιτυχία.");
            customerService.saveData(); // Άμεση αποθήκευση
        } else {
            System.out.println("-> Δεν βρέθηκε πελάτης με αυτόν τον κωδικό.");
        }
    }

    private void displayAllCustomers() {
        clearConsole();
        System.out.println("--- Λίστα Πελατών ---");
        List<Customer> all = customerService.getAllCustomers();
        if (all.isEmpty()) {
            System.out.println("Δεν υπάρχουν καταχωρημένοι πελάτες.");
        } else {
            all.forEach(System.out::println);
        }
    }
    
    // --- Στατιστικά ---
    
    private void showStatistics() {
        clearConsole();
        System.out.println("--- Στατιστικά Κρατήσεων ανά Παράσταση ---");
        Map<String, Long> stats = bookingService.getStatistics();

        if (stats.isEmpty()) {
            System.out.println("Δεν υπάρχουν κρατήσεις για να εμφανιστούν στατιστικά.");
            return;
        }
        
        stats.forEach((performanceId, count) -> {
            Optional<Performance> pOpt = performanceService.findById(performanceId);
            String title = "Άγνωστη Παράσταση (ID: " + performanceId + ")";
            if(pOpt.isPresent()){
                title = pOpt.get().getTitle();
            }
            System.out.printf("Παράσταση: %s - Κρατήσεις: %d\n", title, count);
        });
    }

    // --- Βοηθητικές Μέθοδοι ---

    private int getIntegerInput() {
        try {
            int value = scanner.nextInt();
            scanner.nextLine();
            return value;
        } catch (InputMismatchException e) {
            scanner.nextLine();
            return -1;
        }
    }
    
    /**
     * Μέθοδος για καθαρισμό κονσόλας.
     * Χρησιμοποιεί ProcessBuilder για Windows και ANSI escape codes για άλλα συστήματα.
     */
    private void clearConsole() {
        try {
            // Αν δεν υπάρχει κονσόλα (π.χ. μέσα σε ένα IDE), απλώς τυπώνουμε κενές γραμμές.
            if (System.console() == null) {
                for (int i = 0; i < 50; i++) System.out.println();
                return;
            }

            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb = os.contains("win")
                    ? new ProcessBuilder("cmd", "/c", "cls")
                    : new ProcessBuilder("clear"); // σε Linux/macOS/Unix

            pb.inheritIO().start().waitFor();
        } catch (IOException | InterruptedException ex) {
            // Επαναφέρουμε το interrupt flag αν το νήμα διακοπεί.
            if (ex instanceof InterruptedException) Thread.currentThread().interrupt();
            // Fallback: Χρήση ANSI escape codes που δουλεύουν στους περισσότερους emulators.
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }

    private void pause() {
        System.out.print("\n-> Πατήστε Enter για να συνεχίσετε...");
        scanner.nextLine();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }

    private void art() {
        System.out.println();
        System.out.println("  __  ___   _______      _____ "); sleep(100);
        System.out.println(" / / / / | / /  _/ |     / /   |"); sleep(100);
        System.out.println("/ / / /  |/ // / | | /| / / /| |"); sleep(100);
        System.out.println("/ /_/ / /|  // /  | |/ |/ / ___ |"); sleep(100);
        System.out.println("\\\\____/_/ |_/___/  |__/|__/_/  |_|"); sleep(100);
        System.out.println("              Booking App v1.0");
        System.out.println();
    }
    
    private void printMainMenu() {
        System.out.println("===== Κεντρικό Μενού =====");
        System.out.println("1. Διαχείριση Παραστάσεων");
        System.out.println("2. Διαχείριση Πελατών");
        System.out.println("3. Δημιουργία Κράτησης");
        System.out.println("4. Εμφάνιση Στατιστικών");
        System.out.println("0. Έξοδος");
        System.out.print("Επιλογή: ");
    }
    
    private void printPerformancesMenu() {
        System.out.println("--- Μενού Διαχείρισης Παραστάσεων ---");
        System.out.println("1. Προσθήκη Νέας Παράστασης");
        System.out.println("2. Διόρθωση Παράστασης");
        System.out.println("3. Διαγραφή Παράστασης");
        System.out.println("4. Εμφάνιση Όλων των Παραστάσεων");
        System.out.println("0. Επιστροφή στο Κεντρικό Μενού");
        System.out.print("Επιλογή: ");
    }

    private void printCustomersMenu() {
        System.out.println("--- Μενού Διαχείρισης Πελατών ---");
        System.out.println("1. Προσθήκη Νέου Πελάτη");
        System.out.println("2. Διόρθωση Πελάτη");
        System.out.println("3. Διαγραφή Πελάτη");
        System.out.println("4. Εμφάνιση Όλων των Πελατών");
        System.out.println("0. Επιστροφή στο Κεντρικό Μενού");
        System.out.print("Επιλογή: ");
    }
}
