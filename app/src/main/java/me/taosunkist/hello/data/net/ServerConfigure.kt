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
        baseUrl = "https://api.faceline.live:8089",
    ),

    kTestServer to ServerInfo(
        name = "Test",
        description = " N/A",
        key = kTestServer,
        baseUrl = "http://test.faceline.live",
    ),

    kLocalServer to ServerInfo(
        name = "Local",
        description = " N/A",
        key = kLocalServer,
        baseUrl = "http://192.168.1.102:8089",
    )
)