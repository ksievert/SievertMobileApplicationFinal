package hu.ait.sievertmobileapplicationfinal.data

object SearchParser { //I think this should be a class?
    var position = 0
    fun getStopIDs(response: String): MutableList<String> {
        var ids = mutableListOf<String>()
        var desiredLabel = "stopIds"
        var position = response.indexOf(desiredLabel, 0)
        if (position == -1) return ids
        while(response[position] != '[') position++
        while(response[position] != ']') {
            ids.add(getNextID(response))
            position++
        }
        position = 0
        return ids
    }

    fun getNextID(response:String): String {
        var nextID = ""
        while(response[position] != '\"') position++
        position++
        while(response[position] != '\"') {
            nextID += response[position]
        }
        position++
        return nextID
    }
}