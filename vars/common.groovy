def compile() {

  if (app_lang == "nodejs") {
    sh 'npm install'
  }

  if (app_lang == "maven") {
    sh 'mvn package'
  }

  if (app_lang == "golang") {
    if [ $? -ne 0 ]; then
    sh 'go mod init dispatch ; go get ; go build'
    fi
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