package payroll;

public class HourlyEmployee extends Employee {

	private float hoursWorked;
	private float hourlyPay;
	
	// Getters
	public float getHoursWorked() {
		return hoursWorked;
	}
	
	public float getHourlyPay() {
		return hourlyPay;
	}
	
	// Setter
	public void setHoursWorked(float hoursWorked) {
		this.hoursWorked = hoursWorked;
	}
	
	// Constructor
	public HourlyEmployee(String firstName, String lastName, float hourlyPay) {
		super(firstName, lastName);
		this.hourlyPay = hourlyPay;
		hoursWorked = 0;
	}
	
	@Override
	public float getPaycheck() {
		float paycheck = hourlyPay * hoursWorked;
		
		return (float) Math.round(paycheck * 100) / 100;
	}
	
	@Override
	public String toString() {
		return String.format("Hourly: $%,.2f; %s", hourlyPay, super.toString());
	}
}