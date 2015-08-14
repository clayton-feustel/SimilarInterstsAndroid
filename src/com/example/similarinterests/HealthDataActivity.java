package com.example.similarinterests;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HealthDataActivity extends Fragment {

		private static View view;
	
		public HealthDataActivity() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.health_data_layout, container,
					false);
			
			view = rootView;
			updateHealthData();
			
			return rootView;
		}
		
		public static void updateHealthData(){
			if(view != null){
				TextView weightLabel = (TextView)view.findViewById(R.id.weightLabel);
				TextView weekCalLabel = (TextView)view.findViewById(R.id.caloriesWeekLabel);
				TextView todaysCals = (TextView)view.findViewById(R.id.todaysCaloriesLabel);
				TextView weekSteps = (TextView)view.findViewById(R.id.stepsWeekLabel);
				TextView todaySteps = (TextView)view.findViewById(R.id.todayStepsLabel);
				
				weightLabel.setText(String.format("%.0f pounds", HealthData.getWeight()));
				weekCalLabel.setText(String.format("%.2f calories", HealthData.getAverageCalories()));
				todaysCals.setText(String.format("%.2f calories", HealthData.getTodaysCalories()));
				weekSteps.setText(String.format("%d steps", HealthData.getAverageSteps()));
				todaySteps.setText(String.format("%d steps", HealthData.getTodaySteps()));
			}
		}
}
