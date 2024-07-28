import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Task class
class Task {
    private String description;
    private LocalTime startTime;
    private LocalTime endTime;
    private String priority;

    public Task(String description, LocalTime startTime, LocalTime endTime, String priority) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return startTime + " - " + endTime + ": " + description + " [" + priority + "]";
    }
}

// Factory Pattern for Task creation
class TaskFactory {
    public Task createTask(String description, String startTime, String endTime, String priority) {
        return new Task(description, LocalTime.parse(startTime), LocalTime.parse(endTime), priority);
    }
}

// Singleton Pattern for ScheduleManager
class ScheduleManager {
    private static ScheduleManager instance;
    private List<Task> tasks;

    private ScheduleManager() {
        tasks = new ArrayList<>();
    }

    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    public void addTask(Task task) throws Exception {
        for (Task t : tasks) {
            if (task.getStartTime().isBefore(t.getEndTime()) && task.getEndTime().isAfter(t.getStartTime())) {
                throw new Exception("Error: Task conflicts with existing task \"" + t.getDescription() + "\".");
            }
        }
        tasks.add(task);
        tasks.sort(Comparator.comparing(Task::getStartTime));
    }

    public void removeTask(String description) throws Exception {
        boolean found = tasks.removeIf(t -> t.getDescription().equals(description));
        if (!found) {
            throw new Exception("Error: Task not found.");
        }
    }

    public List<Task> viewTasks() {
        return tasks;
    }
}

// Observer Pattern for conflict alerts (simplified)
interface TaskObserver {
    void update(Task task);
}

class ConflictObserver implements TaskObserver {
    @Override
    public void update(Task task) {
        System.out.println("Conflict detected with task: " + task.getDescription());
    }
}

// Main class
public class AstronautScheduleApp {
    public static void main(String[] args) {
        ScheduleManager manager = ScheduleManager.getInstance();
        TaskFactory factory = new TaskFactory();
        TaskObserver observer = new ConflictObserver();

        try {
            Task task1 = factory.createTask("Morning Exercise", "07:00", "08:00", "High");
            manager.addTask(task1);

            Task task2 = factory.createTask("Team Meeting", "09:00", "10:00", "Medium");
            manager.addTask(task2);

            System.out.println("Current Tasks:");
            for (Task task : manager.viewTasks()) {
                System.out.println(task);
            }

            manager.removeTask("Morning Exercise");

            Task task3 = factory.createTask("Lunch Break", "12:00", "13:00", "Low");
            manager.addTask(task3);

            System.out.println("\nUpdated Tasks:");
            for (Task task : manager.viewTasks()) {
                System.out.println(task);
            }

            // Test conflict detection
            try {
                Task conflictTask = factory.createTask("Training Session", "09:30", "10:30", "High");
                manager.addTask(conflictTask);
            } catch (Exception e) {
                observer.update(new Task("Training Session", LocalTime.parse("09:30"), LocalTime.parse("10:30"), "High"));
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
