import java.util.ArrayList;

public class SMPSolver {
	private ArrayList < Participant > S = new ArrayList < Participant >() ; // suitors
	private ArrayList < Participant > R = new ArrayList < Participant >() ; // receivers
	private double avgSuitorRegret ; // average suitor regret
	private double avgReceiverRegret ; // average receiver regret
	private double avgTotalRegret ; // average total regret
	private boolean matchesExist ; // whether or not matches exist
	private boolean stable ; // whether or not matching is stable
	private long compTime ; // computation time
	private boolean suitorFirst ; // whether to print suitor stats first

	public SMPSolver (){
		this.avgSuitorRegret = -1;
		this.avgReceiverRegret = -1;
		this.avgTotalRegret = -1;
		matchesExist = false;
		this.stable = false;
		this.compTime = -1;
		this.suitorFirst = true;
	}
	

	
	// getters
	public double getAvgSuitorRegret () { return this . avgSuitorRegret ; }
	public double getAvgReceiverRegret () { return this . avgReceiverRegret ; }
	public double getAvgTotalRegret () { return this . avgTotalRegret ; }
	public boolean matchesExist (){ return this.matchesExist; }
	public boolean isStable (){ return stable; }
	
	public long getTime () { return this.compTime; }
	public int getNSuitorOpenings (){
		// Student as suitor
		//Loop through every student
		int openings = 0;
		for (int i = 0; i < S.size(); i++){
			openings += this.S.get(i).getMaxMatches();
		}
		return openings;
	}
	public int getNReceiverOpenings (){
		// School as receiver
		//Loop through every school
		int openings = 0;
		for (int i = 0; i < R.size(); i++){
			openings += this.R.get(i).getMaxMatches();
		}
		return openings;
	}

	// setters
	public void setMatchesExist ( boolean b ){
			this.matchesExist = b;
	}
	
	public void setSuitorFirst ( boolean b ){
		if (b == true)
			this.suitorFirst = true;
		else 
			this.suitorFirst = false;
	}
	public void setParticipants ( ArrayList <? extends Participant > S , ArrayList <? extends Participant > R ){
		this.S = (ArrayList<Participant>) S;
		this.R = (ArrayList<Participant>) R;
	}
	

	//Returns arraylist of suitors
	public ArrayList <Participant> getSuitors (){
		ArrayList <Participant > Suitors = new ArrayList < Participant >();
		//Make copy of suitors
		Suitors = this.S;
		
		return Suitors;
	}
	
	//Returns arraylist of receivers
	public ArrayList <Participant> getReceivers (){
		ArrayList <Participant > Receivers = new ArrayList < Participant >();
		//Make copy of receivers
		Receivers = this.R;
		return Receivers;
	}
	
	// reset everything with new suitors and receivers
	public void reset ( ArrayList <? extends Participant > S , ArrayList <? extends Participant > R ){
		this.setParticipants(S, R);
		
		this.clearMatches();
		this.avgSuitorRegret = -1;
		this.avgReceiverRegret = -1;
		this.avgTotalRegret = -1;
		this.setMatchesExist(false);
		stable = false;
	}
	

	


	// methods for matching
	
	public void clearMatches (){
		// clear out existing matches
		for (int i = 0; i< S.size();i++){
			this.S.get(i).clearMatches();
		}
		for (int i = 0; i <R.size();i++){
				this.R.get(i).clearMatches();
		}
	}
	
