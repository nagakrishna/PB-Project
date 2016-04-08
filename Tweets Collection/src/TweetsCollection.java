//reference: "http://www.jeffkuchta.com/Tutorial/2013/02/21/java--twitter-public-streams-with-support-from-heroku-spark--oauth/"


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

public class TweetsCollection extends Thread{
	
	 private static final String STREAM_URI = "https://stream.twitter.com/1.1/statuses/filter.json";

	 StringBuilder stringBuilder;
	    public void run(){
	        try{
	            System.out.println("Starting Twitter public stream consumer thread.");

	            // Enter your consumer key and secret below
	            OAuthService service = new ServiceBuilder()
	                    .provider(TwitterApi.class)
	                    .apiKey("tVn4cS3jIkr6Z0jFo4sH9OR62")
	                    .apiSecret("v1PoT7YSA6zX8SDbr3Y6Y5CHxYBxpsiAp21R19p4W94kjVIPjy")
	                    .build();

	            // Set your access token
	            Token accessToken = new Token("3524247253-o7zXFn3r8PwPb4IUTYqHRQzVabTzfoFn9Ck7FzW", "V7zkLiJmF0vsdX8rUdkXo3q1ha452vKqVtB5OoCuQWAgR");

	            // Let's generate the request
	            System.out.println("Connecting to Twitter Public Stream");
	            OAuthRequest request = new OAuthRequest(Verb.POST, STREAM_URI);
	            request.addHeader("version", "HTTP/1.1");
	            request.addHeader("host", "stream.twitter.com");
	            request.setConnectionKeepAlive(true);
	            request.addHeader("user-agent", "Twitter Stream Reader");
	            request.addBodyParameter("track", "US Elections, Donald Trump, Bernie Sanders, Hillary Clinton, Roque De La Fuente"); // Set keywords you'd like to track here
	            service.signRequest(accessToken, request);
	            Response response = request.send();

	            // Create a reader to read Twitter's stream
	            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getStream()));
	            int c=0;
	            String line;
	            stringBuilder = new StringBuilder();
	            while ((line = reader.readLine()) != null) {
	            	if(line!=null){
	            		System.out.println(c + line);
		                stringBuilder.append(line + "\n");
	            	}
	                c++;
	                if(c>10000){
	                	break;
	                }
	            }
	        }
	        catch (IOException ioe){
	            ioe.printStackTrace();
	        }
	        finally
	        { 
	     	   try{
	     	   File file = new File("E:/PB/US/new/temp1.txt");
	            if(!file.exists())
	            {
	            	file.createNewFile();
	            }
	            FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
	             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	             bufferedWriter.write(stringBuilder.toString());
	             bufferedWriter.close();
	             System.out.println("Write Completed");
	     	   }
	     	   catch (Exception ex)
	     	   {
	     		   
	     	   }
	        }
	    }


}
