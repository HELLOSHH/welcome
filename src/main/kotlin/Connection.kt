import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL

class Connection {
    var instance: Connection? = null
        get() {
            if (instance == null) instance = Connection()
            return instance
        }

    // POST /action
    //      X-Auth-Token: {Token}
    //      Content-Type: application/json
    fun action(commandArrays: JsonArray<JsonObject>): JsonObject? {
        println("\n\n>>>> api.action()")
        var conn: HttpURLConnection? = null
        var responseJson: JsonObject? = null

        val url = URL(GlobalData.HOST_URL + GlobalData.POST_ACTION)
        conn = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.setRequestProperty("X-Auth-Token", TokenManager.instance?.token)
        conn.setRequestProperty("Content-Type", "application/json")
        conn.doOutput = true

        val bw = conn.outputStream.bufferedWriter()
        var commands = JsonObject()
        commands["commands"] = commandArrays
        bw.write(commands.toString())
        bw.flush()
        bw.close()

        val responseCode = conn.responseCode
        when (responseCode) {
            400 -> println("$responseCode:: 해당 명령을 실행할 수 없음. (실행할 수 없는 상태일 때, 엘리베이터 수와 Command 수가 일치하지 않을 때, 엘리베이터 정원을 초과하여 태울 때)")
            401 -> println("$responseCode:: X-Auth-Token Header가 잘못됨")
            500 -> println("$responseCode:: 서버 에러, 문의 필요")
            else -> {
                val br = conn.inputStream.bufferedReader()
                val sb = StringBuilder()
                var line = br.readLine()
                while (line != null) {
                    sb.append(line)
                    line = br.readLine()
                }

//                responseJson = JsonObject(sb.toString())
            }
        }
        return responseJson
    }
}