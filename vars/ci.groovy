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
            sh "sonar-scanner -Dsonar.host.url=http://mysonarprivateip:9000 -Dsonar.login=${SONAR_USER} -Dsonar.password=${SONAR_PASS} -Dsonar.projectKey=cart"
          }
        }
        // sed searches for double quotes and remove them

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

