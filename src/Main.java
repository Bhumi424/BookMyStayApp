import java.util.*;

// Class representing a booking request
class BookingRequest {

    String customerName;
    String roomType;

    BookingRequest(String customerName, String roomType) {
        this.customerName = customerName;
        this.roomType = roomType;
    }
}

// Inventory Service
class InventoryService {

    private Map<String, Integer> roomInventory = new HashMap<>();

    public InventoryService() {
        roomInventory.put("Single", 3);
        roomInventory.put("Double", 2);
        roomInventory.put("Suite", 1);
    }

    public boolean checkAvailability(String roomType) {
        return roomInventory.getOrDefault(roomType, 0) > 0;
    }

    public void decrementRoom(String roomType) {
        roomInventory.put(roomType, roomInventory.get(roomType) - 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : roomInventory.keySet()) {
            System.out.println(type + " Rooms Left: " + roomInventory.get(type));
        }
    }
}

// Booking Service
class BookingService {

    private Queue<BookingRequest> bookingQueue = new LinkedList<>();
    private Map<String, Set<String>> allocatedRooms = new HashMap<>();
    private Set<String> usedRoomIds = new HashSet<>();
    private int roomCounter = 1;

    private InventoryService inventory;

    BookingService(InventoryService inventory) {
        this.inventory = inventory;
    }

    // Add booking request
    public void addBookingRequest(String customerName, String roomType) {
        bookingQueue.add(new BookingRequest(customerName, roomType));
        System.out.println("Booking request added for " + customerName + " (" + roomType + ")");
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        String roomId;

        do {
            roomId = roomType.substring(0, 1).toUpperCase() + roomCounter++;
        } while (usedRoomIds.contains(roomId));

        usedRoomIds.add(roomId);
        return roomId;
    }

    // Process bookings
    public void processBookings() {

        while (!bookingQueue.isEmpty()) {

            BookingRequest request = bookingQueue.poll();

            System.out.println("\nProcessing request for " + request.customerName);

            if (inventory.checkAvailability(request.roomType)) {

                String roomId = generateRoomId(request.roomType);

                allocatedRooms
                        .computeIfAbsent(request.roomType, k -> new HashSet<>())
                        .add(roomId);

                inventory.decrementRoom(request.roomType);

                System.out.println("Reservation Confirmed!");
                System.out.println("Customer: " + request.customerName);
                System.out.println("Room Type: " + request.roomType);
                System.out.println("Allocated Room ID: " + roomId);

            } else {
                System.out.println("Sorry! No " + request.roomType + " rooms available.");
            }
        }
    }

    // Display allocated rooms
    public void showAllocatedRooms() {

        System.out.println("\nAllocated Rooms:");

        for (String type : allocatedRooms.keySet()) {
            System.out.println(type + " -> " + allocatedRooms.get(type));
        }
    }
}

// Main Class
public class Main {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();
        BookingService bookingService = new BookingService(inventory);

        // Add booking requests (FIFO Queue)
        bookingService.addBookingRequest("Alice", "Single");
        bookingService.addBookingRequest("Bob", "Double");
        bookingService.addBookingRequest("Charlie", "Single");
        bookingService.addBookingRequest("David", "Suite");

        // Process bookings
        bookingService.processBookings();

        // Show allocated rooms
        bookingService.showAllocatedRooms();

        // Show updated inventory
        inventory.displayInventory();
    }
}