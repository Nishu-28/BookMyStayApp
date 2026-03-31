import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

/**
 * ============================================================
 * ABSTRACT CLASS - Room
 * ============================================================
 *
 * Use Case 2: Basic Room Types & Static Availability
 *
 * Description:
 * This abstract class represents a generic hotel room.
 *
 * It models attributes that are intrinsic to a room type
 * and remain constant regardless of availability.
 *
 * Inventory-related concerns are intentionally excluded.
 *
 * @version 2.0
 */
abstract class Room {

    /** Number of beds available in the room. */
    protected int numberOfBeds;

    /** Total size of the room in square feet. */
    protected int squareFeet;

    /** Price charged per night for this room type. */
    protected double pricePerNight;

    /**
     * Constructor used by child classes to
     * initialize common room attributes.
     *
     * @param numberOfBeds number of beds in the room
     * @param squareFeet total room size
     * @param pricePerNight cost per night
     */
    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    /**
     * Displays room details including beds, size, and price per night.
     */
    public void displayRoomDetails() {
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

/**
 * ============================================================
 * CLASS - SingleRoom
 * ============================================================
 *
 * Represents a single room in the hotel.
 *
 * @version 2.0
 */
class SingleRoom extends Room {

    /**
     * Initializes a SingleRoom with
     * predefined attributes.
     */
    public SingleRoom() { super(1, 250, 1500.0); }
}

/**
 * ============================================================
 * CLASS - DoubleRoom
 * ============================================================
 *
 * Represents a double room in the hotel.
 *
 * @version 2.0
 */
class DoubleRoom extends Room {

    /**
     * Initializes a DoubleRoom with
     * predefined attributes.
     */
    public DoubleRoom() { super(2, 400, 2500.0); }
}

/**
 * ============================================================
 * CLASS - SuiteRoom
 * ============================================================
 *
 * Represents a suite room in the hotel.
 *
 * @version 2.0
 */
class SuiteRoom extends Room {

    /**
     * Initializes a SuiteRoom with
     * predefined attributes.
     */
    public SuiteRoom() { super(3, 750, 5000.0); }
}

/**
 * ============================================================
 * CLASS - RoomInventory
 * ============================================================
 *
 * Use Case 3: Centralized Room Inventory Management
 *
 * Description:
 * This class acts as the single source of truth
 * for room availability in the hotel.
 *
 * Room pricing and characteristics are obtained
 * from Room objects, not duplicated here.
 *
 * This avoids multiple sources of truth and
 * keeps responsibilities clearly separated.
 *
 * @version 3.0
 */
class RoomInventory {

    /**
     * Stores available room count for each room type.
     *
     * Key   -> Room type name
     * Value -> Available room count
     */
    private Map<String, Integer> roomAvailability;

    /**
     * Constructor initializes the inventory
     * with default availability values.
     */
    public RoomInventory() {
        roomAvailability = new HashMap<>();
        initializeInventory();
    }

    /**
     * Initializes room availability data.
     *
     * This method centralizes inventory setup
     * instead of using scattered variables.
     */
    private void initializeInventory() {
        roomAvailability.put("Single", 5);
        roomAvailability.put("Double", 3);
        roomAvailability.put("Suite", 2);
    }

    /**
     * Returns the current availability map.
     *
     * @return map of room type to available count
     */
    public Map<String, Integer> getRoomAvailability() { return roomAvailability; }

    /**
     * Updates availability for a specific room type.
     *
     * @param roomType the room type to update
     * @param count new availability count
     */
    public void updateAvailability(String roomType, int count) { roomAvailability.put(roomType, count); }
}

/**
 * ============================================================
 * CLASS - RoomSearchService
 * ============================================================
 *
 * Use Case 4: Room Search & Availability Check
 *
 * Description:
 * This class provides search functionality
 * for guests to view available rooms.
 *
 * It reads room availability from inventory
 * and room details from Room objects.
 *
 * No inventory mutation or booking logic
 * is performed in this class.
 *
 * @version 4.0
 */
class RoomSearchService {

    /**
     * Displays available rooms along with
     * their details and pricing.
     *
     * This method performs read-only access
     * to inventory and room data.
     *
     * @param inventory centralized room inventory
     * @param singleRoom single room definition
     * @param doubleRoom double room definition
     * @param suiteRoom suite room definition
     */
    public void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        // Check and display Single Room availability
        if (availability.get("Single") > 0) {
            System.out.println("Single Room:");
            singleRoom.displayRoomDetails();
            System.out.println("Available: " + availability.get("Single"));
            System.out.println();
        }

        // Check and display Double Room availability
        if (availability.get("Double") > 0) {
            System.out.println("Double Room:");
            doubleRoom.displayRoomDetails();
            System.out.println("Available: " + availability.get("Double"));
            System.out.println();
        }

        // Check and display Suite Room availability
        if (availability.get("Suite") > 0) {
            System.out.println("Suite Room:");
            suiteRoom.displayRoomDetails();
            System.out.println("Available: " + availability.get("Suite"));
            System.out.println();
        }
    }
}

/**
 * ============================================================
 * CLASS - Reservation
 * ============================================================
 *
 * Use Case 5: Booking Request (FIFO)
 *
 * Description:
 * This class represents a booking request
 * made by a guest.
 *
 * At this stage, a reservation only captures
 * intent, not confirmation or room allocation.
 *
 * @version 5.0
 */
class Reservation {

