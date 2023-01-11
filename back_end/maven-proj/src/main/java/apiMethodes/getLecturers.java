package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import static jdbc_handler.jdbc_exp.getFromQuery;

public class getLecturers implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(getLecturers.class);
    public JSONObject run(JSONObject request) {
        logger.info("Getting all lecturers");
        JSONObject response = new JSONObject();

        String query = "SELECT title, name, surname, lecturer_id FROM lecturers";
        String[] columns = {"title", "name", "surname", "lecturer_id"};
        String[] args = {};
        ArrayList<ArrayList<String>> result;

        try {
            result = getFromQuery(query, args, columns);
            JSONArray lecturers = new JSONArray();

            for (ArrayList<String> record : result) {
                JSONObject lecturer = new JSONObject();
                lecturer.put("title", record.get(0));
                lecturer.put("name", record.get(1));
                lecturer.put("surname", record.get(2));
                lecturer.put("lecturerId", Integer.parseInt(record.get(3)));
                lecturers.put(lecturer);
            }
            response.put("lecturers", lecturers);
            response.put("code", 200);

        } catch (SQLException e) {
            response.put("code", 500);
            logger.info("Problem with database");
        }

        return response;
    }
}