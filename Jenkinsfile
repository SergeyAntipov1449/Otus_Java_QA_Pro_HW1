pipeline {
  agent { label 'docker' }

  environment {
    REGISTRY = 'localhost:5000'
  }

  parameters {
    string(
      name: 'BASE_URL',
      defaultValue: 'https://otus.ru',
      description: 'Базовый URL для UI-тестов'
    )
    choice(
      name: 'BROWSER',
      choices: ['chrome', 'firefox'],
      description: 'Браузер для запуска тестов'
    )
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Docker Build') {
      steps {
        sh "docker build -t ${REGISTRY}/ui-tests:latest ."
      }
    }

    stage('Push to Registry') {
      steps {
        sh "docker push ${REGISTRY}/ui-tests:latest"
      }
    }

    stage('Run Tests') {
      steps {
        sh """
          docker run --name ui-tests-\${BUILD_NUMBER} ${REGISTRY}/ui-tests:latest --base_url ${params.BASE_URL} --browser ${params.BROWSER} || true
          docker cp ui-tests-\${BUILD_NUMBER}:/root/ui_test/target/allure-results ./allure-results
          docker rm ui-tests-\${BUILD_NUMBER}
        """
      }
    }

    stage('Allure Report') {
      steps {
        script {
          allure([
            includeProperties: false,
            jdk: '',
            reportBuildPolicy: 'ALWAYS',
            results: [[path: 'allure-results']]
          ])
        }
      }
    }

    stage('Cleanup') {
      steps {
        sh "docker rmi ${REGISTRY}/ui-tests:latest || true"
      }
    }
  }
}