	public boolean matchingCanProceed (){
		// check that matching rules are satisfied
		//Only match if equal number (not 0) of schools and students
		if (this.S.size()> 0 && this.R.size()> 0 && (getNSuitorOpenings () == getNReceiverOpenings()) ){
			return true;
		}
		
		else{
			this.print();
			return false;
		}
	}
	public boolean match (){
		long start = System . currentTimeMillis (); // Get current time
		//Array for each suitors' highest possible match(s), 0 is ranked 0 
		int[] highestMatch = new int [S.size()];
		
		for (int i = 0; i < highestMatch.length; i++){
			highestMatch[i] = 0;
		}
		while (!everyoneMatched()){

			//Loop through every suitor i, if suitor does not have maxMatches ask to marry highestMatch value starting at 0
			for (int i = 0; i <S.size(); i++){
				//If openings, suitor proposes
				if (!S.get(i).isFull()){
					//Suitor i proposes
					//If return true (receiver accepts)
					if (makeProposal (i,S.get(i).getIDAtRanking(highestMatch[i])) == true){
						
						//Confirm engagement, if receiver is full, break engagement off with receiver's worst match
						int oldSuitorID = -1;
						
						if (R.get(S.get(i).getIDAtRanking (highestMatch[i])).isFull()){
							oldSuitorID = R.get(S.get(i).getIDAtRanking (highestMatch[i])).getWorstMatch();
						}
						
						makeEngagement(i, S.get(i).getIDAtRanking(highestMatch[i]), oldSuitorID);
						
					}
					//If receiver rejects suitor, Suitor moves to next highest receiver
					else
					highestMatch[i]++;	
				}	
			}
		
		}
		//Matches made
		this.setMatchesExist(true);
		this.compTime = System . currentTimeMillis () -start ;
		return true;
		
	}
	
	
	private boolean makeProposal (int suitor , int receiver ){
		// Suitor proposes
		//If receiver has opening, receiver accepts
		if (!R.get(receiver).isFull())
			return true;
		//If receiver has no openings, check if receiver prefers this suitor over any of those the suitor is already matched with
		//If rank of receiver match(s) is worse than suitor, proposal succeeds

		else if (R.get(receiver).findRankingByID(R.get(receiver).getWorstMatch()) > R.get(receiver).findRankingByID(suitor))
			return true;
				
		return false;
	}
	
	private void makeEngagement (int suitor , int receiver , int oldSuitor){
		// make suitor - receiver engagement , break receiver - oldSuitor engagement
		
		//Break engagement if worst suitor is not -1
		//Remove engagement for old suitor, break engagement for receiver
		if (oldSuitor != -1) { 
			S.get(oldSuitor).unmatch(receiver);
			R.get(receiver).unmatch(oldSuitor);
		}
		
		//Make new engagement
		R.get(receiver).setMatch(suitor);
		S.get(suitor).setMatch(receiver);
	}
	
	
	public boolean everyoneMatched (){
		//If every suitor and school has 1 match, then everyone matched
		for (int i =0; i <S.size();i++){
			if (!S.get(i).isFull())
				return false;
		}		
		
		for (int i = 0; i < R.size(); i++){
			if (!R.get(i).isFull())	
				return false;
		}
		return true;
	}
	

	public boolean determineStability (){
		// calculate if a matching is stable
		//Does any school ranked higher than the school matched to the respective student, 
				//is also matched to a student that is lower ranked to the respective student?
				//If so, then unstable
			
				
				//Loop through each suitor
				for (int i = 0; i < S.size(); i++){
					
					//Loop through each suitor match
					for (int m = 0; m <S.get(i).getNMatches(); m++){
						//Determine ranking of matched receiver for the respective suitor, 
						int matchedReceiverRank= S.get(i).findRankingByID(S.get(i).getMatch(m));
					
						//Loop through each receiver
						//If j receiver has higher rank than matched receiver for respective suitor,
						//and any of j's matched suitor(s) is lower ranked than respective suitor => unstable
						for (int j = 0; j <R.size(); j++){
						
							//Loop through each school's match
						
							for (int k = 0; k < R.get(j).getNMatches(); k++){
								
								//Determine rank(s) of matched student for each school 
								int matchedSuitorRank = R.get(j).findRankingByID(R.get(j).getMatch(k));
						
								//If ranking of matched school is higher than other school
								if (S.get(i).findRankingByID(j) < matchedReceiverRank){
							
									//Calculate respective's student i ranking for higher ranked school
									int respectiveSuitorRank = R.get(j).findRankingByID(i);
							
									//If respective student rank is higher than higher school's matched student then unstable
									if (respectiveSuitorRank < matchedSuitorRank){
										stable = false;
										return false;
								}
							
								}			
						}
					}
					}
				}
				stable = true;
				return true;
	}

