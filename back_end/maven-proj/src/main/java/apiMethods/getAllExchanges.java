package apiMethods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static jdbc_handler.jdbc_exp.getFromArrayQuery;
import static jdbc_handler.jdbc_exp.getFromQuery;

public class getAllExchanges implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(getAllExchanges.class);
    public JSONObject run(JSONObject request) {
        logger.info("Started getting all exchanges");
        JSONObject response = new JSONObject();
        String login = null;
        JSONArray courses = null;
        Boolean forUser = null;
        String coursesStr = null;
        int facultyId = -1;
        try {
            login = request.getString("login");
            courses = request.getJSONArray("courses");
            forUser = request.getBoolean("forUser");
            facultyId = request.getInt("facultyId");
        } catch (JSONException e) {
            response.put("code", 400);
            response.put("message", "incorrect request");
            logger.info("Problem with request");
            return response;
        }
        if (forUser) {
            JSONArray userCourses = getUserCourses(login);
            for(int i=0; i < userCourses.length(); i++) {
                courses.put(userCourses.get(i));
            }
        }

        if (courses.length() == 0) {
            JSONObject courseReq = null;
            courseReq.put("facultyId", facultyId);
            getCourses crs = new getCourses();
            JSONArray allCourses = (JSONArray) crs.run(courseReq).get("courses");
            for(int i=0; i < allCourses.length(); i++) {
                courses.put(allCourses.get(i));
            }
        }

        if(courses.length() == 0) {

            response.put("exchanges", new JSONArray());
            response.put("code", 200);
            return response;
        }

        String questionString = "(?";
        List<String> listCourses = new ArrayList<String>();
        for(int i=0; i< courses.length(); i++){
            listCourses.add(courses.getString(i));
        }
        for(int i=1; i< courses.length(); i++){
            questionString += ", ?";
        }
        int size = listCourses.size();
        String[] stringCourses = listCourses.toArray(new String[size]);

        questionString += ")";



        String query = "SELECT json_object('sell_group_id'          VALUE (SELECT json_object('groupNr'          VALUE group_nr, \n" +
                "                                                       'timeStart'         VALUE time_start,\n" +
                "                                                       'timeEnd'           value time_end,\n" +
                "                                                       'code'              value c.code,\n" +
                "                                                       'day'               VALUE day)  \n" +
                "                                                        from (groups join courses c using(course_id))\n" +
                "                                                        where group_id = ug.group_id), \n" +
                "                   'insertionDate'          VALUE exchanges_sell.INSERTION_DATE,\n" +
                "                   'complete'               value CASE WHEN exchanges_sell.COMPLETING_EXCHANGE_ID is null THEN 0 ELSE 1 END,\n" +
                "                   'buy_group_id'           VALUE json_arrayagg(\n" +
                "                                                    (SELECT json_object('groupNr'          VALUE group_nr, \n" +
                "                                                       'timeStart'         VALUE time_start,\n" +
                "                                                       'timeEnd'           value time_end,\n" +
                "                                                       'code'              value c.code,\n" +
                "                                                       'day'               VALUE day)  \n" +
                "                                                        from (groups join courses c using(course_id))\n" +
                "                                                        where group_id = exchanges_buy.group_id)\n" +
                "                                                    ORDER BY exchanges_buy.group_id)) as a\n" +
                "  from (((exchanges_sell join user_groups ug using(ug_id)) join exchanges_buy using(exchange_sell_id))) join groups g on(ug.group_id = g.group_id)\n" +
                "  WHERE g.course_id in " + questionString + " and (exchanges_sell.COMPLETING_EXCHANGE_ID is null)\n" +
                "  GROUP BY ug.group_id, exchanges_sell.INSERTION_DATE, exchanges_sell.COMPLETING_EXCHANGE_ID";
        String[] columns = {"a"};
        String[] args = stringCourses;
        ArrayList<ArrayList<String>> result;

        try {
            result = getFromQuery(query, args, columns);
            JSONArray exchanges = new JSONArray();
            for (ArrayList<String> res: result) {
                JSONObject row = new JSONObject(res.get(0));
                exchanges.put(row);
            }
            response.put("exchanges", exchanges);
            response.put("code", 200);

        } catch (SQLException e) {
            response.put("code", 500);
            logger.info("Problem with database");
        }

        return response;
    }

    JSONArray getUserCourses(String login){
        String query = "select c.code\n" +
                "from ((groups join courses c using(COURSE_ID)) join user_groups using(group_id))\n" +
                "where login = ? and is_current=1";
        String[] columns = {"code"};
        String[] args = {login};
        ArrayList<ArrayList<String>> result;
        JSONArray courses = new JSONArray();

        try {
            result = getFromQuery(query, args, columns);

            for (ArrayList<String> record : result) {
                courses.put(record.get(0));
            }

        } catch (SQLException e) {
            logger.info("Problem with database");
        }
        return courses;

    }


}
