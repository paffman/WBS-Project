# WBS-Tool

The WBS-Tool is a project managment tool combining the Work Breakdown
Structure and Earned Value Analysis.

## Build

[gradle](http://www.gradle.org/) is used to build, test and deploy the
application. If you want to learn more about it check out the
[Documentation](http://www.gradle.org/documentation) or the
[Learn](http://www.gradle.org/learn) page.

There are two ways to use gradle with this project. You can either use your own gradle installation or the gradle wrapper (gradlew) that you will find in our repo after cloning. Using the wrapper does not require you to install gradle on your machine.

Git is used to generate the version tag to make sure `git` is on your path.

### Using Gradle

First of all, open a Terminal of your choice and navigate to the project root
(`cd path/to/project/root`). After that you can use `gradlew tasks` (if using the wrapper) or to see a
list of all available tasks. Here are some of the tasks you will probably use
most often.

- build the project: `gradlew build`
- build excluding a task: `gradlew build -x [TASK_NAME]`, for example `gradlew build -x test`
- run tests: `gradlew test`
- run main class: `gradlew run`
- run checkstyle on your project: `gradlew checkstyleMain`
- create a html checkstyle report: `gradlew csReport`

When building the project gradlew will generate a `build` folder and put all the
build related output into that folder. If you want to get rid of the build
folder just run `gradlew clean`.
