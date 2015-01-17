package com.daoleen.banking.web.infrastructure;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 1/16/15.
 */
public class ViewResult {
    private String viewName;
    private final Map<String, Object> variables = new HashMap<>();

    public ViewResult(String viewName) {
        this.viewName = viewName;
    }

    public ViewResult add(String name, Object value) {
        variables.put(name, value);
        return this;
    }


    public String getViewName() {
        return viewName;
    }

    public ViewResult setViewName(String viewName) {
        this.viewName = viewName;
        return this;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }
}
