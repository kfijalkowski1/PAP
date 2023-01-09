package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import org.json.JSONException;

import java.sql.SQLException;

import static jdbc_handler.jdbc_exp.executeQuery;

public class addClassroom implements ApiMethodes {

    public static void main(String args[]) {
    }
    private static final Logger logger = LogManager.getLogger(addClassroom.class);
    public JSONObject run(JSONObject request) {
        JSONObject result = new JSONObject();
        logger.info("Beginning process of adding classroom...");
        String class_nr = "";
        int faculty_id = -1;

        // validate data
        try{
            logger.info("Validating data...");
            class_nr = request.getString("classNr");
            faculty_id = request.getInt("facultyId");
            // validate class number and faculty id
            if (class_nr.length() > 7 || faculty_id > 99 || faculty_id < 0)
            {
                logger.info("Data invalid, adding classroom aborted");
                result.put("code", 400);
                return result;
            }

        } catch (JSONException e) {
            logger.info("Data invalid, adding classroom aborted, incorrect keys in json");
            result.put("code", 400);
            return result;
        }


        try {
            String query1 = String.format("INSERT INTO classrooms" +
                    " (nr, faculty_id) " +
                    "values (?, ?)");
            String[] args = {class_nr, Integer.toString(faculty_id)};
            int check = executeQuery(query1, args);
            if (check != 1) {
                logger.info("changed more than 1 row, sth strange, gonna abort");
                String[] empty = {};
                executeQuery("rollback", empty);
                result.put("code", 500);
                return result;
            }

            logger.info("Added classroom");

            result.put("code", 200);

        } catch (SQLException e) {
            logger.info("DB not working");
            result.put("code", 500);
        }

        return result;

    }

}