    /** Name of the guest making the booking. */
    private String guestName;

    /** Requested room type. */
    private String roomType;

    /**
     * Creates a new booking request.
     *
     * @param guestName name of the guest
     * @param roomType requested room type
     */
    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    /** @return guest name */
    public String getGuestName() { return guestName; }

    /** @return requested room type */
    public String getRoomType() { return roomType; }
}

/**
 * ============================================================
 * CLASS - BookingRequestQueue
 * ============================================================
 *
 * Use Case 5: Booking Request (FIFO)
 *
 * Description:
 * This class manages booking requests
 * using a queue to ensure fair allocation.
 *
 * Requests are processed strictly
 * in the order they are received.
 *
 * @version 5.0
 */
class BookingRequestQueue {

    /** Queue that stores booking requests. */
    private Queue<Reservation> requestQueue;

    /** Initializes an empty booking queue. */
    public BookingRequestQueue() { requestQueue = new LinkedList<>(); }

    /**
     * Adds a booking request to the queue.
     *
     * @param reservation booking request
     */
    public void addRequest(Reservation reservation) { requestQueue.offer(reservation); }

    /**
     * Retrieves and removes the next
     * booking request from the queue.
     *
     * @return next reservation request
     */
    public Reservation getNextRequest() { return requestQueue.poll(); }

    /**
     * Checks whether there are
     * pending booking requests.
     *
     * @return true if queue is not empty
     */
    public boolean hasPendingRequests() { return !requestQueue.isEmpty(); }
}

/**
 * ============================================================
 * CLASS - RoomAllocationService
 * ============================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * Description:
 * This class is responsible for confirming
 * booking requests and assigning rooms.
 *
 * It ensures:
 * - Each room ID is unique
 * - Inventory is updated immediately
 * - No room is double-booked
 *
 * @version 6.0
 */
class RoomAllocationService {

    /**
     * Stores all allocated room IDs to
     * prevent duplicate assignments.
     */
    private Set<String> allocatedRoomIds;

    /**
     * Stores assigned room IDs by room type.
     *
     * Key   -> Room type
     * Value -> Set of assigned room IDs
     */
    private Map<String, Set<String>> assignedRoomsByType;

    /**
     * Initializes allocation tracking structures.
     */
    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    /**
     * Confirms a booking request by assigning
     * a unique room ID and updating inventory.
     *
     * @param reservation booking request
     * @param inventory centralized room inventory
     */
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String roomType = reservation.getRoomType();
        Map<String, Integer> availability = inventory.getRoomAvailability();

        if (availability.getOrDefault(roomType, 0) > 0) {
            String roomId = generateRoomId(roomType);
            allocatedRoomIds.add(roomId);
            assignedRoomsByType.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);
            inventory.updateAvailability(roomType, availability.get(roomType) - 1);
            System.out.println("Booking confirmed for Guest: " + reservation.getGuestName() + ", Room ID: " + roomId);
        } else {
            System.out.println("No rooms available for type: " + roomType);
        }
    }

    /**
     * Generates a unique room ID
     * for the given room type.
     *
     * @param roomType type of room
     * @return unique room ID
     */
    private String generateRoomId(String roomType) {
        int count = assignedRoomsByType.containsKey(roomType) ? assignedRoomsByType.get(roomType).size() : 0;
        return roomType + "-" + (count + 1);
    }
}

/**
 * ============================================================
 * CLASS - AddOnService
 * ============================================================
 *
 * Use Case 7: Add-On Service Selection
 *
 * Description:
 * This class represents an optional service
 * that can be added to a confirmed reservation.
 *
 * Examples:
 * - Breakfast
 * - Spa
 * - Airport Pickup
 *
 * @version 7.0
 */
class AddOnService {

    /**
     * Name of the service.
     */
    private String serviceName;

    /**
     * Cost of the service.
     */
    private double cost;

    /**
     * Creates a new add-on service.
     *
     * @param serviceName name of the service
     * @param cost cost of the service
     */
    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    /**
     * @return service name
     */
    public String getServiceName() { return serviceName; }

