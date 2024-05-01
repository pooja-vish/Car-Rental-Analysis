package package1;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidationUsingRegex {

	// regex in mm/dd/yyyy format
	private static final String DATE_FORMAT_REGEX = "^(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])/(19|20)\\d\\d$";

	// Function to validate the date as per the regex defined above.
	// Takes date input in a string as a parameter
	// Returns true if date matches the pattern else false
	public static boolean dataValidator(String date) {
		Pattern patternObject = Pattern.compile(DATE_FORMAT_REGEX);
		Matcher matcherObject = patternObject.matcher(date);
		return matcherObject.matches();
	}

	//main method
	public static void main(String[] args) {
		//user gives input
		System.out.println("Enter your date here");
		Scanner scannerObj = new Scanner(System.in);
		String date = scannerObj.next();
		System.out.println(date + " is valid : " + dataValidator(date));
		scannerObj.close();
	}
}
