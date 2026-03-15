import java.util.*;

// Booking Request
class BookingRequest {

    String guestName;
    String roomType;

    BookingRequest(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}


// Inventory Service (shared resource)
class InventoryService {

    private Map<String, Integer> inventory = new HashMap<>();

    InventoryService() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Suite", 1);
    }

    // synchronized critical section
    public synchronized boolean allocateRoom(String roomType, String guest) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available <= 0) {
            System.out.println("Booking failed for " + guest +
                    " (No " + roomType + " rooms left)");
            return false;
        }

        inventory.put(roomType, available - 1);

        System.out.println("Room allocated to " + guest +
                " | Room Type: " + roomType +
                " | Remaining: " + (available - 1));

        return true;
    }

    public void displayInventory() {

        System.out.println("\nFinal Inventory State:");

        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms Left: " + inventory.get(type));
        }
    }
}


// Concurrent Booking Processor
class BookingProcessor implements Runnable {

    private Queue<BookingRequest> bookingQueue;
    private InventoryService inventory;

    BookingProcessor(Queue<BookingRequest> bookingQueue,
                     InventoryService inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {

            BookingRequest request;

            // synchronized queue access
            synchronized (bookingQueue) {

                if (bookingQueue.isEmpty()) {
                    return;
                }

                request = bookingQueue.poll();
            }

            if (request != null) {
                inventory.allocateRoom(request.roomType, request.guestName);
            }
        }
    }
}


// Main Class
public class Main {

    public static void main(String[] args) {

        Queue<BookingRequest> bookingQueue = new LinkedList<>();
        InventoryService inventory = new InventoryService();

        // Multiple booking requests
        bookingQueue.add(new BookingRequest("Alice", "Single"));
        bookingQueue.add(new BookingRequest("Bob", "Single"));
        bookingQueue.add(new BookingRequest("Charlie", "Single"));
        bookingQueue.add(new BookingRequest("David", "Double"));
        bookingQueue.add(new BookingRequest("Eva", "Suite"));

        // Simulate concurrent guests using threads
        Thread t1 = new Thread(new BookingProcessor(bookingQueue, inventory));
        Thread t2 = new Thread(new BookingProcessor(bookingQueue, inventory));
        Thread t3 = new Thread(new BookingProcessor(bookingQueue, inventory));

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        inventory.displayInventory();
    }
}