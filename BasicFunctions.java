import java.io.*;
	
	
public class BasicFunctions {
	//Global buffered reader
	public static BufferedReader cin = new BufferedReader(new InputStreamReader(System.in));
	 

	//Determine if file exists
	public static boolean fileExists(String fileName){
		File file = new File (fileName);
		boolean exists = file.exists();
		return exists;
		
	}
	
	//Get regular string input function
	public static String getString(String prompt) throws NumberFormatException, IOException{
			System.out.print (prompt);
			String x = cin.readLine();
			return x;
	}	
	
	public static String getMenuString(String prompt, String[] options) throws NumberFormatException, IOException{
		String x = "";
		boolean valid = false;
		
			System.out.print (prompt);
			valid = false;
			
			//Robust input
			try {
				x = BasicFunctions.cin.readLine();
			}
			catch (NumberFormatException e){
				valid = false;
			}
			catch (IOException e) {
				valid = false;
			}
			//Check through valid options for main menu
			for (int i = 0; i < options.length; i++){
				if (x.equalsIgnoreCase(options[i])){
					valid = true;
					x = options[i];
					return x;
				}
			}
			//Ask for input again if no valid option entered
			if (valid==false && prompt == "Edit rankings (y/n): "){
				System.out.println("ERROR: Choice must be 'y' or 'n'!");
			}	
			
			else if (valid == false){
				System.out.println();
				System.out.println ("ERROR: Invalid menu choice!");
				System.out.println();
			}
		return x;
	}
	
	//Get Integer function
	public static int getInteger(String prompt,int LB, int UB){
		int x = -1;
		boolean valid;
		
		//Ask for integer input
		do{
			System.out.print (prompt);
			valid = true;
				
			try{
			x = Integer.parseInt(cin.readLine());
			}
				
			catch (NumberFormatException e){
				System.out.println();
				//Determine to print out infinity or a different integer for upper bound
				if (UB == Integer.MAX_VALUE){
					System.out.println ("ERROR: Input must be an integer in [" + LB + ", infinity]!");
				}
				else{
					System.out.println ("ERROR: Input must be an integer in [" + LB + ", " + UB + "]!");
				}
				System.out.println();
				valid = false;
			}
				
			catch (IOException e) {
				//Determine to print out infinity or a different integer for upper bound
				System.out.println();
				if (UB == Integer.MAX_VALUE){
					System.out.println ("ERROR: Input must be an integer in [" + LB + ", infinity]!");
				}
				else{
					System.out.println ("ERROR: Input must be an integer in [" + LB + ", " + UB + "]!");
				}
				System.out.println();
				valid = false;
			}
				
			if ((valid && x < LB) || (valid && x > UB)){
				System.out.println();
				//Determine to print out infinity or a different integer for upper bound
				if (UB == Integer.MAX_VALUE){
					System.out.println ("ERROR: Input must be an integer in [" + LB + ", infinity]!");
				}
				else{
					System.out.println ("ERROR: Input must be an integer in [" + LB + ", " + UB + "]!");
				}
				System.out.println();
				valid = false;
			}	
		} while (!valid);
			
		return x;
		}
	
	//GET DOUBLE FUNCTION: Get a real number in the range [LB, UB] from the user. Prompt the user repeatedly until a valid value is entered
		public static double getDouble (String prompt, double LB, double UB) throws NumberFormatException, IOException{
			double y = -1;
			boolean valid;
			
			//Get valid input
			do {
				System.out.print (prompt);
				valid = true;		
				try{
					y = Double.parseDouble(cin.readLine());
				}
				
				catch (NumberFormatException e){
					System.out.println();
					System.out.format ("ERROR: Input must be a real number in [%.2f, %.2f]!",LB,UB);
					System.out.println();
					System.out.println();
					valid = false;
				}
				
				catch (IOException e){
					System.out.println();
					System.out.format ("ERROR: Input must be a real number in [%.2f, %.2f]!",LB, UB);
					System.out.println();
					System.out.println();
					valid = false;
				}
				
				if ((valid && y <LB) || (valid && y >UB)){
					System.out.println();
					System.out.format ("ERROR: Input must be a real number in [%.2f, %.2f]!",LB,UB);
					System.out.println();
					System.out.println();
					valid = false;
				}
			} while (!valid);
			return y;
		}
	
}
