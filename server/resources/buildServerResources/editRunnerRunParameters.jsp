<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<%@ taglib prefix="l" tagdir="/WEB-INF/tags/layout" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="forms" tagdir="/WEB-INF/tags/forms" %>

<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>

<tr>
    <th>
        <label for="argument.unity_version">Unity Version: </label>
    </th>
    <td>
        <props:textProperty name="argument.unity_version" style="width:32em;"/>
        <span class="error" id="error_argument.unity_version"></span>
        <span class="smallNote">
             Specify a version of Unity to use - e.g. 5.1.2f1. If left blank the latest version installed will be used.
        </span>
    </td>
</tr>


<tr>
    <th>
        <label for="unity.executable">Unity Executable: </label>
    </th>
    <td>
        <props:textProperty name="unity.executable" style="width:32em;"/>
        <span class="error" id="error_unity.executable"></span>
        <span class="smallNote">
             Optional, specify to use override the Unity executable path (if specifying a version of unity its best to leave blank).
        </span>
    </td>
</tr>

<tr>
    <th>
        <label for="teamcity.build.workingDir">Working Directory: </label>
    </th>
    <td>
        <props:textProperty name="teamcity.build.workingDir" style="width:32em;"/>
        <span class="error" id="error_teamcity.build.workingDir"></span>
        <span class="smallNote">
             Optional, specify if differs from the checkout directory.
        </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.batch_mode">Batch mode: </label>
    </th>
    <td>
         <props:checkboxProperty name="argument.batch_mode"/>
         <span class="error" id="error_argument.batch_mode"></span>
         <span class="smallNote">
             Run Unity in batch mode.
         </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.no_graphics">No graphics: </label>
    </th>
    <td>
        <props:checkboxProperty name="argument.no_graphics"/>
        <span class="error" id="error_argument.no_graphics"></span>
        <span class="smallNote">
             When running in batch mode, do not initialize graphics device at all.
             This makes it possible to run your automated workflows on machines that don't even have a GPU.
        </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.project_path">Project path: </label>
    </th>
    <td>
        <props:textProperty name="argument.project_path" style="width:32em;"/>
        <span class="error" id="error_argument.project_path"></span>
        <span class="smallNote">
             Open the project at the given path.
        </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.line_list_path">Line List path: </label>
    </th>
    <td>
        <props:textProperty name="argument.line_list_path" style="width:32em;"/>
        <span class="error" id="error_argument.line_list_path"></span>
        <span class="smallNote">
             Open the Error / Warning list file at the following path
        </span>
    </td>
</tr>

<props:selectSectionProperty name="argument.build_player" title="Build player:">
    <props:selectSectionPropertyContent value="StandaloneOSXUniversal" caption="Build a universal OSX standalone"/>
    <props:selectSectionPropertyContent value="StandaloneOSXIntel" caption="Build an OS X standalone (Intel only)"/>
    <props:selectSectionPropertyContent value="StandaloneWindows" caption="Build a Windows standalone"/>
    <props:selectSectionPropertyContent value="iOS" caption="Build an iOS player"/>
    <props:selectSectionPropertyContent value="PS3" caption="Build a PS3 Standalone"/>
    <props:selectSectionPropertyContent value="XBOX360" caption="Build a XBox Standalone"/>
    <props:selectSectionPropertyContent value="Android" caption="Build an Android "/>apk standalone app"/>
    <props:selectSectionPropertyContent value="StandaloneLinux" caption="Build a Linux standalone"/>
    <props:selectSectionPropertyContent value="StandaloneWindows64" caption="Build a Windows 64-bit standalone"/>
    <props:selectSectionPropertyContent value="WebGL" caption="WebGL"/>
    <props:selectSectionPropertyContent value="WSAPlayer" caption="Build an Windows Store Apps player"/>
    <props:selectSectionPropertyContent value="StandaloneLinux64" caption="Build a Linux 64-bit standalone"/>
    <props:selectSectionPropertyContent value="StandaloneLinuxUniversal" caption="Build a Linux universal standalone"/>
    <props:selectSectionPropertyContent value="StandaloneOSXIntel64" caption="Build an OSX Intel 64-bit standalone"/>
    <props:selectSectionPropertyContent value="Tizen" caption="Build a Tizen player"/>
    <props:selectSectionPropertyContent value="PSP2" caption="Build a PS Vita Standalone"/>
    <props:selectSectionPropertyContent value="PS4" caption="Build a PS4 Standalone"/>
    <props:selectSectionPropertyContent value="XboxOne" caption="Build a Xbox One Standalone"/>
    <props:selectSectionPropertyContent value="SamsungTV" caption="Build to Samsung Smart TV platform"/>
    <props:selectSectionPropertyContent value="Nintendo3DS" caption="Build to Nintendo 3DS platform"/>
    <props:selectSectionPropertyContent value="WiiU" caption="Build a Wii U standalone"/>
    <props:selectSectionPropertyContent value="tvOS"/>
