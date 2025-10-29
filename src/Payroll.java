package payroll;

import java.util.Scanner;

public class Payroll {

	private Employee[] Employees = new Employee[100];
	private int employeeCount = 0;

	private boolean sortByID = false;
	public Scanner input = new Scanner(System.in);
	
	private String getName(Scanner input, String prompt) {
		while (true) {
			System.out.println(prompt);
			System.out.println("(Enter 'Q' to return to main menu)");
			
			// trim() is not included here because 'space' is a valid input
			String name = input.nextLine();
			
			if(name.equalsIgnoreCase("q")) return null;
			
			// Checks if the string contains upper/lower case letter, space, or hyphen before converting
			if (name.matches("[A-Za-z\\s-]+")) return name;
			
			System.out.println("\nERR: A valid name may consist only of letters, spaces, and hyphens =====");
		}
	}
	
	private float getPay(Scanner input, String prompt) {
		while (true) {
			System.out.print(prompt);
			String userInput = input.nextLine().trim();
			
			if(userInput.equalsIgnoreCase("q")) return 0;
			
			// Checks if the string is an integer or a decimal before converting
			if (userInput.matches("\\d+(\\.\\d+)?")) return Float.parseFloat(userInput);
			
			System.out.println("\nERR: Please enter a valid number =====");
		}
	}
	
	private int getType(Scanner input) {
		while (true) {
			System.out.println("\nSelect employee type:\n");
			System.out.println("1) Salaried");
			System.out.println("2) Hourly");
			System.out.println("Q) Return to main menu");
			String userInput = input.nextLine().trim();
			
			if (userInput.equalsIgnoreCase("q")) return 0;
			
			// Checks if the string is either 1 or 2
			if (userInput.matches("[12]")) return Integer.parseInt(userInput);
			
			System.out.println("\nERR: Please select 1 or 2 =====");
		}
	}
	
	private int getID(Scanner input) {
		while (true) {
			System.out.println("\nPlease enter employee ID:");
			System.out.println("(Enter 'Q' to return to main menu)");
			String userInput = input.nextLine().trim();
			
			if (userInput.equalsIgnoreCase("q")) return 0;
			
			// Checks if the string is an integer
			if (!userInput.matches("\\d+")) {
				System.out.println("\nERR: Please enter a valid ID number =====");
				continue;
			}
			
			int id = Integer.parseInt(userInput);
			
			if (id < 1 || id > employeeCount) {
				System.out.println("\nERR: ID must be between 1 and " + employeeCount + " =====");
				continue;
			}
			return id;
		}
	}
	
	private int compareEmployees(Employee e1, Employee e2) {
		if (sortByID) return Integer.compare(e1.getIdNumber(), e2.getIdNumber()); // Sorts by ID right away
		
		int last = e1.getLastName().compareToIgnoreCase(e2.getLastName());
		if (last != 0) return last;
		
		int first = e1.getFirstName().compareToIgnoreCase(e2.getFirstName());
		if (first != 0) return first;
		
		return Integer.compare(e1.getIdNumber(), e2.getIdNumber());
	}
	
	// Payroll-specific printing format
	private void printPayroll(Employee employee) {
		String name = employee.getLastName() + ", " + employee.getFirstName();
		String paycheck = String.format("$%,.2f", employee.getPaycheck());
		System.out.println(String.format("%-20s %20s%n", name, paycheck));
	}
	
	// partition(), quickSort(),findLargest(), selectionSort(), sequentialSearch(), and binarySearch() are implemented from lecture slides
	private int partition(Employee[] employees, int low, int high) {
		Employee pivot = employees[(low + high) / 2];
		
		while(true) {
			// Since string comparison returns either + or - number, we compare it to 0 instead of pivot
			while(compareEmployees(employees[low], pivot) < 0) low++;
			while(compareEmployees(employees[high], pivot) > 0)high--;
			if(low >= high) return high;
			
			Employee temp = employees[low];
			employees[low] = employees[high];
			employees[high] = temp;
			low++;
			high--;
		}
	}
	
	private void quickSort(Employee[] employees, int low, int high) {
		if(low >= 0 && high >= 0 && low < high) {
			int p = partition(employees, low, high);
			
			quickSort(employees, low, p);
			quickSort(employees, p + 1, high);
		}
	}
	
	private static int findLargest(Employee[] employees, int start, int end) {
		int largest = start;
		
		for (int i = start + 1; i < end; i++) {
			if (employees[i].getPaycheck() > employees[largest].getPaycheck()) {
				largest = i;
			}
		}
		return largest;
	}
	
	private void selectionSort(Employee[] employees, int size) {
		for (int outer = 0; outer < size; outer++) {
			int foundIndex = findLargest(employees, outer, size);
			
			Employee temp = employees[outer];
			employees[outer] = employees[foundIndex];
			employees[foundIndex] = temp;
		}
	}
	
	private int sequentialSearch(String lastname) {
		for (int i = 0; i < employeeCount; i++) {
			String currentLast = Employees[i].getLastName();
			
			if (currentLast.equalsIgnoreCase(lastname)) return i;
		}
		return -1; // Name not found
	}
	
	private int binarySearch(Employee[] employees,int low, int high, int id) {
		if (low > high) return -1; // ID not found
		
		int middle = (low + high) / 2;
		
		if (employees[middle].getIdNumber() == id) return middle;
		if (employees[middle].getIdNumber() > id) return binarySearch(employees, low, middle - 1, id);
		else return binarySearch(employees, middle + 1, high, id);
	}
	
