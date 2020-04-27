# 配置 #
## first setup protogen ##

## project root build.gradle ##
```
buildscript {
    repositories {
        jcenter()
        google()
        mavenCentral()
        maven { url 'https://maven.aliyun.com/repository/releases' }
    }

    dependencies {
        classpath 'com.android.tools.build:gradle:3.6.3'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.71"
        classpath 'com.google.protobuf:protobuf-gradle-plugin:0.8.8'
        classpath 'com.jakewharton:butterknife-gradle-plugin:10.2.1'
    }
}
```

## app root build.gradle ##
```
apply plugin: 'com.google.protobuf'
android {
    sourceSets {
        main {
            proto {
                // In addition to the default 'src/main/proto'
                srcDir 'src/main/protobuf'
                // In addition to the default '**/*.proto' (use with caution).
                // Using an extension other than 'proto' is NOT recommended,
                // because when proto files are published along with class files, we can
                // only tell the type of a file from its extension.
                include '**/*.protodevel'
            }
        }
    }
}
protobuf {
    protoc { artifact = 'com.google.protobuf:protoc:3.6.1' }
    protoc { path = '/Users/taohui/DevStation/opt/protobuf/protoc-3.11.4-osx-x86_64/bin/protoc' }
    plugins {
        javalite { artifact = "com.google.protobuf:protoc-gen-javalite:3.0.0" }
        grpc { artifact = 'io.grpc:protoc-gen-grpc-java:1.19.0' }
    }

    generateProtoTasks {
        all().each { task ->
            task.plugins {
                javalite {}
                grpc { // Options added to --grpc_out
                    option 'lite'
                }
            }
        }
    }
}
```

Gradle build column
choose generateDebugProto

