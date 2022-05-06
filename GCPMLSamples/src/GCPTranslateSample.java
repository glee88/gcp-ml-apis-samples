/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import javax.imageio.ImageIO;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author glee
 */
public class GCPTranslateSample {
    public static void main(String[] args)
    {
        String apiUrl = "https://translation.googleapis.com/language/translate/v2?key=";
        String apiKey = "Your API Key from GCP";
        
        try {
            String textToTranslate = "Hello%20world"; // sample text
            String targetlang = "es";
            URL url = new URL(apiUrl + apiKey + "&q=" + textToTranslate + "&target="+targetlang);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.connect();

            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.close();
            
            // now result from GCP is to come
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String result = "", output;
            while ((output = br.readLine()) != null) {
                result += output;
                System.out.println(output); // print out result JSON from GCP
            }
            
            // below is to parse JSON returned
            // JSON format is going to be different from what detection was asked. 
            // See printed JSON to see what output is like. Use it to parse for your own project.
//            JSONParser parser = new JSONParser();
//            JSONObject jobj = (JSONObject) parser.parse(result);
//            JSONObject jobj1 = (JSONObject) ((JSONArray) jobj.get("responses")).get(0);
//            // 
//            JSONArray annotations = (JSONArray) jobj1.get("labelAnnotations");
//            if (annotations == null) {
//                System.out.println("Nothing detected");
//                return;
//            }
//            
//            for (int i=0;i<annotations.size();i++)
//            {
//                JSONObject anot = (JSONObject) annotations.get(i);
//                System.out.println("Description: " + anot.get("description"));
//            }
//            //System.out.println("Locale: " + info.get("locale"));
            conn.disconnect();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            
            System.out.println("An error occured");
        }
    }
}
