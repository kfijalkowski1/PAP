package apiMethods;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class getCourseTypes implements ApiMethodes {
    private static final Logger logger = LogManager.getLogger(getCourseTypes.class);
    public JSONObject run(JSONObject request) {
        logger.info("Geting course types");

        JSONObject response = new JSONObject();

        JSONArray courses = new JSONArray()
                .put("laboratorium")
                .put("seminarium")
                .put("cwiczenia")
                .put("wyklad");

        response.put("code", 200);
        response.put("courses", courses);

        return response;
    }
}
