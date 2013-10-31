# WBS-Tool

## Build

[Gradle](http://www.gradle.org/) is used to build the project.

    # regular build
    gradle build

    # build excluding a task
    gradle build -x [TASKNAME]

    # run application
    gradle run

The tests are not working properly right now, so use `gradle build -x test` to build the project.
