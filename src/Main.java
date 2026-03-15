import java.util.*;

// Custom Exception for invalid bookings
class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}


// Validator class to check booking input
class InvalidBookingValidator {

    private Set<String> validRoomTypes;

    InvalidBookingValidator(Set<String> validRoomTypes) {
        this.validRoomTypes = validRoomTypes;
    }

    // Validate room type
    public void validateRoomType(String roomType) throws InvalidBookingException {

        if (roomType == null || roomType.isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty.");
        }

        if (!validRoomTypes.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type selected: " + roomType);
        }
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

    public void allocateRoom(String roomType) throws InvalidBookingException {

        int available = inventory.getOrDefault(roomType, 0);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }

        inventory.put(roomType, available - 1);

        System.out.println("Room allocated successfully for type: " + roomType);
    }

    public void displayInventory() {

        System.out.println("\nCurrent Inventory State:");

        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms Left: " + inventory.get(type));
        }
    }
}


// Main class
public class Main {

    public static void main(String[] args) {

        InventoryService inventory = new InventoryService();

        Set<String> validTypes = new HashSet<>();
        validTypes.add("Single");
        validTypes.add("Double");
        validTypes.add("Suite");

        InvalidBookingValidator validator = new InvalidBookingValidator(validTypes);

        // Simulated booking inputs
        String[] bookingRequests = {"Single", "Deluxe", "Double", "Suite", "Suite"};

        for (String roomType : bookingRequests) {

            System.out.println("\nProcessing booking for room type: " + roomType);

            try {

                // Validate input
                validator.validateRoomType(roomType);

                // Allocate room
                inventory.allocateRoom(roomType);

            } catch (InvalidBookingException e) {

                System.out.println("Booking Failed: " + e.getMessage());
            }
        }

        // Display final inventory
        inventory.displayInventory();
    }
}