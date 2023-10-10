package org.kie.kogito.jitexecutor.bpmn;

public class TestingUtils {

    public static final String SINGLE_BPMN_FILE = "/SingleProcess.bpmn";
    public static final String MULTIPLE_BPMN_FILE = "/MultipleProcess.bpmn";

    public static final String INVALID_BPMN_FILE = "/InvalidModel.bpmn";

    public static final String UNPARSABLE_BPMN_FILE = "/UnparsableModel.bpmn";
    public static final String SINGLE_BPMN2_FILE = "/SingleProcess.bpmn2";
    public static final String MULTIPLE_BPMN2_FILE = "/MultipleProcess.bpmn2";
    public static final String SINGLE_INVALID_BPMN2_FILE = "/SingleInvalidModel.bpmn2";

    public static final String SINGLE_UNPARSABLE_BPMN2_FILE = "/SingleUnparsableModel.bpmn2";

    public static final String MULTIPLE_INVALID_BPMN2_FILE = "/MultipleInvalidModel.bpmn2";

    public static final String UNPARSABLE_BPMN2_FILE = "/UnparsableModel.bpmn2";

    public static String getFilePath(String fileName) {
        return "src/test/resources" + fileName;
    }

}
