package loketbayar.id;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by boyke on 7/19/2016.
 */
public class PojoMion {
    private static JSONObject jsonObject;
    private static JSONArray jsonArray;

    public static ArrayList<String> AmbilArray(String b, String datanya, String c) {
        ArrayList<String> Arrays =  new ArrayList<>();
        try {
            Arrays.clear();
            jsonObject = new JSONObject(b);
            jsonArray = jsonObject.getJSONArray(c);
            int count = 0;
            while (count < jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                String isi = jo.getString(datanya);
                Arrays.add(isi);
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return Arrays;

    }
    public static ArrayList<Double> AmbilArray22(String b, String datanya, String c) {
        ArrayList<Double> Arrays =  new ArrayList<>();
        try {
            Arrays.clear();
            jsonObject = new JSONObject(b);
            jsonArray = jsonObject.getJSONArray(c);
            int count = 0;
            while (count < jsonArray.length()) {
                JSONObject jo = jsonArray.getJSONObject(count);
                String isi = jo.getString(datanya);
                Arrays.add(Double.valueOf(isi));
                count++;
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return Arrays;

    }
    public static String AmbilArray2(String b,String tercari) {
        String res="";
        try {
            jsonObject = new JSONObject(b);
            res=jsonObject.getString(tercari);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return res;


    }

}
