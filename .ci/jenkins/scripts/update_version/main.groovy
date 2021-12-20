void execute(def pipelinesCommon) {
    maven.mvnVersionsUpdateParentAndChildModules(pipelinesCommon.getDefaultMavenCommand(), pipelinesCommon.getKogitoVersion(), !pipelinesCommon.isRelease())
    // Update Optaplanner version
    maven.mvnSetVersionProperty(pipelinesCommon.getDefaultMavenCommand(), 'version.org.optaplanner', pipelinesCommon.getOptaPlannerVersion())
}

return this
