package com.dxc.poc.beam.utils.logging;


import java.util.HashMap;

public final class LogContext {

    private static final ThreadLocal<HashMap<String, String>> labels = ThreadLocal.withInitial(HashMap::new);
    private static final ThreadLocal<HashMap<String, String>> jsonVars = ThreadLocal.withInitial(HashMap::new);

    public static void setLabel(String label, String value) {
        labels.get().put(label, value);
    }

    public static void removeLabel(String label) {
        labels.get().remove(label);
    }

    public static void clearLabel() {
        labels.get().clear();
    }

    public static HashMap<String, String> getLabels() {
        return labels.get();
    }

    public static void setJsonVar(String jsonVar, String value) {
        jsonVars.get().put(jsonVar, value);
    }

    public static void removeJsonVar(String jsonVar) {
        jsonVars.get().remove(jsonVar);
    }

    public static void clearJsonVar() {
        jsonVars.get().clear();
    }

    public static HashMap<String, String> getJsonVars() {
        return jsonVars.get();
    }

    private LogContext() {
    }
}
