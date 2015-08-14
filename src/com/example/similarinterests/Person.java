package com.example.similarinterests;

public class Person {
	
	private String activity;
	private String name;
	private int averageSteps;
	private int todaySteps;
	private float averageCalories;
	private float todayCalories;
	private float weight;
	
	public Person(String name, int averageSteps, int todaySteps, float averageCals, float todayCals, float weight, String message){
		this.name = name;
		this.averageSteps = averageSteps;
		this.todaySteps = todaySteps;
		this.averageCalories = averageCals;
		this.todayCalories = todayCals;
		this.weight = weight;
		this.activity = message;
	}
	
	@Override
	public String toString(){
		return activity + " with " + name;
	}
	
	public float getTodaysCalories(){
		return todayCalories;
	}
	
	public float getAverageCalories(){
		return averageCalories;
	}
}
