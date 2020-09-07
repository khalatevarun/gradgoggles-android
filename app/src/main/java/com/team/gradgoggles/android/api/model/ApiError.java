package com.team.gradgoggles.android.api.model;

import java.util.List;
import java.util.Map;

public class ApiError {

    String error_message;
    Map<String, List<String>> errors;

    public String getError(){
        return error_message;
    }

    public Map<String, List<String>> getErrors(){
        return errors;
    }

}
