package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import org.json.JSONException;

import java.sql.SQLException;

import static jdbc_handler.jdbc_exp.executeQuery;

public class addGroup implements ApiMethodes {

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
    private static final Logger logger = LogManager.getLogger(addGroup.class);
    public JSONObject run(JSONObject request) {
        JSONObject result = new JSONObject();
        logger.info("Beginning process of adding group...");
        // validate data
        if (!validateData(request)){
            logger.info("Data invalid, adding group aborted");
            result.put("code", 400);
            return result;
        }
        int time_start = request.getInt("timeStart");
        int time_end = request.getInt("timeEnd");
        int day = request.getInt("day");
        int classroom_id = request.getInt("classroomId");
        int lecturer_id = request.getInt("lecturerId");
        int course_id = request.getInt("courseId");
        int group_nr = request.getInt("groupNr");

        try {
            String query1 = "INSERT INTO groups" +
                    " (group_nr, time_start, time_end, day, classroom_id, lecturer_id, course_id) values (?, ?, ?, ?, ?, ?, ?)";
            String[] args = {Integer.toString(group_nr), Integer.toString(time_start), Integer.toString(time_end), Integer.toString(day), Integer.toString(classroom_id), Integer.toString(lecturer_id), Integer.toString(course_id)};

            int check = executeQuery(query1, args);
            if (check != 1) {
                logger.info("changed more than 1 row, sth strange, gonna abort");
                String[] empty = {""};
                executeQuery("rollback", empty);
                result.put("code", 500);
                return result;
            }

            logger.info("Added group");

            result.put("code", 200);

        } catch (SQLException e) {
            logger.info("DB not working");
            result.put("code", 500);
        }


        return result;


    }

    public Boolean validateData(JSONObject request)
    {
        Boolean result = true;
        try{
            logger.info("Validating data...");
            int time_start = request.getInt("time_start");
            int time_end = request.getInt("time_end");
            int day = request.getInt("day");
            int classroom_id = request.getInt("classroom_id");
            int lecturer_id = request.getInt("lecturer_id");
            int course_id = request.getInt("course_id");
            int group_nr = request.getInt("group_nr");

            // validate time
            if (time_start <= time_end || 1440 < time_start || time_start< 0 || 1440 < time_end || time_end< 0)
            {
                result = false;
            }

            // validate day
            if (day < 0 || day > 6) result=false;




        } catch (JSONException e) {
            result = false;
        }
        return result;
    }

}
