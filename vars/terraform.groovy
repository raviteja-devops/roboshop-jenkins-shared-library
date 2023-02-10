def call() {
  pipeline {

    options {
      ansiColor('xterm')
    }

    agent {
      node {
        label 'work-station'
      }
    }

    parameters {
      string(name: 'INFRA_ENV', defaultValue: '', description: 'Enter Env like dev or prod')
    }

    stages {

      stage('Terraform init') {
        steps {
          sh "terraform init -backend-config=env-${INFRA_ENV}/state.tfvars"
        }
      }

    }

  }
}