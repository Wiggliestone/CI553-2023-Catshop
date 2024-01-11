package variableValidator;

public class TestVariable {

	public static boolean validIntForStock(String amountChosen) {
		
		int testInt;
		
		try {
			testInt = Integer.valueOf(amountChosen);
			if (testInt > 0)
				return true;
			else
				return false;
			
		} catch (NumberFormatException e) {
			
			return false;
		}
		
	}
}