    /**
     * @return service cost
     */
    public double getCost() { return cost; }
}

/**
 * ============================================================
 * CLASS - AddOnServiceManager
 * ============================================================
 *
 * Use Case 7: Add-On Service Selection
 *
 * Description:
 * This class manages optional services
 * associated with confirmed reservations.
 *
 * It supports attaching multiple services
 * to a single reservation.
 *
 * @version 7.0
 */
class AddOnServiceManager {

    /**
     * Maps reservation ID to selected services.
     *
     * Key   -> Reservation ID
     * Value -> List of selected services
     */
    private Map<String, List<AddOnService>> servicesByReservation;

    /**
     * Initializes the service manager.
     */
    public AddOnServiceManager() { servicesByReservation = new HashMap<>(); }

    /**
     * Attaches a service to a reservation.
     *
     * @param reservationId confirmed reservation ID
     * @param service add-on service
     */
    public void addService(String reservationId, AddOnService service) {
        servicesByReservation.computeIfAbsent(reservationId, k -> new ArrayList<>()).add(service);
    }

    /**
     * Calculates total add-on cost
     * for a reservation.
     *
     * @param reservationId reservation ID
     * @return total service cost
     */
    public double calculateTotalServiceCost(String reservationId) {
        List<AddOnService> services = servicesByReservation.get(reservationId);
        if (services == null) return 0.0;
        double total = 0.0;
        for (AddOnService service : services) {
            total += service.getCost();
        }
        return total;
    }
}

/**
 * ============================================================
 * CLASS - BookingHistory
 * ============================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class maintains a record of
 * confirmed reservations.
 *
 * It provides ordered storage for
 * historical and reporting purposes.
 *
 * @version 8.0
 */
class BookingHistory {

    /**
     * List that stores confirmed reservations.
     */
    private List<Reservation> confirmedReservations;

    /**
     * Initializes an empty booking history.
     */
    public BookingHistory() { confirmedReservations = new ArrayList<>(); }

    /**
     * Adds a confirmed reservation
     * to booking history.
     *
     * @param reservation confirmed booking
     */
    public void addReservation(Reservation reservation) { confirmedReservations.add(reservation); }

    /**
     * Returns all confirmed reservations.
     *
     * @return list of reservations
     */
    public List<Reservation> getConfirmedReservations() { return confirmedReservations; }
}

/**
 * ============================================================
 * CLASS - BookingReportService
 * ============================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * Description:
 * This class generates reports
 * from booking history data.
 *
 * Reporting logic is separated
 * from data storage.
 *
 * @version 8.0
 */
class BookingReportService {

    /**
     * Displays a summary report
     * of all confirmed bookings.
     *
     * @param history booking history
     */
    public void generateReport(BookingHistory history) {
        System.out.println("Booking History Report");
        for (Reservation reservation : history.getConfirmedReservations()) {
            System.out.println("Guest: " + reservation.getGuestName() + ", Room Type: " + reservation.getRoomType());
        }
    }
}

/**
 * ============================================================
 * CLASS - InvalidBookingException
 * ============================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * This custom exception represents
 * invalid booking scenarios in the system.
 *
 * Using a domain-specific exception
 * makes error handling clearer and safer.
 *
 * @version 9.0
 */
class InvalidBookingException extends Exception {

    /**
     * Creates an exception with
     * a descriptive error message.
     *
     * @param message error description
     */
    public InvalidBookingException(String message) { super(message); }
}

/**
 * ============================================================
 * CLASS - ReservationValidator
 * ============================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * Description:
 * This class is responsible for validating
 * booking requests before they are processed.
 *
 * All validation rules are centralized
 * to avoid duplication and inconsistency.
 *
 * @version 9.0
 */
class ReservationValidator {

    /**
     * Validates booking input provided by the user.
     *
     * @param guestName name of the guest
     * @param roomType requested room type
     * @param inventory centralized inventory
     * @throws InvalidBookingException if validation fails
     */
    public void validate(
            String guestName,
            String roomType,
            RoomInventory inventory
    ) throws InvalidBookingException {
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }
        if (!roomType.equals("Single") && !roomType.equals("Double") && !roomType.equals("Suite")) {
            throw new InvalidBookingException("Invalid room type selected.");
        }
        int available = inventory.getRoomAvailability().getOrDefault(roomType, 0);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + roomType);
        }
    }
}

/**
 * ============================================================
 * CLASS - CancellationService
 * ============================================================
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * Description:
 * This class is responsible for handling
 * booking cancellations.
 *
 * It ensures that:
 * - Cancelled room IDs are tracked
 * - Inventory is restored correctly
 * - Invalid cancellations are prevented
 *
 * A stack is used to model rollback behavior.
 *
 * @version 10.0
 */
