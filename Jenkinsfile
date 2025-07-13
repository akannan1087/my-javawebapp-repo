pipeline {
    //agent { label  "my-agent" }
    agent any
    tools {
        maven 'Maven3'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: 'dd3adaef-467d-4711-a310-350a57c8bf16', url: 'https://github.com/akannan1087/my-javawebapp-repo'
            }
        }
        
        stage ("Build") {
            steps {
               sh "mvn clean install -f MyWebApp/pom.xml" 
            }
        }
        
        stage ("unit test and coverage") {
            steps {
                jacoco()
                junit '**/target/surefire-reports/*.xml'
            }
        }
        
        stage ("Code scan") {
            steps {
                withSonarQubeEnv("SonarQube") {
                    sh "mvn sonar:sonar -f MyWebApp/pom.xml" 
                }
            }
        }
        stage ("binary upload") {
            steps {
                nexusArtifactUploader artifacts: [[artifactId: 'MyWebApp', classifier: '', file: 'MyWebApp/target/MyWebApp.war', type: 'war']], credentialsId: 'fabbf1b2-7174-4de7-bc80-cc7be4606367', groupId: 'com.gcp', nexusUrl: 'ec2-35-175-151-27.compute-1.amazonaws.com:8081', nexusVersion: 'nexus3', protocol: 'http', repository: 'maven-snapshots', version: '1.0-SNAPSHOT'
            }
         }
        
        stage ("DEV deploy") {
            steps {
                deploy adapters: [tomcat9(alternativeDeploymentContext: '', credentialsId: '5874e9c9-3bbc-4fe5-8934-6deasd0d3446d26', path: '', url: 'http://ec2-13-221-244-69.compute-1.amazonaws.com:8080')], contextPath: null, war: '**/*.war'
            }
        }
        
        stage ("DEV notify") {
            steps {
                slackSend channel: 'may-2025-weekend-batch', message: 'Hey dev team, deployment is done, please start smoke testing in dev environment'
            }
        }
    
        //CI pipelines ends here
        /*
        * this is multi line comments
        **/
    //CD starts here
    stage ('DEV Approve') {
      steps {
      echo "Taking approval from DEV Manager for QA Deployment"
        timeout(time: 7, unit: 'DAYS') {
        input message: 'Do you want to deploy?', submitter: 'admin'
        }
      }
    }
     stage ('QA Deploy') {
      steps {
        echo "deploying to QA Env "
                deploy adapters: [tomcat9(alternativeDeploymentContext: '', credentialsId: '5874e9c9-3bbc-4fe5-8934-6de0d3446d26', path: '', url: 'http://ec2-54-161-177-206.compute-1.amazonaws.com:8080')], contextPath: null, war: '**/*.war'
        }
    }
    
    stage ("QA notify") {
            steps {
                slackSend channel: 'may-2025-weekend-batch', message: 'Hey QA team, QA deployment is done, please start functional testing in QA environment'
            }
        }
    
    stage ('QA Approve') {
      steps {
        echo "Taking approval from QA manager"
        timeout(time: 3, unit: 'DAYS') {
        input message: 'Do you want to proceed to PROD?', submitter: 'admin,manager_userid'
        }
      }
    }

     stage ('prod Deploy') {
      steps {
        echo "deploying to prod Env "
            deploy adapters: [tomcat9(alternativeDeploymentContext: '', credentialsId: '5zdfsfd874e9c9-3bbc-4fe5-8934-6de0d3446d26', path: '', url: 'http://ec2-54-161-177-206.compute-1.amazonaws.com:8080')], contextPath: null, war: '**/*.war'
        }
    }
    
    stage ("Final notify") {
            steps {
                slackSend channel: 'may-2025-weekend-batch', message: 'Hey PO team, PROD deployment is done, please inform end customers'
            }
        }

    }

    post {
        always {
            // Clean up workspace
            cleanWs()
        }
        success {
            // Notify success (you can add email or Slack notifications here)
            echo "Build and deployment successful."
        }
        failure {
            // Notify failure
            echo "Build or deployment failed."
            slackSend channel: 'may-2025-weekend-batch', message: 'pipeline build failed..please troubleshoot'

        }
    }
}

