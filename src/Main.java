import java.util.*;

// Class representing an Add-On Service
class Service {

    String serviceName;
    double cost;

    Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String toString() {
        return serviceName + " ($" + cost + ")";
    }
}

// Manager for Add-On Services
class AddOnServiceManager {

    // Map reservationID -> List of Services
    private Map<String, List<Service>> reservationServices = new HashMap<>();

    // Add service to reservation
    public void addService(String reservationId, Service service) {

        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Service added to Reservation " + reservationId + ": " + service.serviceName);
    }

    // Display services for reservation
    public void showServices(String reservationId) {

        List<Service> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services for reservation " + reservationId);
            return;
        }

        System.out.println("\nServices for Reservation " + reservationId + ":");

        for (Service s : services) {
            System.out.println("- " + s.serviceName + " : $" + s.cost);
        }
    }

    // Calculate total add-on cost
    public double calculateTotalCost(String reservationId) {

        List<Service> services = reservationServices.get(reservationId);

        if (services == null) return 0;

        double total = 0;

        for (Service s : services) {
            total += s.cost;
        }

        return total;
    }
}

// Main Class
public class Main {

    public static void main(String[] args) {

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Assume reservation already confirmed in UC6
        String reservationId1 = "R101";
        String reservationId2 = "R102";

        // Guest selects services
        serviceManager.addService(reservationId1, new Service("Breakfast", 20));
        serviceManager.addService(reservationId1, new Service("Airport Pickup", 40));
        serviceManager.addService(reservationId2, new Service("Spa Access", 60));

        // Display services
        serviceManager.showServices(reservationId1);
        serviceManager.showServices(reservationId2);

        // Calculate additional cost
        double total1 = serviceManager.calculateTotalCost(reservationId1);
        double total2 = serviceManager.calculateTotalCost(reservationId2);

        System.out.println("\nTotal Add-On Cost for " + reservationId1 + ": $" + total1);
        System.out.println("Total Add-On Cost for " + reservationId2 + ": $" + total2);
    }
}