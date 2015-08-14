package com.example.similarinterests;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

public class HealthData {

	private static Calendar calendar;
	private static float weight;
	private static float todayCalories;
	private static float weekCalories;
	private static float caloriesAverage;
	private static int todaySteps;
	private static int weekSteps;
	private static int stepsAverage;
	
	public static void requestCurrentWeight(GoogleApiClient mClient){
		weight = 0;
		calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			
		long endTime = calendar.getTimeInMillis();
		//Subtract away the length of time you would like to grab data from
		calendar.add(Calendar.DATE, -1);
		long startTime = calendar.getTimeInMillis();
		
		DataReadRequest weightRequest = new DataReadRequest.Builder()
							.aggregate(DataType.TYPE_WEIGHT, DataType.AGGREGATE_WEIGHT_SUMMARY)
							.bucketByTime(1, TimeUnit.DAYS)
							.setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
							.build();
		
		Fitness.HistoryApi.readData(mClient, weightRequest)
							.setResultCallback(new ResultCallback<DataReadResult>(){

								@Override
								public void onResult(DataReadResult result) {
									for (Bucket bucket : result.getBuckets()) {
										
										DataSet data = bucket.getDataSet(DataType.AGGREGATE_WEIGHT_SUMMARY);
										
										for (DataPoint dp : data.getDataPoints()){
											
											weight += dp.getValue(Field.FIELD_AVERAGE).asFloat() * 2.2046f;
										}
									}
									
									HealthDataActivity.updateHealthData();
									NotificationActivity.updateNotifications();
								}
			
		});
		
	}
	
	public static void requestBurnedCalories(GoogleApiClient mClient){
		todayCalories = 0;
		weekCalories = 0;
		calendar = Calendar.getInstance();
			calendar.setTime(new Date());
		
		long endTime = calendar.getTimeInMillis();
		//Subtract away the length of time you would like to grab data from
		calendar.add(Calendar.DATE, -5);
		long startTime = calendar.getTimeInMillis();
		
		
		DataReadRequest caloriesRequest = new DataReadRequest.Builder()
							.aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
							.bucketByTime(1, TimeUnit.DAYS)
							.setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
							.build();
		
		Fitness.HistoryApi.readData(mClient, caloriesRequest)
							.setResultCallback(new ResultCallback<DataReadResult>(){
					
								@Override
								public void onResult(DataReadResult result) {
									List<Bucket> buckets = result.getBuckets();
									int counter = 1;
									for (Bucket bucket : buckets) {
										
										DataSet data = bucket.getDataSet(DataType.AGGREGATE_CALORIES_EXPENDED);
										
										for (DataPoint dp : data.getDataPoints()){
											
											float val = dp.getValue(Field.FIELD_CALORIES).asFloat();
											if (buckets.size() == counter){
												todayCalories += val;
											}
											weekCalories += val;
										}
										counter += 1;
									}
									
									caloriesAverage = weekCalories / buckets.size();
									HealthDataActivity.updateHealthData();
									NotificationActivity.updateNotifications();
								}

							});
	}
	
	public static void requestSteps(GoogleApiClient mClient){
		todaySteps = 0;
		weekSteps = 0;
		calendar = Calendar.getInstance();
			calendar.setTime(new Date());
		
		long endTime = calendar.getTimeInMillis();
		//Subtract away the length of time you would like to grab data from
		calendar.add(Calendar.DATE, -5);
		long startTime = calendar.getTimeInMillis();
		
		
		DataReadRequest caloriesRequest = new DataReadRequest.Builder()
							.aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
							.bucketByTime(1, TimeUnit.DAYS)
							.setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
							.build();
		
		Fitness.HistoryApi.readData(mClient, caloriesRequest)
							.setResultCallback(new ResultCallback<DataReadResult>(){
					
								@Override
								public void onResult(DataReadResult result) {
									List<Bucket> buckets = result.getBuckets();
									int counter = 1;
									for (Bucket bucket : buckets) {
										
										DataSet data = bucket.getDataSet(DataType.AGGREGATE_STEP_COUNT_DELTA);
										
										for (DataPoint dp : data.getDataPoints()){
											
											int val = dp.getValue(Field.FIELD_STEPS).asInt();
											if (buckets.size() == counter){
												todaySteps += val;
											}
											weekSteps += val;
										}
										counter += 1;
									}
									
									stepsAverage = weekSteps / buckets.size();
									HealthDataActivity.updateHealthData();
									NotificationActivity.updateNotifications();
								}

							});
	}
	
	public static float getWeight(){
		return weight;
	}
	
	public static float getWeeksCalories(){
		return weekCalories;
	}
	
	public static float getAverageCalories(){
		return caloriesAverage;
	}
	
	public static float getTodaysCalories(){
		return todayCalories;
	}
	
	public static int getTodaySteps(){
		return todaySteps;
	}
	
	public static int getWeekSteps(){
		return weekSteps;
	}
	
	public static int getAverageSteps(){
		return stepsAverage;
	}
}
