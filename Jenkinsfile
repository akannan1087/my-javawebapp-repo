pipeline {
    agent any

    tools {
        maven "Maven3"
    }
    stages {
        stage ("Build") {
            steps {
                sh "mvn clean install -f MyWebApp/pom.xml"
            }
        }
        
        stage ("unit tests & coverage") {
            steps {
                junit '**/target/surefire-reports/*.xml'
                jacoco()
            }
        }
        
        stage ("code analysis") {
            steps {
                withSonarQubeEnv("SonarQube") {
                    sh "mvn sonar:sonar -f MyWebApp/pom.xml"
                }
            }
        }
        
        stage ("SAST scanning") {
            steps {
                sh "trivy fs ./MyWebApp"
            }
        }
        
        stage ("binary upload") {
            steps {
                nexusArtifactUploader artifacts: [[artifactId: 'MyWebApp', classifier: '', file: 'MyWebApp/target/MyWebApp.war', type: 'war']], credentialsId: '75b03606-5990-4591-8a73-71eb798abe69', groupId: 'com.gcp.app', nexusUrl: 'ec2-44-208-167-190.compute-1.amazonaws.com:8081', nexusVersion: 'nexus3', protocol: 'http', repository: 'maven-snapshots', version: '1.0-SNAPSHOT'
            }
        }
        
        stage ("Deploy WAR") {
            steps {
            deploy adapters: [tomcat9(credentialsId: '18d203e6-c5bb-4662-9ad5-415464fbd21f', path: '', url: 'http://ec2-3-83-3-21.compute-1.amazonaws.com:8080/')], contextPath: null, war: '**/*.war'
         }
        }
        
        stage ("DEV notify") {
            steps {
                slackSend channel: 'learn-devops-60-days', message: 'Hi DEV team - We have deployed code into DEV env. please start smoke testing in DEV env..'
            }
        }
        
        //CI pipeline ends here..
        
        //CD pipeline starts here..
        
        stage ("DEV Mgr Approve") {
            steps {
                echo "Taking approval from DEV Manager for QA Deployment.."
                timeout (time: 4, unit: 'HOURS') {
                    input message: 'Do you approve QA Deployment?', submitter: 'admin,dev-mgr@email.com'
                }
            }
        }
        
        stage ("QA deploy") {
            steps {
                deploy adapters: [tomcat9(credentialsId: '18d203e6-c5bb-4662-9ad5-415464fbd21f', path: '', url: 'http://ec2-3-83-3-21.compute-1.amazonaws.com:8080/')], contextPath: null, war: '**/*.war'
            }
        }
        
        stage ("QA notify") {
            steps {
                slackSend channel: 'learn-devops-60-days,qa-testing-team', message: 'Hi QA team - We have deployed code into QA env. please start functional testing in QA env..'
            }
        }
        
        stage ("QA Mgr Approve") {
            steps  {
                    echo "Taking approval from QA Manager for PROD Deployment.."
                    timeout (time: 4, unit: 'HOURS') {
                    input message: 'Do you approve PROD Deployment?', submitter: 'admin,qa-mgr@email.com'
                }
            }
        }

        stage ("PROD deploy") {
            steps {
                deploy adapters: [tomcat9(credentialsId: '18d203e6-c5bb-4662-9ad5-415464fbd21f', path: '', url: 'http://ec2-3-83-3-21.compute-1.amazonaws.com:8080/')], contextPath: null, war: '**/*.war'
            }
        }
        
        stage ("Final notify") {
            steps {
                slackSend channel: 'learn-devops-60-days,qa-testing-team,product-owners-teams', message: 'Hi PO team - We have deployed code into PROD env. please start inform end customers..'
            }
        }
        
    }
}

