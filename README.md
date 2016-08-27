This Version control user statistics analyzer is a web based statistical analysis tool for git repositories. 

Stats corresponding to authors, files and code compilations are displayed in the application.

The tool dynamically generates a Jenkins poll scm job for every project added using the Jenkins Job DSL Plugin. Various statistics are then obtained from the git logs the JGit API. 

The Poll SCM job will ensure that the statistics are updated regularly.

The project is built on the Jenkins server on every commit and compilation errors and other details are captured from the build console output.
