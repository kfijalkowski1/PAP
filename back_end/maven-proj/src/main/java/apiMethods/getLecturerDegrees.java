package apiMethods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class getLecturerDegrees implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(getLecturerDegrees.class);
    public JSONObject run(JSONObject request) {
        logger.info("Geting lecturers potencial degrees");

        JSONObject response = new JSONObject();

        JSONArray degrees = new JSONArray()
                .put("inz")
                .put("mgr")
                .put("mgr inz")
                .put("dr")
                .put("dr hab")
                .put("prof ucz")
                .put("prof");

        response.put("code", 200);
        response.put("degrees", degrees);

        return response;
    }
}