/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author glee
 */
public class GCPNLPSample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Choose apiUrl depending on what you want to do: 
//        https://cloud.google.com/natural-language
        
        String apiUrl = "https://language.googleapis.com/v1/documents:analyzeSentiment?key="; // for analyze Sentiment
//        String apiUrl = "https://language.googleapis.com/v1/documents:analyzeSyntax?key="; // for analyze Syntax
        //String apiUrl = "https://language.googleapis.com/v1/documents:analyzeEntities?key="; // for Entity analysis
        String apiKey = "Your API Key from GCP";
        try {
            URL url = new URL(apiUrl + apiKey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.connect();

            // below is to make JSON for current POST request
            // see https://cloud.google.com/natural-language/docs/analyzing-entities "Protocol" part 
            // to see what request JSON looks like (as a part of curl command)
            
            // Request JSON is in the following format
            // {    
            //  'encodingType' : 'UTF8',
            //  'document': {
            //    'type': 'PLAIN_TEXT',
            //    'content': 'President Obama is speaking at the White House.'
            //  }        
            // }
            JSONObject data = new JSONObject();
            JSONObject document = new JSONObject();
            
            document.put("type", "PLAIN_TEXT");
//            document.put("content", "President Trump is speaking at the White House."); // change text for testing
            document.put("content", "Winter rain is gloomy. Spring is warm and bright."); // change text for testing
            data.put("document", document);
            data.put("encodingType", "UTF8");
            System.out.println(data.toJSONString()); // see if JSON is created correctly
            
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(data.toJSONString()); // send POST JSON request
            osw.close();
            
            // now result from GCP is to come
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String result = "", output;
            while ((output = br.readLine()) != null) {
                result += output;
                System.out.println(output); // print out result JSON from GCP
            }
         
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        
        // NOW YOU NEED TO PARSE JSON RETURNED TO FIND INFORMATION!!
    }
}
