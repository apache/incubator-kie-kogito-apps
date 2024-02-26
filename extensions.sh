ROOT=`pwd`

DOC="${ROOT}/extensions.txt"

EXTENSIONS=`find . -name "quarkus-extension.yaml"`

touch $DOC

echo "| Extension name | Path | Verified |" > $DOC
echo "|-----------------|------|:-------:|" >> $DOC

for ext in $EXTENSIONS
do
  FOLDER=`echo "${ext%/src/*}/"`
  cd $FOLDER

  #GROUPID=`mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.groupId -q -DforceStdout`
  #ARTIFACTID=`mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.artifactId -q -DforceStdout`
  NAME=`mvn org.apache.maven.plugins:maven-help-plugin:3.2.0:evaluate -Dexpression=project.name -q -DforceStdout`

echo $FOLDER
  echo "| $NAME | $FOLDER | :x: |" >> $DOC
  cd $ROOT
done
