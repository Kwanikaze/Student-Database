import java.io.IOException;
import java.util.ArrayList;

public class Student extends Participant {
	private double GPA ; // GPA
	private int ES ; // extracurricular score

	// constructors
	public Student (){
		super();
		this.setName("");
		this.GPA = -1;
		this.ES= -1;
		setNParticipants(0);
		setMaxMatches(1);
	}
	public Student ( String name , double GPA , int ES , int nSchools ){
		super();
		this.setName(name);
		this.GPA = GPA;
		this.ES = ES;
		setNParticipants(nSchools);
		setMaxMatches(1);
	}

	// getters
	public double getGPA (){
		return this.GPA;
	}
	public int getES (){
		return this.ES;
	}
	//setters
	public void setGPA ( double GPA ){
		this.GPA = GPA;
	}
	public void setES ( int ES ){
		this.ES = ES;
	}
	
	@Override
	public void editInfo (ArrayList <? extends Participant > H) throws NumberFormatException, IOException{
		// user info
		System.out.println();
		//Edit name
		String name = BasicFunctions.getString ("Name: ");
		this.setName(name);
		//Edit GPA
		double GPA = BasicFunctions.getDouble("GPA: ", 0.0, 4.0);
		this.setGPA(GPA);
		//Edit ES
		int ES = BasicFunctions.getInteger("Extracurricular score: ", 0, 5);
		this.setES(ES);
		//Edit max number of matches, will always be 1 though
		int maxMatches = BasicFunctions.getInteger("Maximum number of matches: ", 1, Integer.MAX_VALUE);
		maxMatches = 1;
		this.setMaxMatches(maxMatches);

		//Ask to edit rankings
		String editRankingsInput = "";
		String[] options = {"y","n"};
		
		//Get valid input
		do {
			editRankingsInput = BasicFunctions.getMenuString("Edit rankings (y/n): ", options);
		} while (editRankingsInput != "y" && editRankingsInput != "n" );
		
		//If y entered, edit rankings
		if (editRankingsInput == "y"){
			editRankings(H);
		}
	}
	
	@Override
	public void print ( ArrayList <? extends Participant > H){
		//Print Student Row
			
			//Print name
				if (this.getName().length()<=40)
					System.out.format("%-40s    ", this.getName());
				else 
					System.out.print (this.getName() + "    ");
			//Print GPA and ES
				System.out.format("%4.2f   %1d  ",this.getGPA(),this.getES());
				
			//Print assigned school (dash if no school)
				String matchedSchool = "-"; 
				if (this.getNMatches() == 0){
				System.out.format("%-40s", matchedSchool);
				}
				
				//Matched school name less than 40 chars long
				else if(H.get(this.getMatch(0)).getName().length()<=40)
					System.out.format("%-40s", H.get(this.getMatch(0)).getName());
				//School name more than 40 chars long
				else 
					System.out.print (H.get(this.getMatch(0)).getName());	
					
				//Print ranked list of schools
				printRankings(H);
				System.out.println();
	}
	
	public boolean isValid () {
		//Check if GPA is valid
		if (this.GPA >= 0 && this.GPA <= 4){
			//Check if ES is valid
			if (this.ES >=0 && this.ES <=5 ){
			//Check if rankings valid 
				int nUniqueRankings = 0;
				for(int i = 0; i < this.getNParticipants(); i++){
					for (int j = 0; j<this.getNParticipants();j++){
						if (getRanking(j) == i){
						nUniqueRankings++;
						break;
						}
					}
				}
				//Return true if valid rankings
				if (nUniqueRankings == getNParticipants())
					return true;
				else return false;
					
			}
			else return false;
		}
		else return false;
	}
	
}