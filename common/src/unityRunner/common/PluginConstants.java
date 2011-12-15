package unityRunner.common;

import org.jetbrains.annotations.NonNls;

import java.util.Arrays;
import java.util.List;

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
    String PROPERTY_BUILD_PATH = "argument.build_path";
    String PROPERTY_BUILD_PLAYER = "argument.build_player";
    
    List<String> PROPERTY_BUILD_TARGETS = Arrays.asList("buildWindowsPlayer", "buildOSXPlayer!", "buildWebPlayer");

    String REPORT_TAB_CODE = "unityReportTab";
}
