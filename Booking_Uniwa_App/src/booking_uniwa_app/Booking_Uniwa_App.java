/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package booking_uniwa_app;
// package com.uniwa.booking;

import booking_uniwa_app.service.*;
import booking_uniwa_app.ui.ConsoleUI;


/**
 * Η κύρια κλάση που ξεκινά την εφαρμογή.
 * Ο ρόλος της είναι να αρχικοποιήσει τις υπηρεσίες (services) και
 * τη διεπαφή χρήστη (UI), και στη συνέχεια να ξεκινήσει την εκτέλεση του UI.
 */
public class Booking_Uniwa_App {
   public static void main(String[] args) {
        // 1. Δημιουργούμε την υπηρεσία διαχείρισης αρχείων CSV.
        CsvService csvService = new CsvService();

        // 2. Δημιουργούμε τις υπόλοιπες υπηρεσίες, δίνοντάς τους την CsvService.
        PerformanceService performanceService = new PerformanceService(csvService);
        CustomerService customerService = new CustomerService(csvService);
        BookingService bookingService = new BookingService(csvService);

        // 3. Φορτώνουμε τα δεδομένα από τα αρχεία στην αρχή της εφαρμογής.
        performanceService.loadData();
        customerService.loadData();
        bookingService.loadData();

        // 4. Δημιουργούμε το UI και του "περνάμε" τις υπηρεσίες που χρειάζεται για να λειτουργήσει.
        ConsoleUI ui = new ConsoleUI(performanceService, customerService, bookingService);

        // 5. Ξεκινάμε την εκτέλεση της εφαρμογής. Το UI θα χειριστεί την αποθήκευση.
        ui.start();
        
        // 6. Όταν το UI τερματίσει, απλώς εμφανίζουμε το τελικό μήνυμα.
        System.out.println("Η εφαρμογή τερματίζεται.");
    }
}