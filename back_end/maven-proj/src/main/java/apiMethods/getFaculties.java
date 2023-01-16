package apiMethods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import static jdbc_handler.jdbc_exp.getFromQuery;

public class getFaculties implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(getFaculties.class);
    public JSONObject run(JSONObject request) {
        logger.info("Getting faculties degrees");

        JSONObject response = new JSONObject();

        String query = "SELECT shortname, FACULTY_ID FROM faculties";
        String[] columns = {"shortname", "FACULTY_ID"};
        String[] args = {};
        ArrayList<ArrayList<String>> result;

        try {
            result = getFromQuery(query, args, columns);
            JSONArray faculties = new JSONArray();

            for (ArrayList<String> record : result) {
                JSONObject faculty = new JSONObject();
                faculty.put("name", record.get(0));
                faculty.put("id", Integer.parseInt(record.get(1)));
                faculties.put(faculty);
            }
            response.put("faculties", faculties);
            response.put("code", 200);

        } catch (SQLException e) {
            response.put("code", 500);
            logger.info("Problem with database");
        }

        return response;
    }
}