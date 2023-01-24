def call() {

    pipeline {
        agent {
            label 'work-station'
        }

        stages {

            stage('compile') {
                steps {
                    echo 'compile'
                }
            }

            stage('unit tests') {
                steps {
                    echo 'compile'
                }
            }

            stage('quality control') {
                steps {
                    echo 'compile'
                }
            }

            stage('upload code to a centralized place') {
                steps {
                    echo 'compile'
                }
            }

        }

    }

}

