package org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms;

import java.io.IOException;
import java.util.Collection;

import org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model.Form;
import org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model.FormContent;
import org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model.FormFilter;
import org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model.FormInfo;

public interface FormsStorage {

    int getFormsCount();

    Collection<FormInfo> getFormInfoList(FormFilter filter);

    Form getFormContent(String formName) throws IOException;

    void updateFormContent(String formName, FormContent formContent) throws IOException;
}
