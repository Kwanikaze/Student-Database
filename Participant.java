import java.io.IOException;
import java.util.ArrayList;

public class Participant {
	private String name ; // name
	private int [] rankings ; // rankings of participants
	private ArrayList < Integer > matches = new ArrayList < Integer >() ;// match indices
	private int regret ; // total regret
	private int maxMatches ; // max # of allowed matches / openings
	
	// constructors
	public Participant (){
		this.name = "";
		this.rankings = new int[0];
		this.regret = -1;
		this.maxMatches = -1;
				
	}
	public Participant ( String name , int maxMatches , int nParticipants ){
		this.name = name;
		this.rankings = new int[nParticipants];
		this.regret = -1;
		this.maxMatches = maxMatches;
	}
	
	
	// getters
	public String getName (){
		return this.name;
	}
	
	public int getRanking (int i){
		//Returns participant @ ranking i WRONG WRONG WRONG, FIX THE MATCHMAKER FUNCTION
		//Search for i, return r if rankings[r]= i
		for (int r = 0; r < rankings.length; r++){
			if (this.rankings[r] == i)
				return r;
		}
		return -1;
	}
	
	public int getIDAtRanking (int r){
		return this.rankings[r];
	}
	
	public int getMatch ( int i ){
			return this.matches.get(i);
	}

	
	public int getRegret (){
		return this.regret;
	}
	public int getMaxMatches (){
		return this.maxMatches;
	}
	public int getNMatches (){
		return this.matches.size();
	}
	// return length of rankings array
	public int getNParticipants (){
		return this.rankings.length;
	}
	public boolean isFull (){
		if (this.matches.size() >= this.maxMatches){
			return true;
		}
		return false;
	}
	
	// setters
	public void setName ( String name ){
		this.name = name;
	}
	public void setRanking ( int r , int i ){
		//set ranking r, to hold participant i
		this.rankings[r]=i;
	}
	
	// remove the match with participant k
	public void unmatch ( int k){
		//Find match index that holds k, remove that match
		for (int i = 0; i < this.getNMatches(); i++){
			if (this.getMatch(i) == k){
				this.matches.remove(i);
				break;
			}
		}
	}
	
	public void setMatch ( int m){

		//If participant has maxMatches, reassign new match to worstMatchIndex
		if (isFull()){
			//Loop through all matches to find worstMatchIndex
			for (int i = 0; i<this.getNMatches();i++){	
				if (this.getMatch(i) == this.getWorstMatch()){
					this.unmatch(this.getWorstMatch());
					this.matches.add(i,m);
					break;
				}
			}
		}
		
		else if (!isFull()){
			this.matches.add(m);
		}
	}
	
	public void setMatch2 ( int m){
		this.matches.add(m);
	}
	
	public void setRegret ( int r ){
		this.regret = r;
	}
	public void setNParticipants ( int n ){
		// set rankings array size, all elements have value -1
		this.rankings = new int[n];
		for (int i = 0; i < n ; i++){
			this.rankings[i] = -1;
		}
	}
	public void setMaxMatches ( int n ){
		this.maxMatches = n;
	}
	
	
	// methods to handle matches
	public void clearMatches (){
		// clear all matches
		this.matches.clear();
	}
	
	public int findRankingByID ( int k ){
		// find rank of participant k
		for (int i=0; i<rankings.length; i++){
			if (this.rankings[i] == k)
				return i;
		}
		return k;
	}
	public int getWorstMatch (){
		// find the worst - matched participant
		int worstMatchID = -1;
		int worstRankedMatch = -1;
		
		//Loop through all existing participants, find worst rank of existing matches
		for (int i = 0; i<this.rankings.length; i++){
			// If match to participant i exists, check it's rank
			if (matchExists(i)){
				//If rank of i, is worse than previous worst rank, worst rank is rank of i 
				int rank = this.findRankingByID(i);
				if (rank > worstRankedMatch){
					//Set worst ranked match to rank of i, set worstMatchIndex to i
					worstRankedMatch = rank;
					worstMatchID = i;
				}
			}
		}
		return worstMatchID;
	}
	

	
	
	public boolean matchExists ( int k) {
		// check if match to participant k exists
		for (int i = 0; i <this.getNMatches(); i++){
			if (this.getMatch(i) == k)
				return true;
		}
		return false;
		
	}
	public int getSingleMatchedRegret ( int k){
		return this.findRankingByID(k);
	}
	public void calcRegret (){
		// calculate total regret over all matches
		this.regret = 0;
		for (int i = 0; i <this.getNMatches(); i++){
			//Get each match's regret
			this.regret +=getSingleMatchedRegret(this.getMatch(i));
		}
		
	}
	
	
	// methods to edit data from the user
	//OVERRIDED
	public void editInfo ( ArrayList <? extends Participant > P ) throws NumberFormatException, IOException{
		
	}
	
	//NOT OVERRIDED
	public void editRankings ( ArrayList <? extends Participant > P ){
		//Clear rankings array
				this.setNParticipants(this.rankings.length);
				
				System.out.println();
				System.out.println("Participant " + this.getName() + "'s rankings:");
				
				//Request ranking for each school, Rank-1 is ranking, j is school, 
				//Loop through each school j
				for (int j = 0; j < rankings.length; j++){
					
					//Check to make sure no repeated rankings in rankings array
					boolean used = false;
					int Rank = -1;
					
					do {
						//Get rank, for jth school
						Rank = BasicFunctions.getInteger("School " + P.get(j).getName() + ": ", 1, rankings.length);
						//Go through each rankings k, of each student, make sure no duplicate of that school index for each rank
						for (int k = 0; k < rankings.length; k++){
							if (rankings[Rank-1] != -1 ){
									used = true;
									System.out.println("ERROR: Rank " + Rank + " already used!");
									System.out.println();
									break;
							}
							else used = false;
						}
					}while (used == true);
					
					this.setRanking(Rank-1, j);
				}
				System.out.println();
	}
	
	// print methods
	//OVERRIDED
	public void print ( ArrayList <? extends Participant > P ){
		
	}
	//NOTOVERRIDED
	public void printRankings ( ArrayList <? extends Participant > P ){
		// Print ranked lists 
		//If no rankings print a dash
		if (this.rankings.length == 0)
			System.out.print("-");
		
		else
		//Go through every rank
		for (int r = 0; r < this.rankings.length; r++){
			//For each rank, print corresponding name of the student index
			System.out.print(P.get(this.rankings[r]).getName());
			//Print out comma if not last student
			if (r<(this.rankings.length - 1))
				System.out.print(", ");
		}
	}

	public String getMatchNames ( ArrayList <? extends Participant > P){
		//Format the names of matches and return them
		StringBuilder stringBuilder = new StringBuilder();
		
		for (int i = 0; i<this.getNMatches();i++){
			//For each match, print corresponding name of the student index
			stringBuilder.append (P.get(this.getMatch(i)).getName());
			//Print out comma if not last student
			if (i<(this.getNMatches() - 1))
				stringBuilder.append (", ");
		}
		
		String matchedList = stringBuilder.toString();
		return matchedList;
	}

}
