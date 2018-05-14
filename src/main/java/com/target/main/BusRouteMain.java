package com.target.main;

import java.io.IOException;

import com.target.service.NextBus;

public class BusRouteMain {
	
	    public static long time;
	    public static String route;
	    public static String stop;
	    public static String direction;
	    
		public static String routeID;
		public static String directionID;
		public static String dir;
		public static String urlString;
		public static String stopID = "";
		public static String timeStamp = "";

	/**
	 * The main function of the program. Used primarily to make quick computations and then send off input to other functions
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{	
	//Check to make sure the program is run with three arguments
			if (args.length != 3)
	        {
	            System.out.println("Please enter the correct number of arguments.");
	            System.exit(-1);
	        }
			
			//Set the arguments for the route, stop and direction
	        route = args[0];
	        stop = args[1];
	        direction = args[2];
	        
			//Set the direction to the expected output to correctly match the JSON element
			if(direction.equals("north"))
			{
				dir = "NORTHBOUND";
			}
			else if(direction.equals("south"))
			{
				dir = "SOUTHBOUND";
			}
			else if(direction.equals("east"))
			{
				dir = "EASTBOUND";
			}
			else if (direction.equals("west"))
			{
				dir = "WESTBOUND";
			}
			else
			{
				//Bad Input
				System.out.println("The direction that was entered was invalid");
				System.exit(-1);
			}
	        		
	        //Find the route ID for the specific route that was entered and return the ID
	        urlString = "http://svc.metrotransit.org/NexTrip/Routes?format=json";
			routeID = NextBus.FetchInformation(urlString, "Description", "Route", route);
			
			//error checking to make sure we should continue
	        if(routeID == null)
	        {
	            System.out.println(route + " was not found.");
	            System.exit(-1);
	        }
	        
	        //Find the direction ID for the specific direction that was entered and return the ID
	        urlString = "http://svc.metrotransit.org/NexTrip/Directions/"+ routeID + "?format=json";
			directionID = NextBus.FetchInformation(urlString, "Text", "Value", dir);
	        
			//error checking to make sure we should continue
	        if(directionID == null)
	        {
	        	System.out.println(direction + " is incorrect for this route.");
	        	System.exit(-1);
	        }    
	        
	        //Find the stop ID String and set in global variable
	        urlString = "http://svc.metrotransit.org/NexTrip/Stops/" + routeID + "/"+ directionID +"?format=json";
	        stopID = NextBus.FetchInformation(urlString, "Text", "Value", stop);
	        
	        //error checking to make sure we should continue
	        if(stopID.equals(""))
	        {
	            System.out.println(stop + " was not found.");
	            System.exit(-1);
	        }
	        
	        //Find the timeStamp String and set in global variable
	        urlString = "http://svc.metrotransit.org/NexTrip/" + routeID + "/"+ directionID + "/" + stopID +"?format=json";
	        timeStamp = NextBus.FetchInformation(urlString, "RouteDirection", "DepartureTime", timeStamp);
	        
	        //error checking to make sure we should continue
	        if(timeStamp.equals(""))
		    {
		    	//The specification says that if the last bus of the day has already left to not return anything. So I exit clean here.
	        	System.out.println("No bus routes at this moment");
		    	System.exit(0);
		    }
	        
	        //compute the time and print the result
	        time = NextBus.ComputeTime(timeStamp);
	        if (time > 0) {			
				System.out.println(time + " minutes");
			}
		}
	
}