</props:selectSectionProperty>

<tr>
    <th>
        <label for="argument.build_extra">Build extra: </label>
    </th>
    <td>
        <props:textProperty name="argument.build_extra" style="width:32em;"/>
        <span class="smallNote">
             Add extra command line options.
        </span>
    </td>
<tr>

<tr>
    <th>
        <label for="argument.build_path">Build path: </label>
    </th>
    <td>
        <props:textProperty name="argument.build_path" style="width:32em;"/>
        <span class="error" id="error_argument.build_path"></span>
        <span class="smallNote">
             Build output path.
        </span>
    </td>
</tr>




<tr>
    <th>
        <label for="argument.clear_output_before">Clear output before: </label>
    </th>
    <td>
         <props:checkboxProperty name="argument.clear_output_before"/>
         <span class="error" id="error_argument.clear_output_before"></span>
         <span class="smallNote">
             Clear output directory before running build.
         </span>
    </td>
</tr>


      
<tr>
    <th>
        <label for="argument.clean_output_after">Clean output after: </label>
    </th>
    <td>
         <props:checkboxProperty name="argument.clean_output_after"/>
         <span class="error" id="error_argument.clean_output_after"></span>
         <span class="smallNote">
             Clean output directory of .meta and .svn files after running build.
         </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.warnings_as_errors">Warnings As Errors: </label>
    </th>
    <td>
         <props:checkboxProperty name="argument.warnings_as_errors"/>
         <span class="error" id="error_argument.warnings_as_errors"></span>
         <span class="smallNote">
            Should the build will report warnings as errors.
         </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.execute_method">Execute method: </label>
    </th>
    <td>
        <props:textProperty name="argument.execute_method" style="width:32em;"/>
        <span class="error" id="error_argument.execute_method"></span>
        <span class="smallNote">
             Execute the static method as soon as Unity is started and the project folder has been opened.
        </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.quit">Quit: </label>
    </th>
    <td>
        <props:checkboxProperty name="argument.quit"/>
        <span class="error" id="error_argument.quit"></span>
        <span class="smallNote">
             Quit Unity cleanly upon finishing execution of other command line arguments.
        </span>
    </td>
</tr>

<tr>
    <th>
        <label for="argument.log_ignore">Enable Log Ignore: </label>
    </th>
    <td>
        <c:set var='onclick'>
          if (this.checked) {
          BS.Util.show('argument.log_ignore_text.container');
          $('argument.log_ignore_text').focus();
          } else {
          BS.Util.hide('argument.log_ignore_text.container');
          }
          BS.VisibilityHandlers.updateVisibility('mainContent');
        </c:set>
        <props:checkboxProperty name="argument.log_ignore" onclick="${onclick}"/>
        <span class="error" id="error_argument.log_ignore"></span>

        <div id="argument.log_ignore_text.container" style="${propertiesBean.properties['argument.log_ignore'] ? '' : 'display:none;'}"
            <label for="argument.log_ignore_text">Ignore any log line before: </label>
            <props:textProperty name="argument.log_ignore_text" style="width:32em;"/>
            <span class="error" id="error_argument.log_ignore_text"></span>
        </div>

        <span class="smallNote">
             Will ignore all log lines output before the specified text is found. Will output all ignored lines if the text is never found.<br/>
             This prevents errors shown in TeamCity logs before Unity has refreshed and found new files.<br/>
        </span>
    </td>
</tr>
