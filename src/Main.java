// Version 4.1
// Use Case 4: Room Search & Availability Check

import java.util.HashMap;
import java.util.Map;

// Abstract Room class
abstract class Room {

    protected String roomType;
    protected int beds;
    protected double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Beds      : " + beds);
        System.out.println("Price     : ₹" + price);
    }
}

// Single Room
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 2000);
    }
}

// Double Room
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 3500);
    }
}

// Suite Room
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 6000);
    }
}

// Centralized Inventory
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {

        inventory = new HashMap<>();

        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 0); // unavailable example
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }
}

// Search Service (Read-Only)
class RoomSearchService {

    private RoomInventory inventory;

    public RoomSearchService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void searchAvailableRooms(Room[] rooms) {

        System.out.println("\n===== Available Rooms =====");

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            // show only rooms with availability
            if (available > 0) {

                room.displayDetails();
                System.out.println("Available : " + available);
                System.out.println("-------------------------");
            }
        }
    }
}

// Main Class
public class Main {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay – Room Search =====");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Room domain objects
        Room[] rooms = {
                new SingleRoom(),
                new DoubleRoom(),
                new SuiteRoom()
        };

        // Search service
        RoomSearchService searchService = new RoomSearchService(inventory);

        // Guest searches for rooms
        searchService.searchAvailableRooms(rooms);

        System.out.println("\nSearch completed (Inventory unchanged)");
    }
}