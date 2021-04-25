// I have neither given nor received unauthorized aid on this assignment. 
import java.util.ArrayList; 

public class Person {

	private String name; // Unique to each person in the file
	private ArrayList<String> friendsList; // ArrayList bc everyone has a different number of friends
	private boolean explored; // Indicates whether the algorithm has already explored this person 
	private String predecessor; // Name of the previous person in the chain
	
	//*****CONSTRUCTOR*****
	public Person(String name) {
		this.name = name; 
		explored = false; 
		friendsList = new ArrayList<String>(); 
	}
	
	//*****SETTER METHODS*****
	public void setName(String name) {
		this.name = name; 
	}
	
	public void setFriendsList(ArrayList<String> friends) {
		this.friendsList = friends; 
	}
	
	public void setExplored(boolean explored) {
		this.explored = explored; 
	}
	
	public void setPredecessor(String predecessor) {
		this.predecessor = predecessor; 
	}
	
	//*****GETTER METHODS*****
	public String getName() {
		return this.name; 
	}
	
	public ArrayList<String> getFriendsList(){
		return this.friendsList; 
	}
	
	public boolean getExplored() {
		return this.explored; 
	}
	
	public String getPredecessor() {
		return this.predecessor; 
	}
	
	//*****toString*****
	// Primarily used for debugging and a neat print 
	public String toString() {
		return this.name; 
	}
	
}
