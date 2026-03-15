import java.util.*;

// Class representing a confirmed reservation
class Reservation {

    String reservationId;
    String guestName;
    String roomType;

    Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return reservationId + " | Guest: " + guestName + " | Room Type: " + roomType;
    }
}


// Booking History - stores confirmed reservations
class BookingHistory {

    private List<Reservation> reservations = new ArrayList<>();

    // Add reservation to history
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
        System.out.println("Reservation stored in history: " + reservation.reservationId);
    }

    // Retrieve booking history
    public List<Reservation> getReservations() {
        return reservations;
    }
}


// Reporting Service
class BookingReportService {

    // Display all bookings
    public void displayAllBookings(List<Reservation> reservations) {

        System.out.println("\nBooking History Report");

        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            roomTypeCount.put(
                    r.roomType,
                    roomTypeCount.getOrDefault(r.roomType, 0) + 1
            );
        }

        System.out.println("\nBooking Summary Report");

        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + " Rooms Booked: " + roomTypeCount.get(type));
        }

        System.out.println("Total Reservations: " + reservations.size());
    }
}


// Main Class
public class Main {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Assume these bookings were confirmed earlier
        history.addReservation(new Reservation("R101", "Alice", "Single"));
        history.addReservation(new Reservation("R102", "Bob", "Double"));
        history.addReservation(new Reservation("R103", "Charlie", "Single"));
        history.addReservation(new Reservation("R104", "David", "Suite"));

        // Admin views booking history
        reportService.displayAllBookings(history.getReservations());

        // Admin generates summary report
        reportService.generateSummary(history.getReservations());
    }
}