@Library('jenkins-pipeline-shared-libraries')_

pipeline {
    agent {
        label 'kie-rhel7&&kie-mem8g'
    }
    tools {
        nodejs "nodejs-11.0.0"
        maven 'kie-maven-3.5.4'
        jdk 'kie-jdk1.8'
    }
    options {
        buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '10')
        timeout(time: 90, unit: 'MINUTES')
    }
    environment {
        MAVEN_OPTS = '-Xms512m -Xmx3g'
        SONARCLOUD_TOKEN = credentials('SONARCLOUD_TOKEN')
    }
    stages {
        stage('Prepare') {
            steps {
                sh "npm install -g yarn --registry=${NPM_REGISTRY_URL}"
                sh "yarn config set registry ${NPM_REGISTRY_URL}"
                sh "export XAUTHORITY=$HOME/.Xauthority"
                sh "chmod 600 $HOME/.vnc/passwd"
            }
        }
        stage('Build kogito-runtimes') {
            steps {
                dir("kogito-runtimes") {
                    script {
                        githubscm.checkoutIfExists('kogito-runtimes', "$CHANGE_AUTHOR", "$CHANGE_BRANCH", 'kiegroup', "$CHANGE_TARGET")
                        maven.runMavenWithSubmarineSettings('clean install -Prun-code-coverage', false)
                    }
                }
            }
        }
        stage('Build kogito-apps') {
            steps {
                script {
                    wrap([$class: 'Xvnc', takeScreenshot: false, useXauthority: true]) {
                        sh('yarn run init && yarn build:prod')
                    }
                    maven.runMavenWithSubmarineSettings('clean install -Prun-code-coverage', false)
                }
            }
        }
        stage('Build kogito-cloud') {
            steps {
                dir("kogito-cloud") {
                    script {
                        githubscm.checkoutIfExists('kogito-cloud', "$CHANGE_AUTHOR", "$CHANGE_BRANCH", 'kiegroup', "$CHANGE_TARGET")
                        maven.runMavenWithSubmarineSettings('clean install', false)
                    }
                }
            }
        }
        stage('Build kogito-examples') {
            steps {
                dir("kogito-examples") {
                    script {
                        githubscm.checkoutIfExists('kogito-examples', "$CHANGE_AUTHOR", "$CHANGE_BRANCH", 'kiegroup', "$CHANGE_TARGET")
                        maven.runMavenWithSubmarineSettings('clean install', false)
                    }
                }
                // Use a separate dir for persistence to not overwrite the test results
                dir("kogito-examples-persistence") {
                    script {
                        githubscm.checkoutIfExists('kogito-examples', "$CHANGE_AUTHOR", "$CHANGE_BRANCH", 'kiegroup', "$CHANGE_TARGET")
                        // Don't run with tests so far, see: https://github.com/quarkusio/quarkus/issues/6885
                        maven.runMavenWithSubmarineSettings('clean install -Ppersistence', true)
                    }
                }
            }
        }
//         stage('Analyze kogito-apps') {
//             steps {
//                 script {
//                     maven.runMavenWithSubmarineSettings('-e -nsu generate-resources -Psonarcloud-analysis', false)
//                 }
//             }
//         }
    }
    post {
        unstable {
            script {
                mailer.sendEmailFailure()
            }
        }
        failure {
            script {
                mailer.sendEmailFailure()
            }
        }
        always {
            junit '**/**/junit.xml'
            cleanWs()
        }
    }
}
