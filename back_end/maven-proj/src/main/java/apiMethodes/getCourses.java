package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import static jdbc_handler.jdbc_exp.getFromQuery;

public class getCourses implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(getCourses.class);
    public JSONObject run(JSONObject request) {
        logger.info("Getting courses in given faculty");
        JSONObject response = new JSONObject();

        int faculty_id = 0;
        try {
            faculty_id = request.getInt("facultyId");
        } catch (JSONException e) {
            response.put("code", 400);
            response.put("message", "incorrect request");
            logger.info("Problem with database");
        }

        String query = "SELECT code, course_id FROM courses where faculty_id=?";
        String[] columns = {"code", "course_id"};
        String[] args = {Integer.toString(faculty_id)};
        ArrayList<ArrayList<String>> result;

        try {
            result = getFromQuery(query, args, columns);
            JSONArray courses = new JSONArray();

            for (ArrayList<String> record : result) {
                JSONObject course = new JSONObject();
                course.put("name", record.get(0));
                course.put("courseId", Integer.parseInt(record.get(1)));
                courses.put(course);
            }
            response.put("courses", courses);
            response.put("code", 200);

        } catch (SQLException e) {
            response.put("code", 500);
            logger.info("Problem with database");
        }

        return response;
    }
}