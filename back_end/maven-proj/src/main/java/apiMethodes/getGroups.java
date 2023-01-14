package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import static jdbc_handler.jdbc_exp.getFromQuery;

public class getGroups implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(getGroups.class);
    public JSONObject run(JSONObject request) {
        logger.info("Getting all groups");
        JSONObject response = new JSONObject();

        int course_id = 0;
        try {
            course_id = request.getInt("courseId");
        } catch (JSONException e) {
            response.put("code", 400);
            response.put("message", "incorrect request");
            logger.info("Problem with database");
        }

        String query = "select group_id, group_nr, time_start, time_end, day, c.code, f.shortname\n" +
                "from ((groups join courses c using(COURSE_ID)) join faculties f using(faculty_id)) where course_id = ?";
        String[] columns = {"group_id", "group_nr", "code", "shortname", "time_start", "time_end", "day"};
        String[] args = {Integer.toString(course_id)};
        ArrayList<ArrayList<String>> result;

        try {
            result = getFromQuery(query, args, columns);
            JSONArray groups = new JSONArray();

            for (ArrayList<String> record : result) {
                JSONObject group = new JSONObject();
                group.put("groupId", record.get(0));
                group.put("groupNr", record.get(1));
                group.put("courseCode", record.get(2));
                group.put("facultyShortname", record.get(3));
                group.put("timeStart", record.get(4));
                group.put("timeEnd", record.get(5));
                group.put("day", record.get(6));
                groups.put(group);
            }
            response.put("groups", groups);
            response.put("code", 200);

        } catch (SQLException e) {
            response.put("code", 500);
            logger.info("Problem with database");
        }

        return response;
    }
}