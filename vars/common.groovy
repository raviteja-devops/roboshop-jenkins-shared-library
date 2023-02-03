def compile() {

  if (app_lang == "nodejs") {
    sh 'npm install'
  }

  if (app_lang == "maven") {
    sh 'mvn package'
  }

  if (app_lang == "golang") {
    sh 'rm -rf go.mod'
    sh 'go mod init dispatch ; go get ; go build'
  }

}


def unittests() {

  if (app_lang == "nodejs") {
    // developer is missing unittest cases in this project, we are skipping.
    // true command gives exit status 0
    // sh 'npm test || true'
    sh 'echo No Test Cases'
  }

  if (app_lang == "maven") {
    // sh 'mvn test'
    sh 'echo No Test Cases'
  }

  if (app_lang == "python") {
    // sh 'python3 -m unittest'
    sh 'echo No Test Cases'
  }

  if (app_lang == "golang") {
    // sh 'go test'
    sh 'echo No Test Cases'
  }

}

def email(email_note) {
  mail bcc: '', body: "Job Failed - ${JOB_BASE_NAME}\nJenkins URL - ${JOB_URL}", cc: '', from: 'ravitejatfcdemo@gmail.com', replyTo: '', subject: "Jenkins Job Failed - ${JOB_BASE_NAME}", to: 'ravitejatfcdemo@gmail.com'
}

def artifactPush() {
  sh "echo ${TAG_NAME} >VERSION"

  if (app_lang == "nodejs") {
    sh "zip -r ${component}-${TAG_NAME}.zip node_modules server.js VERSION ${extraFiles}"
  }

  if (app_lang == "nginx") {
    sh "zip -r ${component}-${TAG_NAME}.zip * -x Jenkinsfile"
  }
// in nginx we take all the files except Jenkinsfile into zip folder
  sh 'ls -l'
  NEXUS_PASS = sh(script: 'aws ssm get-parameters --region us-east-1 --names nexus.pass  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
  NEXUS_USER = sh(script: 'aws ssm get-parameters --region us-east-1 --names nexus.user  --with-decryption --query Parameters[0].Value | sed \'s/"//g\'', returnStdout: true).trim()
  wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [[password: "${NEXUS_PASS}", var: 'SECRET']]]) {
    sh "curl -v -u ${NEXUS_USER}:${NEXUS_PASS} --upload-file ${component}-${TAG_NAME}.zip http://172.31.3.231:8081/repository/${component}/${component}-${TAG_NAME}.zip"
  }

}
// we need to compile code but NodeJS is scripting language, no compile is required but add dependencies
// 'npm install' is going to bring one folder- node_modules, has all dependencies for code to run 'server.js'
// these both are enough on server side to run the things
// we are making package of both 'node_modules' and 'server.js' files in zip file
// redirecting TAG_NAME to file called VERSION and storing that version in zip file for future references

// catalogue and user component has extra directory called schema ${extraFiles}
