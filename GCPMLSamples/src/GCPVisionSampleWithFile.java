/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author glee
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
public class GCPVisionSampleWithFile {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String apiUrl = "https://vision.googleapis.com/v1/images:annotate?key=";
        String apiKey = "Your API Key from GCP"; 
        // file path has to be changed to the path of image file on YOUR computer
        File file = new File("C:\\Users\\glee\\OneDrive - Lander University\\Pictures\\segway.jpg"); // for TEXT_DETECTION
//        File file = new File("C:\\Users\\glee\\OneDrive - Lander University\\Pictures\\Ross Tennis Trophy 1.jpg"); // for LABEL_DETECTION
        String base64string = convertFileToBase64(file);
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
            
            JSONObject data = new JSONObject();
            JSONArray requests = new JSONArray();
            JSONObject request = new JSONObject();
            JSONObject content = new JSONObject();
            JSONArray features = new JSONArray();
            JSONObject type = new JSONObject();
            content.put("content", base64string); // if the local file is to be used
            
            // the following code is to make the following request JSON
//            {
//                "requests": [
//                {
//                    "features": [
//                    {
//                        "type": "LABEL_DETECTION"
//                    }
//                    ],
//                    "image": {
//                        "content": IMAGE BINARY FROM FILE 
//                    }
//                }
//                ]
//            }
            // See https://cloud.google.com/vision/docs/drag-and-drop to find out what can be detected
            // type: FACE_DETECTION, TEXT_DETECTION, LABEL_DETECTION, WEB_DETECTION, OBJECT_LOCALIZATION
            // check https://cloud.google.com/vision/docs/how-to to see JSON formats for each type
            type.put("type", "LABEL_DETECTION");
            features.add(type);
            request.put("image", content);
            request.put("features", features);
            requests.add(request);
            data.put("requests", requests);

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
            
            // below is to parse JSON returned
            // JSON format is going to be different from what detection was asked. 
            // See printed JSON to see what output is like. Use it to parse for your own project.
            JSONParser parser = new JSONParser();
            JSONObject jobj = (JSONObject) parser.parse(result);
            JSONObject jobj1 = (JSONObject) ((JSONArray) jobj.get("responses")).get(0);
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
            System.out.println(e);
            e.printStackTrace();
            
            System.out.println("An error occured");
        }
        
    }
    
    public static String convertFileToBase64(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte byteArray[] = new byte[(int) file.length()];
            fis.read(byteArray);
            String base64string = Base64.getEncoder().encodeToString(byteArray);
            fis.close();
            return base64string;
        } catch (Exception e) {
            return "";
        }
    }
    
    /*
        Creates a file with a binary content (string)
        @param - sFilePath: path of a file to be created. e.g. "C:\\Users\\glee\\output.mp3"
        @param - sData: string from json such as String form audioContent element of JSON from GCP TTS
    */
    public static void convertBase64ToFile(String sFilePath, String sBinaryData)
    {
        byte[] base64string = Base64.getDecoder().decode(sBinaryData);
        
        try {
            File playFile = new File(sFilePath);
            if (playFile != null) {
                FileOutputStream fos = new FileOutputStream(playFile);
                fos.write(base64string);
                fos.close();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

