// I have neither given nor received unauthorized aid on this assignment. 
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Driver {

	// GLOBAL VARIABLES - used by multiple methods 
	static ArrayList<Person> peeps; // Population of people 
	static Scanner scan; 
		
	public static void main(String[] args) throws IOException{
		System.out.println("** Six Degree of Separation **");
		
		// Setting up the population 
		peeps = new ArrayList<Person>(); 
		readFile("friends.txt"); 
		// Setting up the out file
		BufferedWriter out = new BufferedWriter(new FileWriter (new File("output.txt")));
		out.write("Queries: \n");
		
		
		// Loop for querying 
		boolean querying = true; 
		while (querying == true) {
			scan = new Scanner(System.in); 
			
			// Handling their queries 
			// ASSUMPTION: User will upper case the first letter of the name and lower case the rest
			
			System.out.println("Enter the name of the first person: ");
			String personA = getQuery(); 
			
			System.out.println("Enter the name of the second person: ");
			String personB = getQuery(); 
			
			// Store the results of their relations
			ArrayList<String> searchResults = search(getPerson(personA), getPerson(personB)); 

			
			// Must account for when the user enters the same person or two people w/ no connection 
			// If the results are greater than 1, then that means the two people are connected 
			// since there is more than 1 person in the ArrayList
			if (searchResults.size() > 1) { 
				// Inform user of connection 
				System.out.print("Relation: ");
				System.out.println(getStringChainRelations(searchResults));  
				System.out.println("Degrees of Separation: " + (searchResults.size()-1));
				// Only want to print to out file when the two queried people have a connection 
				out.write(getStringChainRelations(searchResults) + "\n");
			// If the results do not contain more than one person in the list
			// means that they are not connected or the same person 
			} else {  
				// If they are the same person 
				if (searchResults.get(0).equals(personA) && searchResults.get(0).equals(personB)) 
					System.out.println("They are the same person.");
				else // They are no connected 
					System.out.println("The two people entered are not connected.");
			}
			
			// Checking if the user wants to continue querying 
			System.out.println();
			System.out.println("Want to try another query? (y/n)");
			char response = scan.nextLine().toLowerCase().charAt(0); 
			
			if (response == 'y') 
				querying = true; 
			else // Will stop the querying process if they enter any word that does not begin with y 
				querying = false; 
			
			System.out.println();
		}
		
		// Compute the average degree of separation and print to appropriate locations 
		double averageDegrees = averageDegreeOfSeparation(); 
		
		System.out.println("Average Degree of Separation of entire group: " + averageDegrees);
		out.write("Average Degree of Separation of entire group: " + averageDegrees); 
		
		scan.close(); 
		out.close(); 
	}
	
	
	/*
	 * Read from the file and create Person objects for each person in the file 
	 */
	public static void readFile(String file) throws IOException{
		Scanner fr = new Scanner(new File(file)); // Create file object and scanner at the same time
			
		// Parsing through the file 
		while(fr.hasNextLine()) { 
			
			String line = fr.nextLine(); // Read one line at a time 
			String [] names = line.split("\t"); // Split each line at a tab (tab separates one person from another in the file) 
			
			// First name is the Person object 
			Person person = new Person(names[0]); 
				
			// Rest of the names are the person's friends (friendsList) 
			for (int i = 1; i<names.length; i++) { // Start at index 1 bc index 0 was our Person object
				person.getFriendsList().add(names[i]);  
			}
			
			peeps.add(person); // Add the person to the population
		}
		
		System.out.println("Filing reading completed.");
		fr.close();
		
	}
	
	
	/*
	 * Get and handle the users query 
	 */
	public static String getQuery() {
		
		String personString = scan.nextLine().trim(); 
		// Ensuring that the user entered someone in the population 
		// Will keep prompting them until they enter someone in the population 
		while (getPerson(personString) == null) {
			System.out.println("Error: " + personString + " is not found.");
			System.out.println("Enter the name of a person in the population for the previous query: ");
			personString = scan.nextLine().trim(); 
		}
		
		return personString;
	}
	
	/*
	 * Search the population and see if the two people are connected. 
	 * Returns the chain of how they are connected (starting with the second person they queried about. 
	 */
	public static ArrayList<String> search(Person A, Person B){
		// Initializing the people we are working with 
		A.setExplored(false); 
		A.setPredecessor(null);
		
		B.setExplored(false);
		B.setPredecessor(null);
		
		boolean found = false; // Whether Person B has been found yet 
		ArrayList<Person> exploreList = new ArrayList<Person>(); // List of people to explore 
		
		exploreList.add(A); 
		A.setExplored(true); 
		
		// This algorithm ensures that we get the shortest chain link between two people 
		while (exploreList.size() > 0 && found == false) { 
			
			Person x = exploreList.get(0);
			exploreList.remove(0); // Want this list to be shrinking as you iterate through it 
			if (x.getName()==B.getName()) // They are the same person 
				found = true; 
			else {
				for (int i = 0; i < x.getFriendsList().size(); i++) {
					Person y = getPerson(x.getFriendsList().get(i)); 
					if (y.getExplored() == false) {
						exploreList.add(y); 
						y.setExplored(true);
						y.setPredecessor(x.getName());
					}
				}
			} 
		}
		
		// Finding the chain of acquaintances - start from the end and work way back to beginning  		
		ArrayList<String> chainList = new ArrayList<String>(); 
		String current = B.getName(); 
		while (current != null) {
			chainList.add(current); 
			current = getPerson(current).getPredecessor(); 
		}
		
		// Need to reset every Person's explored variable so that in the next query no one is skipped 
		resetEveryExplored(); 
		return chainList; 
	}
	
	/*
	 * Reset every Person's explored data field to false
	 */
	public static void resetEveryExplored() {
		for (int i = 0; i < peeps.size(); i++) {
			peeps.get(i).setExplored(false);
		}
	}
	
	/*
	 * Convert the chainList of relations between two people to a String. 
	 * Will return in the order of how the user queried. 
	 */
	public static String getStringChainRelations(ArrayList<String> chainList) {	
		String chainRelations = ""; 
		// Go through and add the Persons backwards since 
		// since the chainList returned from the search method starts with the second person the user queried about 
		for (int i = chainList.size() - 1; i >= 0; i--) {
			chainRelations += chainList.get(i) + " "; 
		} 
		
		return (chainRelations); 
	}
	
	/*
	 * Compute the average degree of separation of the entire group
	 */
	public static double averageDegreeOfSeparation() throws IOException {
		
		System.out.println("Computing Average Degree of Separation...");
		
		double totalDegrees = 0; // Must be stored as a double so floor division does not take place later
		int[][] connectArray = new int[peeps.size()][peeps.size()]; // Will store the degree of separation between each pair of people 
		
		// Iterate and fill the connectArray with the degree of separation between every pair of people in the population 
		for (int row = 0; row < connectArray.length; row++) {
			Person personR = peeps.get(row); 
			for(int col=0; col < connectArray[row].length; col++) {
				Person personC = peeps.get(col); 
				ArrayList<String> searchResults = search(personR, personC);
				// Add the regular degrees of separation if 2 people are connected 
				if (searchResults.size() > 1) {
					connectArray[row][col] = searchResults.size()-1;
					totalDegrees += searchResults.size()-1; 
				} else { // Only 1 person in their chain results (either the same person or not connected) 
					if (personR.equals(personC)) // If the same person (object) 
						connectArray[row][col] = 0; 
					else {// If not connected 
						connectArray[row][col] = peeps.size() + 1; 
						totalDegrees += peeps.size() + 1; 
					}
				}
			}
		}
		
		// Computing and printing out the average degree of separation 
		double averageDegrees = totalDegrees / (peeps.size() * (peeps.size()-1)); 
		return averageDegrees; 
	}
	
	/*
	 * Returns the Person object of a given name 
	 */
	public static Person getPerson(String name) {
		
		for (int i = 0; i < peeps.size(); i++) {
			if (peeps.get(i).getName().equals(name))
				return(peeps.get(i)); 
		}
		return null; // Person with name not found 
	}
	

}
