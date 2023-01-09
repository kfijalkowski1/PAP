package apiMethodes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static jdbc_handler.jdbc_exp.executeQuery;
import static jdbc_handler.jdbc_exp.getFromQuery;

public class enterExchange implements ApiMethodes {

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
    private static final Logger logger = LogManager.getLogger(enterExchange.class);
    public JSONObject run(JSONObject request) {
        JSONObject result = new JSONObject();
        String ug_id;
        JSONArray buyGroups = new JSONArray();
        try {
            String login = request.getString("login");
            int sell_group_id = request.getInt("sellGroupId");
            buyGroups = request.getJSONArray("buyGroupIds");
            ug_id = getUserGroupId(login, sell_group_id);
        } catch (JSONException e) {
            result.put("code", 400);
            result.put("message", "wrong request");
            return result;
        }
        String insertion_date = getCurrentDate();

        if (Objects.equals(ug_id, "error")) {
            result.put("code", 500);
            return result;
        }

        try {
            String query = "INSERT INTO exchanges_sell (ug_id, insertion_date) values (?, ?)";
            String[] args = {ug_id, insertion_date};
            executeQuery(query, args);

            String exchange_sell_id = getExchangeSellId(ug_id, insertion_date);
            if (exchange_sell_id.equals("error")) {
                result.put("code", 500);
                return result;
            }

            String query1 = "INSERT INTO exchanges_buy (exchange_sell_id, group_id) values (?, ?)";
            List<String> list = new ArrayList<>();
            list.add(exchange_sell_id);
            list.add(Integer.toString(buyGroups.getInt(0)));

            for (int i = 1; i < buyGroups.length(); ++i) {
                String group_id = Integer.toString(buyGroups.getInt(i));
                query1 += ", (?, ?)";
                list.add(exchange_sell_id);
                list.add(group_id);
            }
            String[] args2 = list.toArray(new String[0]);
            executeQuery(query1, args2);

            result.put("code", 200);
            logger.info("New exchange added.");
        } catch (SQLException e) {
            result.put("code", 500);
            logger.error("Problem with database");
        }
        return result;
    }
    public String getUserGroupId(String login, int group_id) {
        try {
            String query = "Select ug_id from user_groups where login= ? and group_id= ?";
            String[] columns = {"ug_id"};
            String[] args = {login, Integer.toString(group_id)};
            ArrayList<ArrayList<String>> queryResult = getFromQuery(query, args, columns);
            logger.info("Checked ug_id of user: " + login);
            return queryResult.get(0).get(0);
        } catch (SQLException e) {
            return "error";
        }
    }
    public String getExchangeSellId(String ug_id, String insertion_date) {
        try {
            String query = "Select exchange_sell_id from exchanges_sell where ug_id= ? and insertion_date= ?";
            String[] columns = {"exchange_sell_id"};
            String[] args = {ug_id, insertion_date};
            ArrayList<ArrayList<String>> queryResult = getFromQuery(query, args, columns);
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
