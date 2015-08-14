package com.example.similarinterests;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NotificationActivity extends Fragment {

	private static List<String> availableActivities;
	private static ArrayAdapter<String> activitiesListViewAdapter;
	private static View view;
	
	public NotificationActivity() {
		availableActivities = new ArrayList();
			availableActivities.add("No Active Invitations");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		activitiesListViewAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_expandable_list_item_1, availableActivities);
		
		ListView activitesListView = (ListView) rootView.findViewById(R.id.activitiesList);
		activitesListView.setAdapter(activitiesListViewAdapter);
		
		view = rootView;
		
		return rootView;
	}
	
	public static void updateNotifications(){
		PeopleController peopleController = new PeopleController();
		LinkedList<Person> matches = peopleController.findSimilarPeople();
		
		if(matches.size() > 0){
			availableActivities.clear();
			for(Person person : matches){
				availableActivities.add(person.toString());
			}
		}
		
		activitiesListViewAdapter.notifyDataSetChanged();
		setUserMessage();
	}
	
	private static void setUserMessage(){
		TextView healthMessage = (TextView)view.findViewById(R.id.healthDescriptionLabel);
		
		String message = "You need a little more exercise. Why don't you: ";
		if(HealthData.getTodaysCalories() > HealthData.getAverageCalories()){
			message = "You have exceeded your average. Why don't celebrate by: ";
		}
		
		healthMessage.setText(message);
	}
}
