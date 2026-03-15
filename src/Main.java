import java.io.*;
import java.util.*;

// Reservation class
class Reservation implements Serializable {

    String reservationId;
    String guestName;
    String roomType;

    Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return reservationId + " | Guest: " + guestName + " | RoomType: " + roomType;
    }
}


// System State (Inventory + Booking History)
class SystemState implements Serializable {

    Map<String, Integer> inventory;
    List<Reservation> bookingHistory;

    SystemState() {

        inventory = new HashMap<>();
        bookingHistory = new ArrayList<>();

        // default inventory
        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }
}


// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    // Save state to file
    public static void saveState(SystemState state) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(state);
            System.out.println("\nSystem state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    public static SystemState loadState() {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            SystemState state = (SystemState) in.readObject();
            System.out.println("System state restored from file.");
            return state;

        } catch (Exception e) {

            System.out.println("No saved state found. Starting fresh.");
            return new SystemState();
        }
    }
}


// Main class
public class Main {

    public static void main(String[] args) {

        // System startup: restore state
        SystemState state = PersistenceService.loadState();

        // Display current inventory
        System.out.println("\nCurrent Inventory:");
        for (String type : state.inventory.keySet()) {
            System.out.println(type + " Rooms: " + state.inventory.get(type));
        }

        // Simulate booking confirmation
        Reservation r1 = new Reservation("R201", "Alice", "Single");
        state.bookingHistory.add(r1);

        // Update inventory
        int available = state.inventory.get("Single");
        state.inventory.put("Single", available - 1);

        System.out.println("\nNew reservation added:");
        System.out.println(r1);

        // Display booking history
        System.out.println("\nBooking History:");
        for (Reservation r : state.bookingHistory) {
            System.out.println(r);
        }

        // Save state before shutdown
        PersistenceService.saveState(state);
    }
}