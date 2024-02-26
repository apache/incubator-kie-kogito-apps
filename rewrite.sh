echo "Starting"

export MAVEN_OPTS='-Xmx2048m '

echo "Let's go"

mvn org.openrewrite.maven:rewrite-maven-plugin:run -Drewrite.activeRecipes=org.kie.ChangeDependencies  \
-Denforcer.skip \
-fae \
-Dfull \
-U \
-Dexclusions=**/target \
-DplainTextMasks=**/kmodule.xml