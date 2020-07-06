import com.moowork.gradle.node.npm.NpmTask

plugins {
    id("com.github.node-gradle.node") version "2.2.4"
}

node {
    version = "12.14.1"
    npmVersion = "6.13.4"
    download = true
    workDir = file("${project.buildDir}/frontend")
    nodeModulesDir = file("${project.projectDir}")
}

tasks.register<NpmTask>("build") {
    setArgs(listOf("run", "build"))
    dependsOn("npmInstall")
}

