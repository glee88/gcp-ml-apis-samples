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
public class GCPVisionSampleWithURI {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String apiUrl = "https://vision.googleapis.com/v1/images:annotate?key=";
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
            // see https://cloud.google.com/vision/docs/quickstart "Try This API" part 
            // to see what request JSON looks like
            
            // jsonArray.add
            // jsonObj.put
            
            JSONObject JSON = new JSONObject();
            JSONArray requestsArray = new JSONArray();
            JSONObject request = new JSONObject();
            JSONArray featuresArray = new JSONArray();
            JSONObject type = new JSONObject();
            JSONObject source = new JSONObject();
            JSONObject imageUri = new JSONObject();
            imageUri.put("imageUri", "gs://320bucket/demo-img.jpg"); // if URL of image is to be used
//            imageUri.put("imageUri", "gs://320bucket/fake_ai_faces.png"); // if URL of image is to be used
            source.put("source", imageUri); // if URL of image is to be used
            request.put("image", source);
            // the following code is to make the following request JSON with imageURI
//            {
//                "requests": [
//                {
//                    "features": [
//                    {
//                        "type": "LABEL_DETECTION"
//                    }
//                    ],
//                    "image": {
//                        "source": {
//                              "imageUri": "gs://bucket-name-123/demo-image.jpg"
//                        }
//                    }
//                }
//                ]
//            }
            // See https://cloud.google.com/vision/docs/drag-and-drop to find out what can be detected
            // type: FACE_DETECTION, TEXT_DETECTION, LABEL_DETECTION, WEB_DETECTION
            type.put("type", "LABEL_DETECTION");
            featuresArray.add(type); 
            
            request.put("features", featuresArray);
            requestsArray.add(request);
            JSON.put("requests", requestsArray);
            System.out.println(JSON.toJSONString()); // to see if it generates correct JSON

//          program -------------------> GCP REST API
//                   output stream (input to GCP:JSON)

//                  <------------------
//                     input stream (output from GCP)

            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(JSON.toJSONString()); // send POST JSON request
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
            JSONParser parser = new JSONParser();
            JSONObject jobjResponseFromServer = (JSONObject) parser.parse(result);
            JSONObject jobj1 = (JSONObject) ((JSONArray) jobjResponseFromServer.get("responses")).get(0);
            // 
            JSONArray annotations = (JSONArray) jobj1.get("labelAnnotations");
            if (annotations == null) {
                System.out.println("Nothing detected");
                return;
            }
            
            for (int i=0;i<annotations.size();i++)
            {
                JSONObject anot = (JSONObject) annotations.get(i);
                System.out.println("Description: " + anot.get("description"));
            }
            //System.out.println("Locale: " + info.get("locale"));
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occured");
        }
        
    }
}
