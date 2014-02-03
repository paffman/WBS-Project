# WBS-Tool

## Build

[gradle](http://www.gradle.org/) is used to build, test and deploy the
application. If you want to learn more about it check out the
[Documentation](http://www.gradle.org/documentation) or the
[Learn](http://www.gradle.org/learn) page.

### Install gradle

Install gradle 1.8 from [http://www.gradle.org/downloads](http://www.gradle.org/downloads).

### Using Gradle

First of all, open a Terminal of your choice and navigate to the project root
(`cd path/to/project/root`). After that you can use `gradle tasks` to see a
list of all available tasks. Here are some of the tasks you will probably use
most often.

- build the project: `gradle build`
- build excluding a task: `gradle build -x [TASK_NAME]`, for example `gradle build -x test`
- run tests: `gradle test`
- run main class: `gradle run`

When building the project gradle will generate a `build` folder and put all the
build related output into that folder. If you want to get rid of the build
folder just run `gradle clean`.
