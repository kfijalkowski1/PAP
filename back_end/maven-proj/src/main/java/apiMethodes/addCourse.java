package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import org.json.JSONException;

import java.sql.SQLException;

import static jdbc_handler.jdbc_exp.executeQuery;

public class addCourse implements ApiMethodes {

    public static void main(String args[]) {

    }
    private static final Logger logger = LogManager.getLogger(addCourse.class);
    public JSONObject run(JSONObject request) {
        JSONObject result = new JSONObject();
        logger.info("Beginning process of adding course...");
        // validate data

        String validate = validateData(request);
        if (!(validate.equals("")))
        {
            logger.info("Data invalid, adding group aborted");
            result.put("code", 400);
            result.put("message", validate);
            return result;
        }
        String type = request.getString("type");
        String name = request.getString("name");
        String code = request.getString("code");
        int faculty_id = request.getInt("facultyId");

        try {
            String query1 = "INSERT INTO courses" +
                    " (type, name, code, faculty_id) " +
                    "values (?, ?, ?, ?)";
            String [] args = {type, name, code, Integer.toString(faculty_id)};
            int check = executeQuery(query1, args);
            if (check != 1) {
                logger.info("changed more than 1 row, sth strange, gonna abort");
                String[] empty = {""};
                executeQuery("rollback", empty);
                result.put("code", 500);
                return result;
            }

            logger.info("Added course");

            result.put("code", 200);
            result.put("message", "");

        } catch (SQLException e) {
            logger.info("DB not working");
            result.put("code", 500);
            result.put("message", "Database malfunction");
        }


        return result;


    }

    public String validateData(JSONObject request)
    {
        String result = "";
        try{
            logger.info("Validating data...");
            String type = request.getString("type");
            String name = request.getString("name");
            String code = request.getString("code");
            int faculty_id = request.getInt("facultyId");

            // validate time
            if (type.length() < 0 || type.length() > 13 || name.length() > 30 || code.length() > 6)
            {
                result = "incorrect data in request";
            }

            // validate day

        } catch (JSONException e) {
            result = "obligatory data not in request";
        }
        return result;
    }

}
