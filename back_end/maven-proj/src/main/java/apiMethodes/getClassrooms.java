package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import static jdbc_handler.jdbc_exp.getFromQuery;

public class getClassrooms implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(getClassrooms.class);
    public JSONObject run(JSONObject request) {
        logger.info("Getting classes on given faculty");
        JSONObject response = new JSONObject();

        int faculty_id = 0;
        try {
            faculty_id = request.getInt("facultyId");
        } catch (JSONException e) {
            response.put("code", 400);
            response.put("message", "incorrect request");
            logger.info("Problem with database");
        }


        String query = "SELECT nr, classroom_id FROM courses where faculty_id=?";
        String[] columns = {"nr", "classroom_id"};
        String[] args = {Integer.toString(faculty_id)};
        ArrayList<ArrayList<String>> result;

        try {
            result = getFromQuery(query, args, columns);
            JSONArray classrooms = new JSONArray();

            for (ArrayList<String> record : result) {
                JSONObject classroom = new JSONObject();
                classroom.put("nr", record.get(0));
                classroom.put("id", Integer.parseInt(record.get(1)));
                classrooms.put(classroom);
            }
            response.put("classrooms", classrooms);
            response.put("code", 200);

        } catch (SQLException e) {
            response.put("code", 500);
            logger.info("Problem with database");
        }

        return response;
    }
}