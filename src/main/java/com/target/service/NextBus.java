package com.target.service;

/**
 * Description: 	This program returns the time in minutes for a bus on "BUS ROUTE" leaving from "BUS STOP NAME" going "DIRECTION" using the API at http://svc.metrotransit.org/
 */
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import com.google.gson.*;

public class NextBus {
	
   
	/**
	 * Computes the time based on the UTC value given through the GetTimepointDepartures function
	 */
	public static long ComputeTime(String timeStamp)
	{
		//get the portion of the timeStamp we need and convert it to a long
		timeStamp = timeStamp.substring(6,19);
		Long longTime = Long.valueOf(timeStamp).longValue();
		Date currentDate = new Date();

		//take the difference between the current time and the departure time (longTime). Divide by 60000 to account for milliseconds and minutes
		long timeTillBus = (longTime-currentDate.getTime())/60000;
		return timeTillBus;
	}
	
	/**
	 * @param Url 				the full String url where we are sending the GET request to in order to recieve a Json object
	 * @param ElementOne		used to search through the json object: RouteDirection, Text or Description
	 * @param ElementTwo		used to search through the json object: DepartureTime, Value, Route
	 * @param compareString		used to ensure we return a String when using this method: route, dir, stop, timeStamp
	 * @return					the int value we are searching for, -1 if FetchInformation fails and 0 if we return a string
	 */
	public static String FetchInformation(String Url, String ElementOne, String ElementTwo, String compareString)
	{
		//set up a connection to get ready to send a GET request to the url that is passed in
		URL url = null;
		HttpURLConnection request;
		try
		{
			url = new URL(Url);
		    request = (HttpURLConnection)url.openConnection();
		    request.setDoOutput(true);
		    request.setRequestMethod("GET");
		    
		    //we will recieve a Json Array and extract the element from it
		    request.connect();
		    JsonParser jp = new JsonParser();
		    JsonElement element = jp.parse(new InputStreamReader((InputStream)request.getInputStream()));
		    
		    //check to make sure our data will be valid before parsing commences
		    if (request.getResponseCode() != HttpURLConnection.HTTP_OK)
		    {
		    	System.out.println(request.getErrorStream());
		    }

		    JsonArray jsonArrayObj = element.getAsJsonArray();
		    
		    //parse the elements in the array and return either an int or a string depending on input
		    for (JsonElement obj : jsonArrayObj)
		    {
		    	if(obj.getAsJsonObject().get(ElementOne).getAsString().contains(compareString))
		    	{
		    		return obj.getAsJsonObject().get(ElementTwo).getAsString();
		    	}
			}
		}
		catch(IOException e)
		{
			System.out.println("Caused an IOException");
			e.printStackTrace();
		}
		return null;
	}
}