class CancellationService {

    /** Stack that stores recently released room IDs. */
    private Stack<String> releasedRoomIds;

    /** Maps reservation ID to room type. */
    private Map<String, String> reservationRoomTypeMap;

    /** Initializes cancellation tracking structures. */
    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    /**
     * Registers a confirmed booking.
     *
     * This method simulates storing confirmation
     * data that will later be required for cancellation.
     *
     * @param reservationId confirmed reservation ID
     * @param roomType allocated room type
     */
    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    /**
     * Cancels a confirmed booking and
     * restores inventory safely.
     *
     * @param reservationId reservation to cancel
     * @param inventory centralized room inventory
     */
    public void cancelBooking(String reservationId, RoomInventory inventory) {
        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Cancellation failed: Reservation ID not found.");
            return;
        }

        String roomType = reservationRoomTypeMap.get(reservationId);
        releasedRoomIds.push(reservationId);
        reservationRoomTypeMap.remove(reservationId);

        int currentAvailability = inventory.getRoomAvailability().getOrDefault(roomType, 0);
        inventory.updateAvailability(roomType, currentAvailability + 1);

        System.out.println("Booking cancelled successfully. Inventory restored for room type: " + roomType);
    }

    /**
     * Displays recently cancelled reservations.
     *
     * This method helps visualize rollback order.
     */
    public void showRollbackHistory() {
        System.out.println("Rollback History (Most Recent First):");
        for (int i = releasedRoomIds.size() - 1; i >= 0; i--) {
            System.out.println("Released Reservation ID: " + releasedRoomIds.get(i));
        }
    }
}

/**
 * ============================================================
 * CLASS - ConcurrentBookingProcessor
 * ============================================================
 *
 * Use Case 11: Concurrent Booking Simulation
 *
 * Description:
 * This class represents a booking processor
 * that can be executed by multiple threads.
 *
 * It demonstrates how shared resources
 * such as booking queues and inventory
 * must be accessed in a thread-safe manner.
 *
 * @version 11.0
 */
class ConcurrentBookingProcessor implements Runnable {

    /**
     * Shared booking request queue.
     */
    private BookingRequestQueue bookingQueue;

    /**
     * Shared room inventory.
     */
    private RoomInventory inventory;

    /**
     * Shared room allocation service.
     */
    private RoomAllocationService allocationService;

    /**
     * Creates a new booking processor.
     *
     * @param bookingQueue shared booking queue
     * @param inventory shared inventory
     * @param allocationService shared allocation service
     */
    public ConcurrentBookingProcessor(
            BookingRequestQueue bookingQueue,
            RoomInventory inventory,
            RoomAllocationService allocationService
    ) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    /**
     * Executes booking processing logic.
     *
     * This method is called when the thread starts.
     */
    @Override
    public void run() {

        while (true) {
            Reservation reservation;

            /*
             * Synchronize on the booking queue to ensure
             * that only one thread can retrieve a request
             * at a time.
             */
            synchronized (bookingQueue) {
                if (!bookingQueue.hasPendingRequests()) {
                    break;
                }
                reservation = bookingQueue.getNextRequest();
            }

            /*
             * Allocation also mutates shared inventory.
             * Synchronization ensures atomic allocation.
             */
            synchronized (inventory) {
                allocationService.allocateRoom(reservation, inventory);
            }
        }
    }
}

/**
 * BookMyStayApp
 *
 * <p>This class serves as the entry point for the Hotel Booking Management System.
 * It demonstrates concurrent booking simulation with thread-safe
 * access to shared booking queue and inventory resources.</p>
 *
 * @author Nishanth
 * @version 11.1
 */
public class BookMyStayApp {

    /**
     * The main method is the starting point of the Java application.
     *
     * @param args Command-line arguments passed during program execution
     */
    public static void main(String[] args) {

        // Display application header
        System.out.println("Concurrent Booking Simulation");

        // Initialize shared components
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomAllocationService allocationService = new RoomAllocationService();

        // Create booking requests
        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Double"));
        bookingQueue.addRequest(new Reservation("Kural", "Suite"));
        bookingQueue.addRequest(new Reservation("Subha", "Single"));

        // Create booking processor tasks
        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(
                        bookingQueue, inventory, allocationService
                )
        );

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(
                        bookingQueue, inventory, allocationService
                )
        );

        // Start concurrent processing
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        // Display remaining inventory
        System.out.println();
        System.out.println("Remaining Inventory:");
        Map<String, Integer> availability = inventory.getRoomAvailability();
        System.out.println("Single: " + availability.get("Single"));
        System.out.println("Double: " + availability.get("Double"));
        System.out.println("Suite: " + availability.get("Suite"));
    }
}
