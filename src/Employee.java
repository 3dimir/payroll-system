package payroll;

public abstract class Employee {
	
	private static int idCounter = 1;
	private int idNumber;
	private String firstName;
	private String lastName;
	
	// Getters
	public int getIdNumber() {
		return idNumber;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public abstract float getPaycheck();
	
	// Constructor
	public Employee(String firstName, String lastName) {
		this.idNumber = idCounter++;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	@Override
	public String toString() {
		return "ID: " + idNumber + " - " + lastName + ", " + firstName;
	}	
}
