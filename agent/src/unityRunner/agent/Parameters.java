package unityRunner.agent;

import java.util.Map;

/**
 * Utility class for pulling data from parameter map
 */

public final class Parameters {
    static boolean getBoolean(Map<String, String> parameters, String name) {
        return !(parameters.get(name) == null || !parameters.get(name).equals("true"));

    }

    static String getString(Map<String, String> parameters, String name) {
        if (parameters.get(name) == null) {
            return "";
        }

        return parameters.get(name);
    }
}
