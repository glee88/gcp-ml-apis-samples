
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
public class GCPTextToSpeechSample {
    public static void main(String[] args)
    {
        String apiUrl = "https://texttospeech.googleapis.com/v1beta1/text:synthesize?key=";
        String apiKey = "Your API Key from GCP";

//        https://cloud.google.com/text-to-speech
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
//	'input':{    
//		'text':'Android is a mobile operating system developed by Google, based on the Linux kernel and designed primarily for touchscreen mobile devices such as smartphones and tablets.'  
//	},  
//	'voice': {    
//		'languageCode':'en-gb',    
//		'name':'en-GB-Standard-A',    
//		'ssmlGender':'FEMALE'  
//	},  
//	'audioConfig':{    'audioEncoding':'MP3'  } // 'LINEAR16' for .wav file output
//}        
            Scanner input = new Scanner(System.in);
            System.out.println("Enter text to be spoken by GCP: ");
            String sMessage = input.nextLine();
            JSONObject data = new JSONObject();
            JSONObject text = new JSONObject();
            //text.put("text", "Android is a mobile operating system developed by Google, based on the Linux kernel and designed primarily for touchscreen mobile devices such as smartphones and tablets.");
            text.put("text", sMessage);

            JSONObject voice = new JSONObject();
            voice.put("languageCode", "en-gb");
            voice.put("name", "en-GB-Standard-A");
            voice.put("ssmlGender", "FEMALE");
            
            JSONObject audioConfig = new JSONObject();
            audioConfig.put("audioEncoding", "LINEAR16"); // LINEAR16 for .wav, MP3 for .mp3 output file
            
            data.put("input", text);
            data.put("voice", voice);
            data.put("audioConfig", audioConfig);
            System.out.println(data.toJSONString());

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
            
            JSONParser parser = new JSONParser();
            JSONObject jobj = (JSONObject) parser.parse(result);
            String sBinaryData = (String) jobj.get("audioContent");
            // change the file path to your file system
            convertBase64ToFile("C:\\Users\\glee\\Music\\out.mp3", sBinaryData);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occured");
        }      
        
    }
    
    public static void convertImage(String sBinary)
    {
        try {
//            BufferedImage bImage;// = ImageIO.read(new File("sample.jpg"));
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            ImageIO.write(bImage, "jpg", bos );
            byte [] data = sBinary.getBytes();
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            BufferedImage bImage2 = ImageIO.read(bis);
            ImageIO.write(bImage2, "jpg", new File("output.jpg") );
            System.out.println("image created");        
        } catch (Exception e)
        {
            System.out.println(e);
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