	// print methods
	public void print (){
		// print the matching results and statistics
		
		
		//Print Student Optimal Title
		if (this.suitorFirst){
			System.out.println();
			System.out.println("STUDENT-OPTIMAL MATCHING");
			System.out.println();
			
			//Errors if no matching
			if (S.size() == 0){
				System.out.println("ERROR: No suitors are loaded!");
				System.out.println();
			}	
			
			else if (R.size() == 0){
				System.out.println("ERROR: No receivers are loaded!");
				System.out.println();
			}
			else if (getNSuitorOpenings() != getNReceiverOpenings()){
				System.out.println("ERROR: The number of suitor and receiver openings must be equal!");
				System.out.println();
			}
			
			
		}
		
		//Print School Optimal Title
		if (!this.suitorFirst){
			
			System.out.println("SCHOOL-OPTIMAL MATCHING");
			System.out.println();

			//Errors if no matching
			if (S.size() == 0){
				System.out.println("ERROR: No suitors are loaded!");
				System.out.println();
			}	
				
			else if (R.size() == 0){
				System.out.println("ERROR: No receivers are loaded!");
				System.out.println();
			}
			else if (getNSuitorOpenings() != getNReceiverOpenings()){
				System.out.println("ERROR: The number of suitor and receiver openings must be equal!");
				System.out.println();
			}
			
		}
		
	}
	
	public void calcRegrets (){
		// calculate regrets
		
		double totalSuitorRegret = 0;
		double totalReceiverRegret = 0;
		
		//Loop through all students, find totalSuitorRegret
		for (int i = 0; i<S.size(); i++){
			S.get(i).calcRegret();
			totalSuitorRegret += S.get(i).getRegret();
		}
		
		//Loop through all receivers, find totalreceiverRegret
		for (int i =0; i <R.size(); i++){
			R.get(i).calcRegret();
			totalReceiverRegret += R.get(i).getRegret();
		}
		double totalRegret = totalSuitorRegret + totalReceiverRegret;
		//Find avg suitor,receiver, total regret
		this.avgSuitorRegret  = totalSuitorRegret / S.size();
		this.avgReceiverRegret = totalReceiverRegret / R.size();
		this.avgTotalRegret = (totalRegret)/(S.size() + R.size());
	}
	
	public void printMatches (){
		// print matches
		System.out.println("Matches:");
		System.out.println("--------");
		
		
		//Print school first, then matched student(s)
		if (suitorFirst){
			for (int i = 0; i < R.size(); i++){
				System.out.print (R.get(i).getName() + ": ");
				System.out.println (R.get(i).getMatchNames(S));
			}
		}
		//Print school first, then matched student(s)
		if (!suitorFirst){
			for (int i = 0; i < S.size(); i++){
				System.out.print (S.get(i).getName() + ": ");
				System.out.println (S.get(i).getMatchNames(R));
			}
			System.out.println();
		}
	}
	
	public void printStats (){
		//Determine if stable
		this.determineStability();
		if (isStable())
			System.out.println("Stable matching? Yes");
		else System.out.println("Stable matching? No");

		//Calculate and print regrets
		this.calcRegrets();
		//Output avg student,school,total regret
		System.out.format("Average suitor regret: %.2f",this.avgSuitorRegret);
		System.out.println();
		System.out.format("Average receiver regret: %.2f",this.avgReceiverRegret);
		System.out.println();
		System.out.format("Average total regret: %.2f",this.avgTotalRegret);
		System.out.println();
	}
	
	public void printCompTime(){
		System.out.println();
		System.out.println(Math.max(S.size(),R.size()) +" matches made in " + this.compTime + "ms!" );
		System.out.println();
	}
	
	public void printStatsRow ( String rowHeading ){
		// print stats as row
	}
}