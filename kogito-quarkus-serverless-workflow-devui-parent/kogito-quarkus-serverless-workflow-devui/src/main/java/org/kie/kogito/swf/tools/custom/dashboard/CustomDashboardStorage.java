package org.kie.kogito.swf.tools.custom.dashboard;

import java.io.IOException;
import java.util.Collection;

import org.kie.kogito.swf.tools.custom.dashboard.model.CustomDashboardFilter;
import org.kie.kogito.swf.tools.custom.dashboard.model.CustomDashboardInfo;

public interface CustomDashboardStorage {

    int getCustomDashboardFilesCount();

    Collection<CustomDashboardInfo> getCustomDashboardFiles(CustomDashboardFilter filter);

    String getCustomDashboardFileContent(String name) throws IOException;

    void updateCustomDashboard(String content);
}
