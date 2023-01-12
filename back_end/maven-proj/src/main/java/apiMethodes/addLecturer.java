package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static jdbc_handler.jdbc_exp.executeQuery;

public class addLecturer implements ApiMethodes {

    public static void main(String args[]) {
        String exchange_sell_id = "123";
        JSONArray buyGroups = new JSONArray();
        buyGroups.put("el1");
        buyGroups.put("el2");
        buyGroups.put("el3");
        System.out.println(buyGroups);
        String query1 = String.format("INSERT INTO exchanges_buy (exchange_sell_id, group_id) values (%s, %s)", exchange_sell_id, buyGroups.getString(0));
        for (int i = 1; i < buyGroups.length(); ++i) {
            String group_id = buyGroups.getString(i);
            query1 += String.format(", (%s, %s)", exchange_sell_id, group_id);
        }
        System.out.println(query1);
    }
    private static final Logger logger = LogManager.getLogger(addLecturer.class);
    public JSONObject run(JSONObject request) {
        JSONObject result = new JSONObject();
        logger.info("Beginning process of adding lecturer...");

        // validate data
        if(!validateData(request)) {
            logger.info("Data invalid, adding classroom aborted, incorrect keys in json");
            result.put("code", 400);
            result.put("message", "invalid data");
            return result;
        }

        String name = request.getString("name");
        String surname = request.getString("surname");
        float rating = request.getFloat("rating");
        String realRating = "";

        if (rating == -1) { realRating = "0"; }
        else { realRating = Float.toString(rating); }

        String title = request.getString("title");


        try {
            String query1 = "INSERT INTO lecturers" +
                    " (name, surname, rating, title) " +
                    "values (?, ?, ?, ?)";

            String[] args = {name, surname, realRating, title};

            int check = executeQuery(query1, args);
            if (check != 1) {
                logger.info("changed more than 1 row, sth strange, gonna abort");
                String[] empty = {""};
                executeQuery("rollback", empty);
                result.put("code", 500);
                return result;
            }

            logger.info("Added lecturer");

            result.put("code", 200);

        } catch (SQLException e) {
            logger.info("DB not working");
            result.put("code", 500);
        }

        return result;

    }


    private Boolean validateData(JSONObject request)
    {
        Boolean result = true;
        try{
            logger.info("Validating data...");
            String name = request.getString("name");
            String surname = request.getString("surname");
            float rating = request.getFloat("rating");
            String title = request.getString("title");
            // validate class name, surname, title
            if (name.equals("") || surname.equals("") || rating != -1 && (rating > 10 || rating < 0))
            {
                result = false;
            }

        } catch (JSONException e) {
            result = false;
        }
        return result;
    }
}
