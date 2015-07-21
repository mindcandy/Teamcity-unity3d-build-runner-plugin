package unityRunner.server;

import jetbrains.buildServer.requirements.Requirement;
import jetbrains.buildServer.requirements.RequirementType;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.serverSide.RunType;
import jetbrains.buildServer.serverSide.RunTypeRegistry;
import org.jetbrains.annotations.NotNull;
import unityRunner.common.PluginConstants;

import java.util.*;

public class UnityRunnerRunType extends RunType {
    /**
     * construct instance of Unity Runner and register it in the RunTypeRegistry
     * @param registry registry to register yourself in
     */
    public UnityRunnerRunType(final RunTypeRegistry registry) {
        registry.registerRunType(this);
    }

    @NotNull
    @Override
    public String getType() {
        return PluginConstants.RUN_TYPE;
    }

    @Override
    @NotNull
    public String getDisplayName() {
        return PluginConstants.RUNNER_DISPLAY_NAME;
    }

    @Override
    public String getDescription() {
        return PluginConstants.RUNNER_DESCRIPTION;
    }

    /**
     * utility to show param nicely
     */
    private void describeParam(String name, String value, StringBuilder sb) {
        if (value != null && !value.isEmpty()) {
            sb.append(name);
            sb.append(": ");
            sb.append(value);
            sb.append(" \n");
        }
    }

    /**
     * convert the supplied parameters to a human-readable string that can be shown in the UI
     * @param parameters runner parameters
     * @return human-readable description of the parameters
     */
    @Override
    @NotNull
    public String describeParameters(@NotNull final Map<String, String> parameters)
    {
        StringBuilder sb = new StringBuilder();

        describeParam("Version", parameters.get(PluginConstants.PROPERTY_UNITY_VERSION), sb);
        describeParam("Project", parameters.get(PluginConstants.PROPERTY_PROJECT_PATH), sb);
        describeParam("Error / Warning Line List", parameters.get(PluginConstants.PROPERTY_LINELIST_PATH), sb);
        describeParam("Build Player", parameters.get(PluginConstants.PROPERTY_BUILD_PLAYER), sb);
        describeParam("Output directory", parameters.get(PluginConstants.PROPERTY_BUILD_PATH), sb);
        describeParam("Extra options", parameters.get(PluginConstants.PROPERTY_BUILD_EXTRA), sb);
        describeParam("Execute Method", parameters.get(PluginConstants.PROPERTY_EXECUTE_METHOD), sb);

        String logIgnore = parameters.get(PluginConstants.PROPERTY_LOG_IGNORE);
        if (logIgnore != null && "true".equals(logIgnore)) {
            describeParam("Ignore Log Before", parameters.get(PluginConstants.PROPERTY_LOG_IGNORE_TEXT), sb);
        }

        return sb.toString();
    }

    /**
     *
     * @return a property processor to check the validity of user-entered settings
     */
    @Override
    public PropertiesProcessor getRunnerPropertiesProcessor()
    {
        return new PropertiesProcessor()
        {
            public Collection<InvalidProperty> process(Map<String, String> properties)
            {
                if(noBuildTarget(properties))
                    return invalidBuildTarget();

                return Collections.emptySet();
            }

            private Set<InvalidProperty> invalidBuildTarget()
            {
                return Collections.singleton(new InvalidProperty(PluginConstants.PROPERTY_BUILD_PATH,
                                                                "Please set build path."));
            }

            private boolean noBuildTarget(Map<String, String> properties)
            {
                if(properties.containsKey(PluginConstants.PROPERTY_BUILD_PLAYER) &&
                        !properties.get(PluginConstants.PROPERTY_BUILD_PLAYER).equals(""))
                {
                    if(!properties.containsKey(PluginConstants.PROPERTY_BUILD_PATH) ||
                       properties.get(PluginConstants.PROPERTY_BUILD_PATH).equals(""))
                    {
                        return true;
                    }
                }

                return false;
            }
        };
    }

    @Override
    public String getEditRunnerParamsJspFilePath() {
        return "editRunnerRunParameters.jsp";
    }

    @Override
    public String getViewRunnerParamsJspFilePath() {
        return "viewRunnerRunParameters.jsp";
    }

    /**
     * get default properties
     * @return set of default properties
     */
    @Override
    public Map<String, String> getDefaultRunnerProperties() {
        Map<String,String> defaults = new HashMap<String, String>();

        defaults.put(PluginConstants.PROPERTY_QUIT, "true");
        defaults.put(PluginConstants.PROPERTY_BATCH_MODE, "true");
        defaults.put(PluginConstants.PROPERTY_CLEAR_OUTPUT_BEFORE, "true");
        defaults.put(PluginConstants.PROPERTY_CLEAN_OUTPUT_AFTER, "true");
        defaults.put(PluginConstants.PROPERTY_WARNINGS_AS_ERRORS, "true");
        defaults.put(PluginConstants.PROPERTY_LOG_IGNORE, "false");

        return defaults;
    }

    /**
     * Returns specific requirements added by the runner - in our specific case what version of unity is installed
     * @param runParameters parameters provided to the runner
     * @return list of requirements
     */
    @NotNull
    @Override
    public List<Requirement> getRunnerSpecificRequirements(@NotNull Map<String, String> runParameters) {

        List<Requirement> requirements = new ArrayList<>();

        // add parent requirements (if any)
        requirements.addAll(super.getRunnerSpecificRequirements(runParameters));

        String unityVersion = runParameters.get(PluginConstants.PROPERTY_UNITY_VERSION);
        if (unityVersion == null || unityVersion.isEmpty()) {
            // any version of unity will do
            requirements.add(
                new Requirement(
                    PluginConstants.CONFIGPARAM_UNITY_INSTALLED_NAME,
                    PluginConstants.CONFIGPARAM_UNITY_INSTALLED_VALUE,
                    RequirementType.EQUALS
                )
            );
        } else {
            // a specific version of Unity needs to be installed
            String unityVersionParameter = PluginConstants.CONFIGPARAM_UNITY_BASE_VERSION + unityVersion;
            requirements.add(
                new Requirement(unityVersionParameter, "", RequirementType.EXISTS)
            );
        }

        return requirements;
    }
}
