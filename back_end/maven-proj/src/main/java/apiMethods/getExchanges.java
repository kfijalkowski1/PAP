package apiMethods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;

import static jdbc_handler.jdbc_exp.getFromQuery;

public class getExchanges implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(getExchanges.class);
    public JSONObject run(JSONObject request) {
        logger.info("Started getting exchanges");
        JSONObject response = new JSONObject();
        String login = null;
        try {
            login = request.getString("login");
        } catch (JSONException e) {
            response.put("code", 400);
            response.put("message", "incorrect request");
            logger.info("Problem with database");
        }

        String query = "SELECT json_object('sellGroup'          VALUE (SELECT json_object('groupNr'          VALUE group_nr, \n" +
                "                                                       'timeStart'         VALUE time_start,\n" +
                "                                                       'timeEnd'           value time_end,\n" +
                "                                                       'courseCode'              value c.code,\n" +
                "                                                       'day'               VALUE day)  \n" +
                "                                                        from (groups join courses c using(course_id))\n" +
                "                                                        where group_id = ug.group_id), \n" +
                "                   'insertionDate'          VALUE exchanges_sell.INSERTION_DATE,\n" +
                "                   'complete'               value CASE WHEN exchanges_sell.COMPLETING_EXCHANGE_ID is null THEN 0 ELSE 1 END,\n" +
                "                   'buyGroups'           VALUE json_arrayagg(\n" +
                "                                                    (SELECT json_object('groupNr'          VALUE group_nr, \n" +
                "                                                       'timeStart'         VALUE time_start,\n" +
                "                                                       'timeEnd'           value time_end,\n" +
                "                                                       'courseCode'              value c.code,\n" +
                "                                                       'day'               VALUE day)  \n" +
                "                                                        from (groups join courses c using(course_id))\n" +
                "                                                        where group_id = exchanges_buy.group_id)\n" +
                "                                                    ORDER BY exchanges_buy.group_id)) as a\n" +
                "  from ((exchanges_sell join user_groups ug using(ug_id)) join exchanges_buy using(exchange_sell_id))\n" +
                "  WHERE ug.login = ?\n" +
                "  GROUP BY ug.group_id, exchanges_sell.INSERTION_DATE, exchanges_sell.COMPLETING_EXCHANGE_ID";
        String[] columns = {"a"};
        String[] args = {login};
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


}