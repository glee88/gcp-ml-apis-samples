/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author glee
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JSONMakingPractice {
    public static void main(String[] args)
    {
//        {
//            "Hello": "World",
//            "What is": "it?",
//            "They are": [
//                {
//                        "something": "COOL"
//                }, 
//                {
//                        "somewhat": "AWESOME"
//                }
//            ]
//        }

        // we want to make the above JSON object and print it
        
        JSONObject data = new JSONObject();
        
        data.put("Hello", "World"); // put key/value pair in JSONObject, data
        data.put("What is", "it");
        
        JSONArray TheyAre = new JSONArray();
        JSONObject something = new JSONObject();
        something.put("something", "COOL");
        JSONObject somewhat = new JSONObject();
        somewhat.put("somewhat", "AWESOME");
        
        TheyAre.add(something);
        TheyAre.add(somewhat);
        
        data.put("They are", TheyAre);
        
        System.out.println(data.toJSONString());
        
        
//{
//  "name":"John",
//  "age":30,
//  "cars": [
//    { "name":"Ford", "models":[ "Fiesta", "Focus", "Mustang" ] },
//    { "name":"BMW", "models":[ "320", "X3", "X5" ] },
//  ]
// }

        JSONObject jsonObjCars = new JSONObject();
        jsonObjCars.put("name", "John");
        jsonObjCars.put("age", 30);
        JSONArray jsonArrayCars = new JSONArray(); // value for "cars" key
        JSONObject jsonNestedObjFord = new JSONObject();
        jsonNestedObjFord.put("name", "Ford");
        JSONArray jsonNestedArrayFord = new JSONArray();
        jsonNestedArrayFord.add("Fiesta");
        jsonNestedArrayFord.add("Focus");
        jsonNestedArrayFord.add("Mustang");
        jsonNestedObjFord.put("models", jsonNestedArrayFord);
        JSONObject jsonNestedObjBMW = new JSONObject();
        jsonNestedObjBMW.put("name", "BMW");
        JSONArray jsonNestedArrayBMW = new JSONArray();
        jsonNestedArrayBMW.add("320");
        jsonNestedArrayBMW.add("X3");
        jsonNestedArrayBMW.add("X5");
        jsonNestedObjBMW.put("models", jsonNestedArrayBMW);
        
        jsonArrayCars.add(jsonNestedObjFord);
        jsonArrayCars.add(jsonNestedObjBMW);
        jsonObjCars.put("cars", jsonArrayCars);
        
        System.out.println(jsonObjCars.toJSONString());
    }
}
