package top.thsunkist.brainhealthy.network

data class ServerInfo(
    val name: String,
    var description: String,
    val key: String,
    val baseUrl: String,
    val agoraAppID: String,
    val rongCloudAppKey: String,
)

const val kProductionServer = "production"
const val kTestServer = "test"
const val kStagingServer = "staging"
const val kLocalServer = "localhost"

val availableServers = mapOf(
    kProductionServer to ServerInfo(
        name = "Production",
        description = " N/A",
        key = kProductionServer,
        baseUrl = "https://api.faceline.live",
        agoraAppID = "fc3e087f701b4f788099e1924c3cc7b0",
        rongCloudAppKey = "uwd1c0sxu5t41"
    ),

    kTestServer to ServerInfo(
        name = "Test",
        description = " N/A",
        key = kTestServer,
        baseUrl = "http://test.faceline.live",
        agoraAppID = "2e2e3d159acc42959fd7416e9967f997",
        rongCloudAppKey = "pvxdm17jpe9tr"
    ),

    kStagingServer to ServerInfo(
        name = "Staging",
        description = " N/A",
        key = kStagingServer,
        baseUrl = "http://test.faceline.live",
        agoraAppID = "2e2e3d159acc42959fd7416e9967f997",
        rongCloudAppKey = "pvxdm17jpe9tr"
    ),

    kLocalServer to ServerInfo(
        name = "Local",
        description = " N/A",
        key = kLocalServer,
        baseUrl = "http://192.168.1.102:8089",
        agoraAppID = "2e2e3d159acc42959fd7416e9967f997",
        rongCloudAppKey = "pvxdm17jpe9tr"
    )
)