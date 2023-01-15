package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import static jdbc_handler.jdbc_exp.getFromQuery;

public class getUserGroups implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(getUserGroups.class);
    public JSONObject run(JSONObject request) {
        logger.info("Getting user groups");
        JSONObject response = new JSONObject();

        String login = null;
        try {
            login = request.getString("login");
        } catch (JSONException e) {
            response.put("code", 400);
            response.put("message", "incorrect request");
            logger.info("Problem with database");
        }
    //TODO czas i dzień wysyłam niistniejące
        String query = "select group_nr, group_id, c.code, COURSE_ID, f.shortname\n" +
                "from (((groups join courses c using(COURSE_ID)) join faculties f using(faculty_id)) join user_groups using(group_id))\n" +
                "where login = ?";
        String[] columns = {"group_nr", "group_id", "code", "shortname", "COURSE_ID"};
        String[] args = {login};
        ArrayList<ArrayList<String>> result;

        try {
            result = getFromQuery(query, args, columns);
            JSONArray groups = new JSONArray();

            for (ArrayList<String> record : result) {
                JSONObject group = new JSONObject();
                group.put("groupNr", record.get(0));
                group.put("groupId", record.get(1));
                group.put("courseCode", record.get(2));
                group.put("facultyShortname", record.get(3));
                group.put("courseId", record.get(4));
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