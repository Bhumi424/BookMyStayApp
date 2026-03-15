import java.util.*;

// Reservation class
class Reservation {

    String reservationId;
    String guestName;
    String roomType;
    String roomId;

    Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String toString() {
        return reservationId + " | Guest: " + guestName +
                " | RoomType: " + roomType +
                " | RoomID: " + roomId;
    }
}


// Inventory Service
class InventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    public void incrementInventory(String roomType) {
        inventory.put(roomType, inventory.get(roomType) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");

        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms Available: " + inventory.get(type));
        }
    }
}


// Booking History
class BookingHistory {

    Map<String, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        reservations.put(r.reservationId, r);
    }

    public Reservation getReservation(String reservationId) {
        return reservations.get(reservationId);
    }

    public void removeReservation(String reservationId) {
        reservations.remove(reservationId);
    }

    public void displayBookings() {

        System.out.println("\nCurrent Confirmed Bookings:");

        for (Reservation r : reservations.values()) {
            System.out.println(r);
        }
    }
}


// Cancellation Service
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();
    private InventoryService inventory;
    private BookingHistory history;

    CancellationService(InventoryService inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void cancelReservation(String reservationId) {

        Reservation r = history.getReservation(reservationId);

        if (r == null) {
            System.out.println("Cancellation Failed: Reservation not found.");
            return;
        }

        // push room ID to rollback stack
        rollbackStack.push(r.roomId);

        // restore inventory
        inventory.incrementInventory(r.roomType);

        // remove booking
        history.removeReservation(reservationId);

        System.out.println("Reservation cancelled successfully.");
        System.out.println("Released Room ID: " + r.roomId);
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Released Rooms): " + rollbackStack);
    }
}


// Main class
public class Main {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingHistory history = new BookingHistory();

        // Assume confirmed bookings
        history.addReservation(new Reservation("R101", "Alice", "Single", "S1"));
        history.addReservation(new Reservation("R102", "Bob", "Double", "D1"));

        CancellationService cancelService =
                new CancellationService(inventory, history);

        history.displayBookings();

        // Guest cancels booking
        System.out.println("\nGuest requests cancellation for R101");
        cancelService.cancelReservation("R101");

        // Attempt invalid cancellation
        System.out.println("\nGuest requests cancellation for R999");
        cancelService.cancelReservation("R999");

        history.displayBookings();

        cancelService.showRollbackStack();

        inventory.displayInventory();
    }
}