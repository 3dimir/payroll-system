package payroll;

public class SalariedEmployee extends Employee {
	
	protected float salary;
	
	// Getter
	public float getSalary() {
		return salary;
	}
	
	// Constructor
	public SalariedEmployee(String firstName, String lastName, float salary) {
		super(firstName, lastName);
		this.salary = salary;
	}

	@Override
	public float getPaycheck() {
		float paycheck = salary / 26;
		
		return (float) Math.round(paycheck * 100) / 100;
	}
	
	@Override
	public String toString() {
		return String.format("Salaried, Base: $%,.2f: %s", salary, super.toString());
	}
}