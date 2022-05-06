
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author glee
 */
public class JSONTest {
    public static void main(String[] args)
    {
        JSONObject data = new JSONObject();
        // YOUR CODE BELOW

        JSONArray audioArray = new JSONArray();
        JSONObject content = new JSONObject();
        content.put("content", "AcxAe0cj&");
        audioArray.add(content);
        JSONObject uri = new JSONObject();
        uri.put("uri", "http://lander.edu/audio/message.mp3");
        audioArray.add(uri);
        data.put("audio", audioArray);

        JSONObject config = new JSONObject();
        config.put("encoding", "FLAC");
        config.put("languageCode", "en-US");
        config.put("model", "default");
        data.put("config", config);

    System.out.println(data.toJSONString()); // this prints JSON doc created

    }
}
