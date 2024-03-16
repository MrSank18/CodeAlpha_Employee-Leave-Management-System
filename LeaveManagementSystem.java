import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class LeaveManagementSystem {
    private List<Employee> employees;
    private List<LeaveRequest> leaveRequests;
    private Map<String, String> employeeCredentials;
    private Map<Integer, String> adminCredentials;
    private List<LeaveRequest> leaveRequestHistory;

    public LeaveManagementSystem() {
        employees = new ArrayList<>();
        leaveRequests = new ArrayList<>();
        leaveRequestHistory = new ArrayList<>();
        employeeCredentials = new HashMap<>();
        adminCredentials = new HashMap<>();
    }

    public boolean authenticateUser(String username, String password) {
        String storedPassword = employeeCredentials.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }

    public boolean authenticateAdmin(int adminId, String password) {
        String storedPassword = adminCredentials.get(adminId);
        return storedPassword != null && storedPassword.equals(password);
    }

    public void addEmployee(String username, String password, String name, int leaveBalance) {
        employees.add(new Employee(username, name, leaveBalance));
        employeeCredentials.put(username, password);
    }

    public Employee getEmployee(String username) {
        for (Employee employee : employees) {
            if (employee.getUsername().equals(username)) {
                return employee;
            }
        }
        return null;
    }

    public void requestLeave(String username, int daysRequested) {
        Employee employee = getEmployee(username);
        if (employee != null) {
            if (daysRequested <= employee.getLeaveBalance()) {
                employee.setLeaveBalance(employee.getLeaveBalance() - daysRequested);
                LeaveRequest request = new LeaveRequest(employee.getName(), daysRequested, false);
                leaveRequests.add(request);
                leaveRequestHistory.add(request);
                notifyAdmin("Leave request made by " + employee.getName());
                System.out.println("Leave request successful.");
            } else {
                System.out.println("Insufficient leave balance.");
            }
        } else {
            System.out.println("Employee not found.");
        }
    }

    public void approveLeave(int requestIndex) {
        if (requestIndex >= 0 && requestIndex < leaveRequests.size()) {
            LeaveRequest request = leaveRequests.get(requestIndex);
            request.setApproved(true);
            leaveRequests.remove(requestIndex);
            System.out.println("Leave request approved.");
        } else {
            System.out.println("Invalid request index.");
        }
    }

    public void cancelLeave(String username, int requestIndex) {
        Employee employee = getEmployee(username);
        if (employee != null && requestIndex >= 0 && requestIndex < leaveRequests.size()) {
            LeaveRequest request = leaveRequests.get(requestIndex);
            employee.setLeaveBalance(employee.getLeaveBalance() + request.getDaysRequested());
            leaveRequests.remove(requestIndex);
            System.out.println("Leave request cancelled.");
        } else {
            System.out.println("Invalid request index or employee not found.");
        }
    }

    public void adjustLeaveBalance(String username, int newLeaveBalance) {
        Employee employee = getEmployee(username);
        if (employee != null) {
            employee.setLeaveBalance(newLeaveBalance);
            System.out.println("Leave balance adjusted successfully.");
        } else {
            System.out.println("Employee not found.");
        }
    }

    public void displayLeaveBalance(String username) {
        Employee employee = getEmployee(username);
        if (employee != null) {
            System.out.println("Leave balance for " + employee.getName() + " is: " + employee.getLeaveBalance());
        } else {
            System.out.println("Employee not found.");
        }
    }

    public void displayLeaveRequests() {
        System.out.println("Leave Requests:");
        for (int i = 0; i < leaveRequests.size(); i++) {
            LeaveRequest request = leaveRequests.get(i);
            System.out.println((i + 1) + ". " + request.getEmployeeName() + " - " + request.getDaysRequested() + " days" + (request.isApproved() ? " (Approved)" : ""));
        }
    }

    public void displayLeaveRequestHistory() {
        System.out.println("Leave Request History:");
        for (LeaveRequest request : leaveRequestHistory) {
            System.out.println(request.getEmployeeName() + " - " + request.getDaysRequested() + " days" + (request.isApproved() ? " (Approved)" : " (Pending/Rejected)"));
        }
    }

    public void notifyAdmin(String message) {
        // Implementation of notification (e.g., email, message) to admin
        System.out.println("Admin notified: " + message);
    }

    public static void main(String[] args) {
        LeaveManagementSystem leaveManagementSystem = new LeaveManagementSystem();
        Scanner scanner = new Scanner(System.in);

        // Admin authentication
        System.out.print("Enter Admin ID: ");
        int adminId = scanner.nextInt();
        System.out.print("Enter Admin Password: ");
        String adminPassword = scanner.next();
        if (!leaveManagementSystem.authenticateAdmin(adminId, adminPassword)) {
            System.out.println("Admin authentication failed.");
            extracted();
        }

        // Admin panel
        System.out.println("Admin Panel");
        System.out.println("1. Add Employee");
        System.out.println("2. Adjust Leave Balance");
        System.out.println("3. Display Leave Request History");
        System.out.println("Enter choice: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                System.out.print("Enter username: ");
                String username = scanner.next();
                System.out.print("Enter password: ");
                String password = scanner.next();
                System.out.print("Enter name: ");
                String name = scanner.next();
                System.out.print("Enter initial leave balance: ");
                int leaveBalance = scanner.nextInt();
                leaveManagementSystem.addEmployee(username, password, name, leaveBalance);
                System.out.println("Employee added successfully.");
                break;
            case 2:
                System.out.print("Enter username of employee to adjust leave balance: ");
                String empUsername = scanner.next();
                leaveManagementSystem.displayLeaveBalance(empUsername);
                System.out.print("Enter new leave balance: ");
                int newLeaveBalance = scanner.nextInt();
                leaveManagementSystem.adjustLeaveBalance(empUsername, newLeaveBalance);
                break;
            case 3:
                leaveManagementSystem.displayLeaveRequestHistory();
                break;
            default:
                System.out.println("Invalid choice.");
        }

        // Employee registration and leave request
        System.out.println("\nEmployee Registration and Leave Request");
        System.out.print("Enter new employee username: ");
        String newUsername = scanner.next();
        System.out.print("Enter new employee password: ");
        String newPassword = scanner.next();
        System.out.print("Enter new employee name: ");
        String newName = scanner.next();
        System.out.print("Enter new employee initial leave balance: ");
        int newLeaveBalance = scanner.nextInt();
        leaveManagementSystem.addEmployee(newUsername, newPassword, newName, newLeaveBalance);
        System.out.println("Employee added successfully.");

        System.out.print("Enter username to request leave: ");
        String requestUsername = scanner.next();
        System.out.print("Enter number of days for leave request: ");
        int daysRequested = scanner.nextInt();
        leaveManagementSystem.requestLeave(requestUsername, daysRequested);

        scanner.close();
    }

	private static void extracted() {
	}
}

class Employee {
    private String username;
    private String name;
    private int leaveBalance;

    public Employee(String username, String name, int leaveBalance) {
        this.username = username;
        this.name = name;
        this.leaveBalance = leaveBalance;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public int getLeaveBalance() {
        return leaveBalance;
    }

    public void setLeaveBalance(int leaveBalance) {
        this.leaveBalance = leaveBalance;
    }
}

class LeaveRequest {
    private String employeeName;
    private int daysRequested;
    private boolean approved;

    public LeaveRequest(String employeeName, int daysRequested, boolean approved) {
        this.employeeName = employeeName;
        this.daysRequested = daysRequested;
        this.approved = approved;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public int getDaysRequested() {
        return daysRequested;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
