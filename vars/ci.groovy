def call() {
  try {

    pipeline {
      agent {
        label 'work-station'
      }

      stages {

        stage('Compile/Build') {
          steps {
            script {
              common.compile()
            }
          }
        }

        stage('Unit Tests') {
          steps {
            script {
              common.unittests()
            }
          }
        }

        stage('Quality Control') {
          environment {
            SONAR_USER = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
            SONAR_PASS = '$(aws ssm get-parameters --region us-east-1 --names sonarqube.pass  --with-decryption --query Parameters[0].Value | sed \'s/"//g\')'
          }
          steps {
            script {
              maskPasswords(varPasswordPairs: [[password: "${SONAR_PASS}", var: 'sonarqube.pass']]) {
                sh "sonar-scanner -Dsonar.host.url=http://172.31.15.116:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASS} -Dsonar.projectKey=${component}"
              }
            }
          }
        }
        // sed searches for double quotes and remove them
        // since aws parameter plugin in jenkins is not working, we are using shell (environment)

        stage('upload code to a centralized place') {
          steps {
            echo 'compile'
          }
        }

      }

    }

  } catch(Exception e) {
    common.email("Failed")
  }
}

// TRY and CATCH if any exception, anywhere in the entire pipeline, it will mail Failed message and links
