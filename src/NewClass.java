/*
 * 
 */

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class NewClass {

	private static final String TARGET_URL =
            "https://vision.googleapis.com/v1/images:annotate?";
	private static final String API_KEY =
	               "key=AIzaSyC1pOM5vJCyBet4Agqw-BH_R7Rv9wNCpGI";
	
	public static void main(String[] args) throws IOException {
		boolean containsText = false;
		boolean incorrectFile = false;
		
		URL serverUrl = new URL(TARGET_URL + API_KEY);
		URLConnection urlConnection = serverUrl.openConnection();
		HttpURLConnection httpConnection = (HttpURLConnection)urlConnection;
		httpConnection.setRequestMethod("POST");
		httpConnection.setRequestProperty("Content-Type", "application/json");
		httpConnection.setDoOutput(true);
		BufferedWriter httpRequestBodyWriter = new BufferedWriter(new
        OutputStreamWriter(httpConnection.getOutputStream()));
		/*
		 * This code can be edited for 3 different results
		 * 1. demo-image.jpg is an image that contains NO text
		 * 2. notimage.docx is not a image file.
		 * 3. text.png is a text image.
		 * To change the file adjust " "gs://teamaaron/notimage.docx\" accordingly.
		 */
		httpRequestBodyWriter.write
		 ("{\"requests\":  [{ \"features\":  [ {\"type\": \"TEXT_DETECTION\""
		 +"}], \"image\": {\"source\": { \"gcsImageUri\":"
		 +" \"gs://teamaaron/notimage.docx\"}}}]}");
		
		
		httpRequestBodyWriter.close();
		String response = httpConnection.getResponseMessage();
		if (httpConnection.getInputStream() == null) {
			   System.out.println("No stream");
			   return;
			}
			Scanner httpResponseScanner = new Scanner (httpConnection.getInputStream());
			String resp = "";
			while (httpResponseScanner.hasNext()) {
			   String line = httpResponseScanner.nextLine();
			   resp += line;
			   //System.out.println(line);  //  alternatively, print the line of response
			   if(line.contains("description"))
			   {
				   containsText = true;
			   }
			   if(line.contains("Bad image data"))
			   {
				   incorrectFile = true;
			   }
			}
			if(containsText == true && incorrectFile == false)
			{
				System.out.println("Image contains text.");
			}
			else if(containsText == true && incorrectFile == false)
			{
				System.out.println("Image does not contain text.");
			}
			else if(incorrectFile == true)
			{
				System.out.println("Incorrect File Type.");
			}
			httpResponseScanner.close();
	}

}