package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static jdbc_handler.jdbc_exp.executeQuery;
import static jdbc_handler.jdbc_exp.getFromQuery;

public class enterExchange implements ApiMethodes {

    public static void main() {
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
    private static final Logger logger = LogManager.getLogger(enterExchange.class);
    public JSONObject run(JSONObject request) {
        JSONObject result = new JSONObject();
        String login = request.getString("login");
        String sell_group_id = request.getString("sell_group_id");
        JSONArray buyGroups = request.getJSONArray("buy_group_ids");
        String ug_id = getUserGroupId(login, sell_group_id);
        String insertion_date = getCurrentDate();

        if (Objects.equals(ug_id, "error")) {
            result.put("code", 500);
            return result;
        }

        try {
            String query = "INSERT INTO exchanges_sell (ug_id, insertion_date) values ('" + ug_id + "', '" + insertion_date + "')";
            executeQuery(query);

            String exchange_sell_id = getExchangeSellId(ug_id, insertion_date);
            if (exchange_sell_id.equals("error")) {
                result.put("code", 500);
                return result;
            }

            String query1 = String.format("INSERT INTO exchanges_buy (exchange_sell_id, group_id) values (%s, %s)", exchange_sell_id, buyGroups.getString(0));
            for (int i = 1; i < buyGroups.length(); ++i) {
                String group_id = buyGroups.getString(i);
                query1 += String.format(", (%s, %s)", exchange_sell_id, group_id);
            }
            executeQuery(query1);

            result.put("code", 200);
            logger.info("New exchange added.");
        } catch (SQLException e) {
            result.put("code", 500);
            logger.error("Problem with database");
        }
        return result;
    }
    public String getUserGroupId(String login, String group_id) {
        try {
            String query = "Select ug_id from ser_groups where login='" + login + "' and group_id='" + group_id + "'";
            String[] columns = {"ug_id"};
            ArrayList<ArrayList<String>> queryResult = getFromQuery(query, columns);
            logger.info("Checked ug_id of user: " + login);
            return queryResult.get(0).get(0);
        } catch (SQLException e) {
            return "error";
        }
    }
    public String getExchangeSellId(String ug_id, String insertion_date) {
        try {
            String query = "Select exchange_sell_id from exchanges_sell where ug_id='" + ug_id + "' and insertion_date='" + insertion_date + "'";
            String[] columns = {"exchange_sell_id"};
            ArrayList<ArrayList<String>> queryResult = getFromQuery(query, columns);
            return queryResult.get(0).get(0);
        } catch (SQLException e) {
            return "error";
        }
    }
    public String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime currentDate = LocalDateTime.now();
        return currentDate.format(formatter);
    }
}
