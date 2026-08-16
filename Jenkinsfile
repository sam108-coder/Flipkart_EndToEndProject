pipeline {

    /*
     * ============================================================
     * PIPELINE CONFIGURATION
     * ============================================================
     */

    agent any

    options {

        // Do not allow two executions of the same job at the same time
        disableConcurrentBuilds()

        // Keep last 20 builds
        buildDiscarder(
            logRotator(
                numToKeepStr: '20',
                artifactNumToKeepStr: '10'
            )
        )

        // Add timestamps to console logs
        timestamps()

        // Stop pipeline if it runs longer than 60 minutes
        timeout(
            time: 60,
            unit: 'MINUTES'
        )

        // Skip default Jenkins checkout because we perform checkout manually
        skipDefaultCheckout(true)
    }

    /*
     * ============================================================
     * PARAMETERS
     * ============================================================
     */

    parameters {

        choice(
            name: 'ENVIRONMENT',
            choices: [
                'QA',
                'UAT',
                'PROD'
            ],
            description: 'Environment against which tests should execute'
        )

        choice(
            name: 'BROWSER',
            choices: [
                'chrome',
                'firefox'
            ],
            description: 'Browser for Selenium execution'
        )

        choice(
            name: 'TEST_TYPE',
            choices: [
                'all',
                'smoke',
                'regression'
            ],
            description: 'Select test suite'
        )

        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run browser in headless mode'
        )

        string(
            name: 'CUCUMBER_TAGS',
            defaultValue: '',
            description: 'Optional Cucumber tags. Example: @smoke'
        )
    }

    /*
     * ============================================================
     * ENVIRONMENT VARIABLES
     * ============================================================
     */

    environment {

        /*
         * Jenkins tool names.
         *
         * Configure these under:
         *
         * Manage Jenkins
         *       ↓
         * Tools
         */

        JAVA_HOME = tool 'JDK21'

        MAVEN_HOME = tool 'Maven'

        PATH = "${JAVA_HOME}\\bin;${MAVEN_HOME}\\bin;${env.PATH}"

        /*
         * Jenkins credentials.
         *
         * Create these in:
         *
         * Manage Jenkins
         *       ↓
         * Credentials
         */

        DB_CREDENTIALS =
            credentials('mysql-automation-db')

        SMTP_CREDENTIALS =
            credentials('smtp-client-email')

        /*
         * Application environment.
         */

        TEST_ENV = "${params.ENVIRONMENT}"

        TEST_BROWSER = "${params.BROWSER}"

        TEST_HEADLESS = "${params.HEADLESS}"

        /*
         * Report locations.
         */

        REPORT_DIR = 'target/cucumber-reports'

        SCREENSHOT_DIR = 'target/screenshots'

        LOG_DIR = 'logs'
    }

    /*
     * ============================================================
     * STAGES
     * ============================================================
     */

    stages {

        /*
         * --------------------------------------------------------
         * 1. CHECKOUT
         * --------------------------------------------------------
         */

        stage('Checkout') {

            steps {

                echo '=============================================='
                echo 'CHECKING OUT SOURCE CODE'
                echo '=============================================='

                checkout scm

                bat 'git rev-parse --short HEAD'
            }
        }


        /*
         * --------------------------------------------------------
         * 2. ENVIRONMENT INFORMATION
         * --------------------------------------------------------
         */

        stage('Environment Information') {

            steps {

                echo '=============================================='
                echo 'ENVIRONMENT INFORMATION'
                echo '=============================================='

                bat 'java -version'

                bat 'mvn -version'

                echo "Environment : ${env.TEST_ENV}"

                echo "Browser     : ${env.TEST_BROWSER}"

                echo "Headless    : ${env.TEST_HEADLESS}"

                echo "Test Type   : ${params.TEST_TYPE}"

                echo "Build Number: ${env.BUILD_NUMBER}"
            }
        }


        /*
         * --------------------------------------------------------
         * 3. CLEAN
         * --------------------------------------------------------
         */

        stage('Clean') {

            steps {

                echo '=============================================='
                echo 'CLEANING PROJECT'
                echo '=============================================='

                bat 'mvn clean'
            }
        }


        /*
         * --------------------------------------------------------
         * 4. COMPILE
         * --------------------------------------------------------
         */

        stage('Compile') {

            steps {

                echo '=============================================='
                echo 'COMPILING JAVA CODE'
                echo '=============================================='

                bat 'mvn test-compile -DskipTests'
            }
        }


        /*
         * --------------------------------------------------------
         * 5. RUN AUTOMATION TESTS
         * --------------------------------------------------------
         */

        stage('Run Automation Tests') {

            steps {

                echo '=============================================='
                echo 'RUNNING AUTOMATION TESTS'
                echo '=============================================='

                script {

                    /*
                     * ------------------------------------------------
                     * Determine Cucumber tag
                     * ------------------------------------------------
                     */

                    def tags = ''

                    if (params.CUCUMBER_TAGS?.trim()) {

                        tags =
                            "-Dcucumber.filter.tags=\"${params.CUCUMBER_TAGS}\""

                    }
                    else if (params.TEST_TYPE == 'smoke') {

                        tags =
                            '-Dcucumber.filter.tags="@smoke"'

                    }
                    else if (params.TEST_TYPE == 'regression') {

                        tags =
                            '-Dcucumber.filter.tags="@regression"'
                    }

                    /*
                     * ------------------------------------------------
                     * Maven command
                     * ------------------------------------------------
                     */

                    def command = """
                        mvn test ^
                        -Denv=${env.TEST_ENV} ^
                        -Dbrowser=${env.TEST_BROWSER} ^
                        -Dheadless=${env.TEST_HEADLESS} ^
                        -DbuildNumber=${env.BUILD_NUMBER} ^
                        ${tags}
                    """

                    echo "Executing:"
                    echo command

                    /*
                     * Run Maven.
                     *
                     * returnStatus allows the pipeline to continue
                     * to the reporting stages even if tests fail.
                     */

                    def testResult =
                        bat(
                            script: command,
                            returnStatus: true
                        )

                    /*
                     * Store result for later stages.
                     */

                    env.TEST_EXIT_CODE =
                        testResult.toString()

                    echo "Maven exit code: ${testResult}"

                    /*
                     * Do not immediately fail the pipeline.
                     *
                     * Reports need to be published first.
                     */

                    if (testResult != 0) {

                        currentBuild.result = 'UNSTABLE'

                        echo 'Some automation tests failed.'
                        echo 'Continuing to report generation.'
                    }
                }
            }
        }


        /*
         * --------------------------------------------------------
         * 6. PUBLISH JUNIT / TESTNG RESULTS
         * --------------------------------------------------------
         */

        stage('Publish Test Results') {

            steps {

                echo '=============================================='
                echo 'PUBLISHING TEST RESULTS'
                echo '=============================================='

                script {

                    /*
                     * Surefire XML files.
                     */

                    junit(
                        testResults:
                            'target/surefire-reports/*.xml',

                        allowEmptyResults:
                            true,

                        skipPublishingChecks:
                            true
                    )
                }
            }
        }


        /*
         * --------------------------------------------------------
         * 7. PUBLISH CUCUMBER REPORT
         * --------------------------------------------------------
         */

        stage('Publish Cucumber Report') {

            steps {

                echo '=============================================='
                echo 'PUBLISHING CUCUMBER REPORT'
                echo '=============================================='

                script {

                    /*
                     * HTML Publisher plugin.
                     *
                     * Plugin:
                     * HTML Publisher
                     */

                    publishHTML(
                        target: [

                            reportDir:
                                "${env.REPORT_DIR}",

                            reportFiles:
                                'cucumber.html',

                            reportName:
                                'Cucumber Automation Report',

                            reportTitle:
                                'Automation Execution Report',

                            allowMissing:
                                true,

                            alwaysLinkToLastBuild:
                                true,

                            keepAll:
                                true,

                            includes:
                                '**/*.html'
                        ]
                    )
                }
            }
        }


        /*
         * --------------------------------------------------------
         * 8. ARCHIVE REPORTS
         * --------------------------------------------------------
         */

        stage('Archive Reports') {

            steps {

                echo '=============================================='
                echo 'ARCHIVING REPORTS'
                echo '=============================================='

                archiveArtifacts(

                    artifacts:
                        '''
                        target/cucumber-reports/**/*,
                        target/surefire-reports/**/*,
                        target/screenshots/**/*,
                        reports/**/*,
                        logs/**/*
                        ''',

                    allowEmptyArchive:
                        true,

                    fingerprint:
                        true
                )
            }
        }


        /*
         * --------------------------------------------------------
         * 9. DATABASE VALIDATION
         * --------------------------------------------------------
         */

        stage('Database Validation') {

            steps {

                echo '=============================================='
                echo 'MYSQL DATABASE VALIDATION'
                echo '=============================================='

                script {

                    /*
                     * The actual test framework should insert
                     * execution results into MySQL.
                     *
                     * Jenkins only verifies that the DB values
                     * are available as environment variables.
                     */

                    echo "MySQL username configured: ${DB_CREDENTIALS_USR}"

                    echo "MySQL password configured: ****"

                    echo "Execution ID: ${env.JOB_NAME}-${env.BUILD_NUMBER}"
                }
            }
        }
    }


    /*
     * ============================================================
     * POST ACTIONS
     * ============================================================
     */

    post {

        /*
         * --------------------------------------------------------
         * ALWAYS
         * --------------------------------------------------------
         */

        always {

            echo '=============================================='
            echo 'POST BUILD PROCESSING'
            echo '=============================================='

            script {

                /*
                 * Create a build summary file.
                 */

                bat """
                    if not exist reports mkdir reports

                    echo Job Name: ${env.JOB_NAME} > reports/build-summary.txt

                    echo Build Number: ${env.BUILD_NUMBER} >> reports/build-summary.txt

                    echo Build URL: ${env.BUILD_URL} >> reports/build-summary.txt

                    echo Environment: ${env.TEST_ENV} >> reports/build-summary.txt

                    echo Browser: ${env.TEST_BROWSER} >> reports/build-summary.txt

                    echo Test Type: ${params.TEST_TYPE} >> reports/build-summary.txt

                    echo Test Exit Code: ${env.TEST_EXIT_CODE} >> reports/build-summary.txt

                    echo Result: ${currentBuild.currentResult} >> reports/build-summary.txt
                """

                /*
                 * Archive summary.
                 */

                archiveArtifacts(

                    artifacts:
                        'reports/build-summary.txt',

                    allowEmptyArchive:
                        true
                )
            }
        }


        /*
         * --------------------------------------------------------
         * SUCCESS
         * --------------------------------------------------------
         */

        success {

            echo '=============================================='
            echo 'AUTOMATION EXECUTION SUCCESSFUL'
            echo '=============================================='

            script {

                emailext(

                    subject:
                        "PASSED - Automation Report - ${env.JOB_NAME} #${env.BUILD_NUMBER}",

                    body:
                        """
                        Hi Team,

                        Automation execution completed successfully.

                        ==========================================
                        AUTOMATION EXECUTION SUMMARY
                        ==========================================

                        Job         : ${env.JOB_NAME}
                        Build       : ${env.BUILD_NUMBER}
                        Environment : ${env.TEST_ENV}
                        Browser     : ${env.TEST_BROWSER}
                        Test Type   : ${params.TEST_TYPE}
                        Result      : PASSED

                        Jenkins URL:
                        ${env.BUILD_URL}

                        Cucumber Report:
                        ${env.BUILD_URL}Cucumber_20Automation_20Report/

                        Regards,
                        QA Automation Team
                        """,

                    to:
                        'client@example.com',

                    from:
                        "${SMTP_CREDENTIALS_USR}",

                    replyTo:
                        "${SMTP_CREDENTIALS_USR}",

                    attachmentsPattern:
                        'target/cucumber-reports/**/*.*',

                    attachLog:
                        false
                )
            }
        }


        /*
         * --------------------------------------------------------
         * UNSTABLE
         * --------------------------------------------------------
         */

        unstable {

            echo '=============================================='
            echo 'AUTOMATION EXECUTION HAS FAILURES'
            echo '=============================================='

            script {

                emailext(

                    subject:
                        "FAILED TESTS - Automation Report - ${env.JOB_NAME} #${env.BUILD_NUMBER}",

                    body:
                        """
                        Hi Team,

                        Automation execution completed, but some
                        test cases have failed.

                        ==========================================
                        AUTOMATION EXECUTION SUMMARY
                        ==========================================

                        Job         : ${env.JOB_NAME}
                        Build       : ${env.BUILD_NUMBER}
                        Environment : ${env.TEST_ENV}
                        Browser     : ${env.TEST_BROWSER}
                        Test Type   : ${params.TEST_TYPE}
                        Result      : FAILED / UNSTABLE

                        Jenkins URL:
                        ${env.BUILD_URL}

                        Please review the attached automation report
                        and screenshots.

                        Regards,
                        QA Automation Team
                        """,

                    to:
                        'client@example.com',

                    from:
                        "${SMTP_CREDENTIALS_USR}",

                    replyTo:
                        "${SMTP_CREDENTIALS_USR}",

                    attachmentsPattern:
                        'target/cucumber-reports/**/*.*',

                    attachLog:
                        true
                )
            }
        }


        /*
         * --------------------------------------------------------
         * FAILURE
         * --------------------------------------------------------
         */

        failure {

            echo '=============================================='
            echo 'JENKINS PIPELINE FAILED'
            echo '=============================================='

            script {

                emailext(

                    subject:
                        "PIPELINE FAILED - ${env.JOB_NAME} #${env.BUILD_NUMBER}",

                    body:
                        """
                        Hi Team,

                        The Jenkins automation pipeline itself has failed.

                        ==========================================
                        PIPELINE SUMMARY
                        ==========================================

                        Job         : ${env.JOB_NAME}
                        Build       : ${env.BUILD_NUMBER}
                        Environment : ${env.TEST_ENV}
                        Browser     : ${env.TEST_BROWSER}

                        Jenkins URL:
                        ${env.BUILD_URL}

                        Please check the Jenkins console log
                        for the root cause.

                        Regards,
                        QA Automation Team
                        """,

                    to:
                        'client@example.com',

                    from:
                        "${SMTP_CREDENTIALS_USR}",

                    replyTo:
                        "${SMTP_CREDENTIALS_USR}",

                    attachLog:
                        true
                )
            }
        }


        /*
         * --------------------------------------------------------
         * ABORTED
         * --------------------------------------------------------
         */

        aborted {

            echo '=============================================='
            echo 'JENKINS BUILD ABORTED'
            echo '=============================================='

            script {

                emailext(

                    subject:
                        "ABORTED - Automation Build - ${env.JOB_NAME} #${env.BUILD_NUMBER}",

                    body:
                        """
                        Hi Team,

                        The automation Jenkins build was aborted.

                        Job:
                        ${env.JOB_NAME}

                        Build:
                        ${env.BUILD_NUMBER}

                        Environment:
                        ${env.TEST_ENV}

                        Jenkins URL:
                        ${env.BUILD_URL}

                        Regards,
                        QA Automation Team
                        """,

                    to:
                        'client@example.com',

                    from:
                        "${SMTP_CREDENTIALS_USR}",

                    replyTo:
                        "${SMTP_CREDENTIALS_USR}"
                )
            }
        }


        /*
         * --------------------------------------------------------
         * CLEANUP
         * --------------------------------------------------------
         */

        cleanup {

            echo '=============================================='
            echo 'CLEANING WORKSPACE'
            echo '=============================================='

            cleanWs(
                deleteDirs: true,
                disableDeferredWipeout: true
            )
        }
    }
}