package unityRunner.common;

import org.jetbrains.annotations.NonNls;

public interface PluginConstants {
    @NonNls String RUN_TYPE = "unityRunner";
    @NonNls String RUNNER_DISPLAY_NAME = "Unity";
    String RUNNER_DESCRIPTION = "Unity runner";
    String OUTPUT_FILE_NAME = "UnityOutput.html";

    String PROPERTY_BATCH_MODE = "argument.batch_mode";
    String PROPERTY_EXECUTE_METHOD = "argument.execute_method";
    String PROPERTY_NO_GRAPHICS = "argument.no_graphics";
    String PROPERTY_QUIT = "argument.quit";
    String PROPERTY_PROJECT_PATH = "argument.project_path";

    String REPORT_TAB_CODE = "unityReportTab";
}
