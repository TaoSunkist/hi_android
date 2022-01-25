package me.taosunkist.hello.data.net

data class ServerInfo(
    val name: String,
    var description: String,
    val key: String,
    val baseUrl: String,
)

const val kProductionServer = "production"
const val kTestServer = "test"
const val kLocalServer = "localhost"

val availableServers = mapOf(
    kProductionServer to ServerInfo(
        name = "Production",
        description = " N/A",
        key = kProductionServer,
        baseUrl = "http://192.168.0.140:8080",
    ),

    kTestServer to ServerInfo(
        name = "Test",
        description = " N/A",
        key = kTestServer,
        baseUrl = "http://192.168.0.140:8080",
    ),

    kLocalServer to ServerInfo(
        name = "Local",
        description = " N/A",
        key = kLocalServer,
        baseUrl = "http://192.168.0.140:8080",
    )
)