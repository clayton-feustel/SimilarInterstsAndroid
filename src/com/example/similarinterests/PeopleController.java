package com.example.similarinterests;

import java.util.LinkedList;
import java.util.List;

public class PeopleController {

	public static List<Person> fakePeople;
	
	public PeopleController(){
		fakePeople = new LinkedList<Person>();
			fakePeople.add(new Person("Gob Bluth", 10, 5, 2000f, 1000f, 210f, "Ride a segway"));
			fakePeople.add(new Person("Korra Sato", 50, 2, 8000f, 500f, 190f, "Probend"));
			fakePeople.add(new Person("Ash Ketchum", 900, 500, 5400f, 2200f, 110f, "Catch'em all"));
			fakePeople.add(new Person("Tobias Bluth", 440, 440, 2000f, 5000f, 130f, "Visit the Gothic Castle"));
			fakePeople.add(new Person("Jerry Smith", 220, 220, 3000f, 9000f, 210f, "Sit around"));
	}
	
	public LinkedList<Person> findSimilarPeople(){
		LinkedList<Person> matches = new LinkedList<Person>();
		
		if(HealthData.getTodaysCalories() > HealthData.getAverageCalories()){
			for(Person possibleMatch : fakePeople){
				if(possibleMatch.getTodaysCalories() > possibleMatch.getAverageCalories())
					matches.add(possibleMatch);
			}
		}
		else{
			for(Person possibleMatch : fakePeople){
				if(possibleMatch.getTodaysCalories() < possibleMatch.getAverageCalories())
					matches.add(possibleMatch);
			}
		}
		
		return matches;
	}

}
