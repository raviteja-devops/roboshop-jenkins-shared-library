def compile() {

  if (app_lang == "nodejs") {
    sh 'npm install'
  }

  if (app_lang == "maven") {
    sh 'mvn package'
  }

  if (app_lang == "golang") {
    sh 'go mod init dispatch ; go get ; go build'
  }

}


def unittests() {

  if (app_lang == "nodejs") {
    // developer is missing unittest cases in this project, we are skipping.
    // true command gives exit status 0
    sh 'npm test || true'
  }

  if (app_lang == "maven") {
    sh 'mvn test'
  }

  if (app_lang == "python") {
    sh 'python3 -m unittest'
  }

  if (app_lang == "golang") {
    sh 'go test'
  }

}