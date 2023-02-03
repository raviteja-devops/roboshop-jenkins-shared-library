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
    sh "zip -r cart-${TAG_NAME}.zip node_modules server.js VERSION"
  }
  sh 'ls -l'
}
// we need to compile code but NodeJS is scripting language, no compile is required but add dependencies
// 'npm install' is going to bring one folder- node_modules, has all dependencies for code to run 'server.js'
// these both are enough on server side to run the things
// we are making package of both 'node_modules' and 'server.js' files in zip file
// redirecting TAG_NAME to file called VERSION and storing that version in zip file for future references
