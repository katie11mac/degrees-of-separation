I have neither given nor received unauthorized aid on this assignment. -Katie Macalintal 


Obvious Aspects of Program: 
Person.java; readFile(String file); resetEveryExplored(); getPerson()


Non-Obvious Aspects of the Program: 
Driver.java 
main(): When communicating the results of the search between the two people the user queried about, there are conditionals that allow information to be printed in the appropriate locations as specified in the instructions. 


getQuery(): Handles the queries that the user may enter. It ensures that their queries (eg. the person whose relations they want to know more about) are actually people in the population. If the person they enter is not in the population, then the Person object of the String they entered would be null. The while loop checks if the person they entered is actually in the population and prompts them to enter a new name while their person cannot be found. 


search(Person A, Person B): Followed the algorithm outlined in the guidelines. At the end of this search() method, the resetEveryExplored() method is called, which resets every Person’s explored data field to false. Calling this method ensures that no person is skipped in the next query (eg. every person that needs to be explored will be explored in the next query). 


getChainRelations(ArrayList<String> chainList): The primary function of this method is to create a String of the chained relations between two people in the same order that the user had queried about them, since my search() method stores the chain of relations in backwards order (eg. from the second person they queried about to the first). This also makes it easier to print the relation to both the console and output file later. 


averageDegreeOfSeparation(): Uses a 2D integer array as specified through the guidelines to determine the average degree of separation between each pair of people. This 2D array is then populated with the degrees of separation through a nested for loop. In this nested for loop, there are also conditions that ensure the degrees of separation between two unconnected people is the size of the population plus one. Instead of reiterating through the entire 2D array after filling it all, the totalDegrees is added throughout the process of running the search method between each pair of people. The totalDegrees must be stored as a double, so that later when finding the average degrees of separation floor division does not take place. 


Bugs
* When the user is asked if they want to try another query, if they enter any word that begins with the letter “y” then another round of querying will begin. If they enter any word that does not begin with “y” then the querying will end. 
* If the user inquires about a pair of connected people more than once, then that pair of relations will be printed to the outfile the number of times they queried about them. 
* For the Peg to Mary connection test, the connection will include Bobby instead of Patrick, which is still valid, but does not align with what is outlined in the guidelines.