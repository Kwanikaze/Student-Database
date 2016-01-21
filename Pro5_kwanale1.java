import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class Pro5_kwanale1 {

	public static void main(String[] args) throws NumberFormatException, IOException {
		//Arraylist to hold students and schools
		ArrayList<Student> S = new ArrayList<Student>();
		ArrayList<School> H = new ArrayList<School>();
		SMPSolver GSS = new SMPSolver();
		SMPSolver GSH = new SMPSolver();
		GSH.setSuitorFirst(false);

		
		//Declarations for valid main menu inputs
		final String[] mainOptions = {"L", "E", "P", "M", "D", "X", "R", "Q"};
		String mainMenuInput= "";
		
		//Other declarations
		boolean matched = false;
		
		//Main loop of program
		do {
			displayMenu();
			//Get valid input
			mainMenuInput = BasicFunctions.getMenuString("Enter choice: ", mainOptions);
			
			//If menu input is L, load students from file
			if (mainMenuInput.equals ("L")){
				System.out.println();
				//Check if added schools, if so, clear students
				int x = H.size();
				loadSchools(H);
				if (H.size() != x){
					S.clear();
					matched = false;
				}
				loadStudents(S,H);
				
				//Calculate student rankings for each school
				for (int i = 0; i<H.size(); i++){
					H.get(i).calcRankings(S);
				}
				
			
			}
			
			//If menu input is E, edit students and schools
			else if (mainMenuInput.equals ("E")){
				System.out.println();
				editData(S,H);
				if (S.size() > 0 && (H.size()>0)){ 
					updateParticipants(GSS, GSH, S, H);
					GSH.calcRegrets();
					GSS.calcRegrets();
				}
			}
			
			//If menu input is P, print students and schools
			else if (mainMenuInput.equals ("P")){
				System.out.println();
				//Print Students	
				if (S.size() == 0){
					System.out.println("ERROR: No students are loaded!");
				}
				else{
						System.out.println ("STUDENTS:");
						System.out.println();
						printStudents(S, H);
				}
				
				//Print Schools
				if (H.size() == 0){
						System.out.println();
						System.out.println("ERROR: No schools are loaded!");
						System.out.println();
				}
				else {
						System.out.println();
						System.out.println("SCHOOLS:");
						System.out.println();
						printSchools(S,H,true);
				}
			}
			
			//If M entered, match students to each school
			else if (mainMenuInput.equals ("M")){

				ArrayList < Student > S2 = copyStudents (S);
				ArrayList <School > H2 = copySchools (H);
				GSS. reset (S, H); //SMPSolver with students as suitors
				GSH. reset (H2, S2); // SMPSolver with high schools as suitors
				GSH.setSuitorFirst(false);
				matched = false;
				//If can match, then proceed
				if (GSS.matchingCanProceed()){
					GSS.match();
					GSS.print();
					GSS.printStats();
					//GSS.printMatches();
					GSS.printCompTime();
				}
				if (GSH.matchingCanProceed()){
					GSH.match();
					GSH.print();
					GSH.printStats();
					//GSH.printMatches();
					GSH.printCompTime();
					matched = true;
				}
				
						
			}
			
			//If D entered, display statistics
			else if (mainMenuInput.equals ("D")){
				if (matched){
				//Print matches, stability, regret, time elapsed
				System.out.println();
				System.out.println("STUDENT-OPTIMAL SOLUTION");
				System.out.println();
				
				GSS.printMatches();
				
				System.out.println();
				
				GSS.printStats();
				
				System.out.println();
				System.out.println("SCHOOL-OPTIMAL SOLUTION");
				System.out.println();
				
				GSH.printMatches();
				GSH.printStats();
				}
				else if (!matched){
					System.out.println();
					System.out.println("STUDENT-OPTIMAL SOLUTION");
					System.out.println();
					System.out.println("ERROR: No matches exist!");
					System.out.println();
					System.out.println("SCHOOL-OPTIMAL SOLUTION");
					System.out.println();
					System.out.println("ERROR: No matches exist!");
				}
				System.out.println();
			}
			//If X entered, compare student-optimal and school-optimal matches
			else if (mainMenuInput.equals("X")){
			
			if (matched){	
				System.out.println();
				System.out.println("Solution              Stable    Avg school regret   Avg student regret     Avg total regret       Comp time (ms)");
				System.out.println("----------------------------------------------------------------------------------------------------------------");
				
				
				//Print Student optimal line
				//GSS.printStatsRow("Student optimal       ");
				System.out.print("Student optimal       ");
				if (GSS.determineStability())
					System.out.print("   Yes");
				else if (!GSS.determineStability())
					System.out.print("    No");
				System.out.format("%21.2f", GSS.getAvgReceiverRegret());
				System.out.format("%21.2f", GSS.getAvgSuitorRegret());
				System.out.format("%21.2f", GSS.getAvgTotalRegret());
				double GSSTime = GSS.getTime();
				System.out.format("%21.0f", GSSTime);
				System.out.println();
				
				//Print School optimal line
				//printStatsRow("School optimal        ");
				System.out.print("School optimal        ");
				if (GSH.determineStability())
					System.out.print("   Yes");
				else if (!GSH.determineStability())
					System.out.print("    No");
				System.out.format("%21.2f", GSH.getAvgSuitorRegret());
				System.out.format("%21.2f", GSH.getAvgReceiverRegret());
				System.out.format("%21.2f", GSH.getAvgTotalRegret());
				double GSHTime = GSH.getTime();
				System.out.format("%21.0f", GSHTime);
				System.out.println();
				System.out.println("----------------------------------------------------------------------------------------------------------------");
				
				//Print Winner line
				System.out.print("WINNER           ");
				//Stable 
				if ((GSH.determineStability() && GSS.determineStability()) || (!GSH.determineStability() && !GSS.determineStability()) )
					System.out.print("        Tie");
				else if (GSS.determineStability() && !GSH.determineStability())
					System.out.print("Student-opt");
				else if(!GSS.determineStability() && GSH.determineStability())
					System.out.print(" School-opt");
				//Avg school regret
				if (GSS.getAvgReceiverRegret() < GSH.getAvgSuitorRegret())
					System.out.print ("          Student-opt");
				else if (GSS.getAvgReceiverRegret() > GSH.getAvgSuitorRegret())
					System.out.print ("           School-opt");
				else if (GSS.getAvgReceiverRegret() == GSH.getAvgSuitorRegret())
					System.out.print ("                  Tie");	
				//Avg student regret
				if (GSS.getAvgSuitorRegret() < GSH.getAvgReceiverRegret())
					System.out.print ("          Student-opt");
				else if (GSS.getAvgSuitorRegret() > GSH.getAvgReceiverRegret())
					System.out.print ("           School-opt");
				else if (GSS.getAvgSuitorRegret() == GSH.getAvgReceiverRegret())
					System.out.print ("                  Tie");
				//Avg total regret
				if (GSS.getAvgTotalRegret() < GSH.getAvgTotalRegret())
					System.out.print ("          Student-opt");
				else if (GSS.getAvgTotalRegret() > GSH.getAvgTotalRegret())
					System.out.print ("           School-opt");
				else if (GSS.getAvgSuitorRegret() == GSH.getAvgReceiverRegret())
					System.out.print ("                  Tie");
				// Comp Time
				if (GSS.getTime() < GSH.getTime())
					System.out.print ("          Student-opt");
				else if (GSS.getTime() > GSH.getTime())
					System.out.print ("           School-opt");
				else if (GSS.getTime() == GSH.getTime())
					System.out.print ("                  Tie");
				
				System.out.println();
				System.out.println("----------------------------------------------------------------------------------------------------------------");
				System.out.println();
			}
			else if (!matched){
				System.out.println();
				System.out.println("ERROR: No matches exist!");
				System.out.println();
			}
			
			}
				
			
			//If R entered, reset database
			else if (mainMenuInput.equals ("R")){
				S.clear();
				H.clear();
				matched = false;
				System.out.println();
				System.out.println("Database cleared!");
				System.out.println();
			}
			
			//If menu input is Q, quit program
			else if (mainMenuInput.equals ("Q")){
			System.out.println();
			System.out.println("Hasta luego!");
			}
			
		}while(mainMenuInput != "Q");

	}
	
	//FUNCTIONS
	
	
    // update participant arrays of solvers, creating independent participant arrays for each solver to avoid conflicts
    public static void updateParticipants(SMPSolver GSS, SMPSolver GSH, ArrayList<Student> S, ArrayList<School> H) {

            // GSS solver duplicate arrays
            ArrayList<School> H1 = copySchools(H);
            ArrayList<Student> S1 = copyStudents(S);
            copyAllMatches(GSS, S1, H1);
            GSS.setParticipants(S1, H1);

            // GSH solver duplicate arrays
            ArrayList<School> H2 = copySchools(H);
            ArrayList<Student> S2 = copyStudents(S);
            copyAllMatches(GSH, H2, S2);
            GSH.setParticipants(H2, S2);

    }

    // copy matches from one solver into S and R to maintain original matching
    public static void copyAllMatches(SMPSolver GS, ArrayList<? extends Participant> S, ArrayList<? extends Participant> R) {
            copyMatches_oneGroup(GS.getSuitors(), S);
            copyMatches_oneGroup(GS.getReceivers(), R);
    }

    // copy participant matches in P into newP
    public static void copyMatches_oneGroup(ArrayList<Participant> P, ArrayList<? extends Participant> newP) {
            for (int i = 0; i < P.size(); i++) {
                    newP.get(i).clearMatches();
                    for (int j = 0; j < P.get(i).getNMatches(); j++)
                            newP.get(i).setMatch2(P.get(i).getMatch(j));
            }
    }
    
	
	// create independent copy of student ArrayList
	public static ArrayList < Student > copyStudents ( ArrayList < Student > P) {
		ArrayList <Student > newList = new ArrayList < Student >();
		for (int i = 0; i < P. size (); i++) {
			String name = P. get(i). getName ();
			double GPA = P. get(i). getGPA ();
			int ES = P. get(i). getES ();
			int maxMatches = P.get (i). getMaxMatches ();
			int nSchools = P.get (i). getNParticipants ();
			Student temp = new Student (name, GPA, ES, nSchools );
			temp.setMaxMatches(maxMatches);
			
			for (int j = 0; j < nSchools ; j++) {
				temp . setRanking (j, P. get(i). getIDAtRanking (j));
			}
			newList .add ( temp );
		}
		return newList ;
	}

	// create independent copy of School ArrayList
	public static ArrayList < School > copySchools ( ArrayList < School > P) {
		ArrayList <School > newList = new ArrayList < School >();
			for (int i = 0; i < P. size (); i++) {
				String name = P. get(i). getName ();
				double alpha = P. get(i). getAlpha ();
				int maxMatches = P.get (i). getMaxMatches ();
				int nStudents = P.get (i). getNParticipants ();
				School temp = new School (name ,alpha , maxMatches , nStudents );
			
				for (int j = 0; j < nStudents ; j++) {
					temp . setRanking (j, P. get(i). getIDAtRanking (j));
				}
				newList .add ( temp );
			}
		return newList ;
	}
	

	//Print students to the screen, including matched school (if one exists)
	public static void printStudents (ArrayList<Student> S, ArrayList<School> H){
		System.out.println(" #   Name                                         GPA  ES  Assigned school                         Preferred school order");
		System.out.println("---------------------------------------------------------------------------------------------------------------------------");
		//Print every student 
		for (int i = 0; i<S.size(); i++){
			//Print list place
			if (i < 9)
				System.out.print("  " + (i+1) + ". ");
			else if (i<99)
				System.out.print(" " + (i+1) + ". ");
			else
				System.out.print("" + (i+1) + ". ");
			S.get(i).print(H);
		}
		System.out.println("---------------------------------------------------------------------------------------------------------------------------");
	}
	
	//Print schools to the screen, including matched student (if one exists)
	public static void printSchools(ArrayList<Student> S, ArrayList<School> H, boolean callFromMainMenu){
		System.out.println(" #   Name                                     # spots  Weight  Assigned students                       Preferred student order");
		System.out.println("------------------------------------------------------------------------------------------------------------------------------");
		//Print every school
		for (int i = 0; i < H.size(); i++){
			//Print list place
			if (i < 9)
				System.out.print("  " + (i+1) + ". ");
			else if (i<99)
				System.out.print(" " + (i+1) + ". ");
			else 
				System.out.print("" + (i+1) + ". ");
			H.get(i).print(S);
		}
		System.out.println("------------------------------------------------------------------------------------------------------------------------------");	
		if (callFromMainMenu==true)
			System.out.println();
	}
	
	//Sub-area menu to edit students and schools
	public static void editData(ArrayList<Student> S, ArrayList<School> H) throws IOException{
		//Array of options
		final String[] options = {"S", "H", "Q"};
		String menuInput = "";
		do{
			System.out.println("Edit data");
			System.out.println("---------");
			System.out.println("S - Edit students");
			System.out.println("H - Edit high schools");
			System.out.println("Q - Quit");
			System.out.println();
			menuInput = BasicFunctions.getMenuString("Enter choice: ", options);
			
			//If menu input is S, edit student info (open menu)
			if (menuInput == "S"){
				if (S.size() == 0){
					System.out.println();
					System.out.println("ERROR: No students are loaded!");
					System.out.println();
				}
				else if (S.size() > 0){
					editStudents(S,H);
				}
			}
			
			//If menu input is H, edit school info (open menu)
			else if (menuInput == "H"){
				if (H.size() == 0){
					System.out.println();
					System.out.println("ERROR: No schools are loaded!");
					System.out.println();
				}
				else if (H.size() > 0 )
					editSchools(S,H);
			}
			
			// If Q, quit loop
			else if (menuInput == "Q"){
				System.out.println();
			}
			
		}while ( menuInput != "Q");
	}
	
	//Sub-area to edit students. Regret editted student's regret if needed. After editting a student, recalculate existing school rankings and regrets
	public static void editStudents(ArrayList<Student> S, ArrayList<School> H) throws IOException{
		int studentMenuInput = -1;
		//Loop to print table, ask for edits, stop when 0 entered
		
		do{
			System.out.println();
			//Print table of students
			printStudents(S, H);
			
			//Prompt user for student, quit if 0
			studentMenuInput = BasicFunctions.getInteger("Enter student (0 to quit): ", 0, S.size());
			
			//If not 0, edit student entered
			if (studentMenuInput != 0){
				int studentIndex = studentMenuInput - 1;
				S.get(studentIndex).editInfo(H);
			}
			if (studentMenuInput == 0)
				System.out.println();
		}while (studentMenuInput != 0);
		
		//Calc student rankings for each school
		for (int i = 0; i<H.size(); i++){
			H.get(i).calcRankings(S);
		}
		
		
	}
	
	//Sub-area to edit schools. Any existing rankings and regret for the edited school are updated
	public static void editSchools(ArrayList<Student> S, ArrayList<School> H) throws IOException{
		int schoolMenuInput = -1;
		
		//Loop to print table, ask for edits, stop when 0 entered
		
		do{
			System.out.println();
			//Print table of schools
			printSchools(S, H, false);
			
			//Prompt user for school, quit if 0
			schoolMenuInput = BasicFunctions.getInteger("Enter school (0 to quit): ", 0, H.size());
			if (schoolMenuInput != 0){
				int schoolIndex = schoolMenuInput - 1;
				H.get(schoolIndex).editSchoolInfo(S);
				//Calc student rankings for each school
				for (int i = 0; i<H.size(); i++){
					H.get(i).calcRankings(S);
				}
			}
			if (schoolMenuInput == 0)
				System.out.println();
		}while (schoolMenuInput != 0);

	}
	
	//Display Menu
	public 	static void  displayMenu(){
		System.out.println("JAVA STABLE MARRIAGE PROBLEM v3");
		System.out.println();
		System.out.println("L - Load students and schools from file");
		System.out.println("E - Edit students and schools");
		System.out.println("P - Print students and schools");
		System.out.println("M - Match students and schools using Gale-Shapley algorithm");
		System.out.println("D - Display matches");
		System.out.println("X - Compare student-optimal and school-optimal matches");
		System.out.println("R - Reset database");
		System.out.println("Q - Quit");
		System.out.println();
	}
	
	//Load school info from a user-provided file and return the num ber of new schools. New schools are added to the list of existing schools
	public static void loadSchools(ArrayList<School> H) throws NumberFormatException, IOException, FileNotFoundException{
		boolean fileExists = false;
		String fileName = "";
		String line="";
		//Counters for number of schools in file, and number of schools which are valid
		int nSchoolsInput = 0;
		int nValidSchoolsAdded = 0;
		
		//Find file of schools
		do{
			fileName = BasicFunctions.getString("Enter school file name (0 to cancel): ");
			fileExists = BasicFunctions.fileExists(fileName);
			if (fileExists == true){
			}
			else if (fileExists == false && !fileName.equals("0")){
				System.out.println();
				System.out.println("ERROR: File not found!");
				System.out.println();
			}
		}while (!fileExists && !fileName.equals ("0"));
		
		if (fileName.equals("0")){
			System.out.println();
			System.out.println("File loading process canceled.");
			System.out.println();
		}
		
		else if (fileExists == true){
			//Retrieve info from valid file
			File file = new File(fileName);
			BufferedReader fin = new BufferedReader(new FileReader(file));
		

			//Loop through school file
			do {
				line = fin.readLine();
				if (line != null){
					nSchoolsInput++;
					//Each line of file is a school inputed, add valid schools to array list
					String [] splitString = line.split(",");
				
					//Valid schools check
				
						School tempSchool = new School();
						tempSchool.setName (splitString[0]);
						tempSchool.setAlpha(Double.parseDouble(splitString[1]));
						tempSchool.setMaxMatches(Integer.parseInt(splitString[2]));
						//Add valid schools to array list	
						if (tempSchool.isValid() == true){
							H.add(tempSchool);
							nValidSchoolsAdded++;
						}
				}
			}while (line!=null);
			fin.close();
		
			//Output number of valid schools out of schools inputed
			System.out.println();
			System.out.println(nValidSchoolsAdded + " of " + nSchoolsInput + " schools loaded!");
			System.out.println();
		}
	}
	
	//Load student info from user-provided file and return the number of new student. New students added to list of existing students
	public static void loadStudents(ArrayList<Student> S, ArrayList<School> H) throws NumberFormatException, IOException, FileNotFoundException{
		boolean fileExists = false;
		String fileName = "";
		String line="";
		//Counters for number of schools in file, and number of schools which are valid
		int nStudentsInput = 0;
		int nValidStudentsAdded = 0;
		
		//Find file of schools
		do{
			fileName = BasicFunctions.getString("Enter student file name (0 to cancel): ");
			fileExists = BasicFunctions.fileExists(fileName);
			if (fileExists == true){
			}
			else if (fileExists == false && !fileName.equals("0")){
				System.out.println();
				System.out.println("ERROR: File not found!");
				System.out.println();
			}
		}while (!fileExists && !fileName.equals ("0"));
		
		if (fileName.equals("0")){
			System.out.println();
			System.out.println("File loading process canceled.");
			System.out.println();
		}
		
		else if (fileExists == true){
			//Retrieve info from valid file
			File file = new File(fileName);
			BufferedReader fin = new BufferedReader(new FileReader(file));

			//Loop through student file
			do{
				line = fin.readLine();
				if (line != null){
					nStudentsInput++;
					//Each line of file is a student inputed, add valid students to array list
					String [] splitString = line.split(",");
				
					//Valid students must have enough rankings for every school, no more no less
					if (splitString.length == (H.size() + 3)){
						Student tempStudent = new Student();
						tempStudent.setName (splitString[0]);
						tempStudent.setGPA (Double.parseDouble(splitString[1]));
						tempStudent.setES (Integer.parseInt(splitString[2]));
						//Assign rankings from file to tempStudent rankings array
						// i is rank, 0 being highest rank, split string[i] is index of that school
						tempStudent.setNParticipants(H.size());
						for (int i = 3; i<(H.size() + 3); i++){
							tempStudent.setRanking(i-3, (Integer.parseInt(splitString[i])-1));
						}
						
						//Add valid students to array list	
						if (tempStudent.isValid() == true){
							S.add(tempStudent);
							nValidStudentsAdded++;
						}
					}
				}
			}while (line != null);
			fin.close();
		
			//Output number of valid students out of students inputed
			System.out.println();
			System.out.println(nValidStudentsAdded + " of " + nStudentsInput + " students loaded!");
			System.out.println();
		}
	}
	
}
