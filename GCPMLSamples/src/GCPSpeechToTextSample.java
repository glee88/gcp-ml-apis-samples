
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author glee
 */
public class GCPSpeechToTextSample {
    public static void main(String[] args)
    {
        String apiUrl = "https://speech.googleapis.com/v1p1beta1/speech:recognize?key=";
        String apiKey = "Your API Key from GCP";
//        File file = new File("C:\\Users\\glee\\Music\\out.mp3");
        File file = new File("C:\\Users\\glee\\Music\\out.wav");        
//        File file = new File("C:\\Users\\glee\\Music\\socialDistancing2.mp3");        
//        File file = new File("C:\\Users\\glee\\Music\\Recording.wav");
//        File file = new File("C:\\Users\\glee\\Music\\lorax-stereo.wav");        
        String base64string = convertFileToBase64(file);
        
//        https://cloud.google.com/speech-to-text/
        try {
            URL url = new URL(apiUrl + apiKey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);
            conn.connect();
            
// JSON INPUT IS AS FOLLOWS
//{
//  "audio": {
//    "content": "/* Your audio */"
//  },
//  "config": {
//    "enableAutomaticPunctuation": true,
//    "encoding": "LINEAR16", // "MP3" for mp3 files. encoding types. See https://cloud.google.com/speech-to-text/docs/reference/rest/v1p1beta1/RecognitionConfig#AudioEncoding
//    "languageCode": "en-US",
//    "model": "default"
//  }
//}
            JSONObject jsonObj = new JSONObject();
            JSONObject audio = new JSONObject();
            //text.put("text", "Android is a mobile operating system developed by Google, based on the Linux kernel and designed primarily for touchscreen mobile devices such as smartphones and tablets.");
            audio.put("content", base64string);

            JSONObject config = new JSONObject();
            config.put("enableAutomaticPunctuation", true);
            config.put("encoding", "LINEAR16"); // "encoding" not necessary for .WAV file
//            config.put("encoding", "MP3"); // encoding for .MP3 file
            config.put("languageCode", "en-gb");
            config.put("model", "default");
            config.put("audioChannelCount", 1); // Mono recording: 1, Stereo: 2. giving incorrect channel returns errors

            
            jsonObj.put("config", config);
            jsonObj.put("audio", audio);
            
            System.out.println(jsonObj.toJSONString());

            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
            osw.write(jsonObj.toJSONString()); // send POST JSON request
            osw.close();
            
            // now result from GCP is to come
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader br = new BufferedReader(in);
            String result = "", output;
            while ((output = br.readLine()) != null) {
                result += output;
                System.out.println(output); // print out result JSON from GCP
            }        
            
            JSONParser parser = new JSONParser();
            JSONObject jobj = (JSONObject) parser.parse(result);
            JSONArray resultsArray = (JSONArray) jobj.get("results");
            JSONObject arrayElem = (JSONObject)resultsArray.get(0);
            JSONArray altArray = (JSONArray) arrayElem.get("alternatives");
            
            for (int i=0;i<altArray.size();i++) 
            {
                JSONObject content = (JSONObject)altArray.get(0);
                System.out.println(content.get("transcript"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
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
