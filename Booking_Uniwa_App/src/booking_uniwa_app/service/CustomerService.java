package booking_uniwa_app.service;

import booking_uniwa_app.model.Customer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Υπηρεσία διαχείρισης πελατών.
 */
public class CustomerService {
    private static final String FILENAME = "customers.csv";
    private final CsvService csvService;
    private List<Customer> customers;

    public CustomerService(CsvService csvService) {
        this.csvService = csvService;
        this.customers = new ArrayList<>();
    }

    public void loadData() {
        List<String[]> records = csvService.readCsv(FILENAME);
        for (String[] record : records) {
             if(record.length < 2) continue;
            customers.add(new Customer(record[0], record[1]));
        }
    }

    public void saveData() {
        List<String[]> data = new ArrayList<>();
        for (Customer customer : customers) {
            data.add(customer.toCsvString().split(";"));
        }
        csvService.writeCsv(FILENAME, data);
    }

    public List<Customer> getAllCustomers() {
        return customers;
    }

    public Optional<Customer> findById(String id) {
        return customers.stream().filter(c -> c.getId().equalsIgnoreCase(id)).findFirst();
    }

    public boolean addCustomer(Customer customer) {
        if (findById(customer.getId()).isPresent()) {
            return false;
        }
        customers.add(customer);
        return true;
    }

    public boolean deleteCustomer(String id) {
        return customers.removeIf(c -> c.getId().equalsIgnoreCase(id));
    }
}
