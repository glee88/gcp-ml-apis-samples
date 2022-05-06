
import java.io.File;
import java.util.Scanner;
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
public class JSONParse {
    public static void main(String[] args) {
        File jsonFile = new File("menu.json");
        String sJson = "";
        try {
            Scanner input = new Scanner(jsonFile);
            while (input.hasNext())
                sJson += input.nextLine();

//            JSONParser parse = new JSONParser();
//            JSONObject jo = (JSONObject)parse.parse(sJson);
//            JSONObject menu = (JSONObject) jo.get("menu");
//            JSONObject popup = (JSONObject) menu.get("popup");
//            JSONArray menuItem = (JSONArray) popup.get("menuitem");
//            
//            for (int i=0;i<menuItem.size();i++)
//            {
//                JSONObject item = (JSONObject) menuItem.get(i);
//                System.out.println("value: " + item.get("value"));
//            }
        
            
            JSONObject jObj = new JSONObject();
            jObj.put("encodingType", "UTF8");
            JSONArray docArray = new JSONArray();
            JSONObject doc = new JSONObject();
            doc.put("type", "PLAIN_TEXT");
            doc.put("content", "President is speaking at White House");
            docArray.add(doc);
            jObj.put("documents", docArray);
            System.out.println(jObj.toJSONString());
        } catch (Exception e)
        {
            e.printStackTrace();
        }        
    }
}
