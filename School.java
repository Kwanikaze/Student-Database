import java.util.ArrayList;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

public class School extends Participant {
	private double alpha ; // GPA weight

	// constructors
	public School (){
		super();
		this.setName("");
		this.alpha = -1;
		this.setMaxMatches(-1);
		this.setNParticipants(0);
	}
	public School ( String name , double alpha , int maxMatches , int nStudents ){
		super();
		this.setName(name);
		this.alpha = alpha;
		this.setMaxMatches(maxMatches);
		this.setNParticipants(nStudents);
	}
	
	// getters
	public double getAlpha () { return this . alpha ; }
	//setters
	public void setAlpha ( double alpha ) { this . alpha = alpha ; }

	// get new info from the user ; cannot be inherited or overridden from parent
	public void editSchoolInfo ( ArrayList < Student > S) throws NumberFormatException, IOException{
		System.out.println();
		//Edit name
		String name = BasicFunctions.getString ("Name: ");
		this.setName(name);
		//Edit alpha
		double alpha = BasicFunctions.getDouble("GPA weight: ", 0.0, 1.0);
		this.setAlpha(alpha);
		//Edit max number of matches
		int maxMatches = BasicFunctions.getInteger("Maximum number of matches: ", 1, Integer.MAX_VALUE);
		this.setMaxMatches(maxMatches);
	
		//Recalculate rankings 
		this.calcRankings(S);
	}

	public void calcRankings ( ArrayList < Student > S ){
		// calc rankings from alpha
		//Set rankings array to hold number of students
		//ranking[i], ith rank has student index
		this.setNParticipants(S.size());
		
		//Make array to hold scores
		Double scores []= new Double[this.getNParticipants()];
		
		//Loop through every student, calculate their score based on alpha
		for (int i = 0; i < this.getNParticipants(); i++){
		scores[i] = (this.alpha *S.get(i).getGPA()) + ((1-this.alpha)*S.get(i).getES());
		}
		
		//Make new array to hold sorted scores
		//Copy info of scores into sorted scores
		Double sortedScores[] = new Double[this.getNParticipants()];
		sortedScores= scores.clone();
		
		//Sort sortedScores to be descending order
		Arrays.sort(sortedScores,Collections.reverseOrder());
		
		
		//Assign indexes i to rankings array, rankings[0] = 1st ranked student is student index
		//Loop through each sorted score element
		for (int i = 0; i<this.getNParticipants();i++){
			//Loop through each score element
			for (int j = 0; j<this.getNParticipants(); j++){
			// If sortedscores i = scores j, then rankings[j] has student[i]
				outerloop:
				if (sortedScores[i] == scores[j]){	
					//Loop to check that a rank hasn't already been used
					int count = 0;
					for (int k = 0; k <this.getNParticipants(); k++){
					  if (this.getRanking(k) == i)
						 count ++;
					  if (count >= 1)
					  break outerloop;
					}
					//The ith rank has jth student if no repeats
					if (count == 0) this.setRanking(i, j);
				}
			
			}
		}
	}
	
	@Override
	public void print ( ArrayList <? extends Participant > S ){
	// print school row
			//Print name, 40 spaces
			if (this.getName().length()<=40)
				System.out.format("%-40s     ", this.getName());
			else 
				System.out.print (this.getName());
				
			//Print number of spots, right justified, 3 spaces
			System.out.format("%3d  ", this.getMaxMatches());
		
			
			//Print alpha, 8 spaces right justified
			System.out.format("%6.2f  ", this.alpha);
			
			//Print assigned student(s) (dash if no student)
			String matchedStudent = "-";
			if (this.getNMatches() == 0){
				System.out.format("%-40s", matchedStudent);
			}
				//Loop through all matched students otherwise
			else if (this.getNMatches() > 0){
				//Format the names of matches
				StringBuilder stringBuilder = new StringBuilder();
				
				for (int i = 0; i<this.getNMatches();i++){
					//For each match, print corresponding name of the student index
					stringBuilder.append (S.get(this.getMatch(i)).getName());
					//Print out comma if not last student
					if (i<(this.getNMatches() - 1))
						stringBuilder.append (", ");
				}
				
				String matchedList = stringBuilder.toString();
				
				
				//Determine how many spaces the matched students names occupy less than 40? or 40 or more?
				int matchedCharSpaces = matchedList.length();
					
				//Print, Student name(s) less than 40 chars, 
				if(matchedCharSpaces <= 40){
					System.out.print(matchedList);
					// Print remaining spaces needed
					for (int i = 0; i < (40 - matchedCharSpaces); i++)
						System.out.print(" ");
				}
					
				//Print, Student name(s) if equal to or more than 40 chars
				else 
					System.out.print(matchedList);
			}
			
		//Print ranked list of students
			printRankings(S);
			
			System.out.println();
	}
	
	public boolean isValid (){ 
	// check if this school has valid info
		if (this.alpha >= 0 && this.alpha <=1){
			if (this.getMaxMatches() > 0)
			return true;
		}
		return false;	
	}
}