	public void createEmployee() {
		String fname = getName(input, "\nEnter employee's first name:");
		if (fname == null) return; // User pressed 'q'
		
		String lname = getName(input, "\nEnter employee's last name:");
		if (lname == null) return;
		
		int employeetype = getType(input);
		if (employeetype == 0) return;
		
		float pay = 0;
		Employee employee = null;
		
		if (employeetype == 1) {
			pay = getPay(input, "\nPlease enter yearly salary:\n$");
			if (pay == 0) return;
			employee = new SalariedEmployee(fname, lname, pay);
		}
		else if (employeetype == 2) {
			pay = getPay(input, "\nPlease enter hourly pay:\n$");
			if (pay == 0) return;
			employee = new HourlyEmployee(fname, lname, pay);
		}
		
		Employees[employeeCount] = employee;
		employeeCount++;
		System.out.println("\n===== SUCCESSFULLY CREATED EMPLOYEE =====");
	}
	
	public void searchEmployee() {
		// Makes sure there is enough employees
		if (employeeCount <= 1) {
			System.out.println("\nERR: Not enough employees =====");
			return;
		}
		
		String lname = getName(input, "\nEnter employee's last name:");
		if (lname == null) return;
		
		sortByID = false;
		quickSort(Employees, 0, employeeCount - 1);
		
		int index = sequentialSearch(lname);
		
		if (index == -1) {
			System.out.println("\n===== Employee not found =====");
		}
		else {
			System.out.println("\n===== Employee(s) found =====\n");
			
			for (int i = index; i < employeeCount; i++) {
				if(!Employees[i].getLastName().equalsIgnoreCase(lname)) break;
				
				String name = Employees[i].getFirstName() + " " + Employees[i].getLastName();
				int iD = Employees[i].getIdNumber();
				System.out.println(name + " Employee " + iD);
			}
		}
	}
	
	public void displayEmployee() {
		// Makes sure there is enough employees
		if (employeeCount == 0) {
			System.out.println("\nERR: No employees to display =====");
			return;
		}
		
		int ID = getID(input);
		if (ID == 0) return;
		
		sortByID = true; // Sorts by ID instead of name
		quickSort(Employees, 0, employeeCount - 1);
		
		int index = binarySearch(Employees, 0, employeeCount - 1, ID);
		
		if (index == -1) {
			System.out.println("\n===== Employee not found =====");
		}
		else {
			System.out.println("\n===== Employee found =====\n");
			System.out.println(Employees[index]);
		}
	}
	
	private void runPayroll() {
		// Makes sure there is enough employees
		if (employeeCount == 0) {
			System.out.println("\nERR: No employees to display =====");
			return;
		}
		
		// Determines if an employee is salaried, then asks for the hours worked
		for (int i = 0; i < employeeCount; i++) {
			Employee employee = Employees[i];
			
			if (employee instanceof HourlyEmployee) {
				HourlyEmployee hourly = (HourlyEmployee) employee;
				
				while (true) {
					System.out.println("Enter hours worked for " + hourly.getFirstName() + " " + hourly.getLastName());
					System.out.println("(Enter 'Q' to return to main menu)");
					String userInput = input.nextLine().trim();
					
					if (userInput.equalsIgnoreCase("q")) {
						System.out.println("Are you sure you want to quit?");
						System.out.println("Quitting will lose all data entered. (Y/N)");
						String confirm = input.nextLine().trim();
						
						if (confirm.equalsIgnoreCase("y")) {
							System.out.println("\n===== EXITING PAYROLL ... ALL DATA LOST =====");
							return;
						}
						else continue; // Stays in loop
					}
					
					// Checks if the string is an integer or a decimal before converting
					if (!userInput.matches("\\d+(\\.\\d+)?")) {
						System.out.println("\nERR: Please enter a valid number =====\n");
						continue;
					}
					
					float hours = Float.parseFloat(userInput);
					hourly.setHoursWorked(hours);
					break;
				}
			}
		}
		
		// Calculates every employees paycheck
		for(int i = 0; i < employeeCount; i++) {
			Employees[i].getPaycheck();
		}
		
		selectionSort(Employees, employeeCount);
		
		System.out.println("\n================ PAYROLL ================\n");
		for (int i = 0; i < employeeCount; i++) {
			printPayroll(Employees[i]);
		}
		
		System.out.println("============ END OF PAYROLL =============\n");
	}
	
	public void run() {		
		System.out.println("\nPAYROLL SYSTEM");
		
		while (true) {
			System.out.println("\n===== MENU =====\n");
			System.out.println("1) Create an employee");
			System.out.println("2) Search for an employee by last name");
			System.out.println("3) Display an employee, by employee number");
			System.out.println("4) Run payroll");
			System.out.println("5) Quit");
			
			// nextLine() clears input buffer
			String userInput = input.nextLine().trim();
			
			// Avoids crash with regex comparison, checking if the string is a number between 1 and 5
			if (userInput.matches("[1-5]")) {
				int choice = Integer.parseInt(userInput);
				
				switch (choice) {
				case 1:
					System.out.println("\n===== CREATE EMPLOYEE =====");
					createEmployee();
					break;
					
				case 2:
					System.out.println("\n===== SEARCH FOR EMPLOYEE =====");
					searchEmployee();
					break;
					
				case 3:
					System.out.println("\n===== DISPLAY EMPLOYEE BY NUMBER =====");
					displayEmployee();
					break;
					
				case 4:
					System.out.println("\n===== RUN PAYROLL =====");
					runPayroll();
					break;
					
				case 5:
					input.close();
					System.out.println("\n===== QUITTING PROGRAM =====");
					return; // Exits the program
					
				default: 
					System.out.println("\nERR: Please select 1 to 5 =====");
				}
			}
			else {
				System.out.println("\nERR: Please select 1 to 5 =====");
			}
		}
	}
	
	public static void main(String[] args) {
		Payroll payroll = new Payroll();
		payroll.run();
	}
}
