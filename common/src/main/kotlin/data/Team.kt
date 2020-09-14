package kt.sandbox.data

data class Team(val id: Int, val teamName: String) {
    val list = listOf(
        "Bears", "Bengals", "Bills", "Broncos", "Browns", "Buccaneers", "Cardinals", "Chargers", "Chiefs",
        "Colts", "Cowboys", "Dolphins", "Eagles", "Falcons", "49ers", "Giants", "Jaguars", "Jets", "Lions",
        "Packers", "Panthers", "Patriots", "Raiders", "Rams", "Ravens", "Redskins", "Saints", "Seahawks",
        "Steelers", "Titans", "Vikings", "Texans"
    )

    fun getTeamById(id: Int): Team {
        return Team(id, list[id])
    }

    fun getTeamByName(name: String): Team {
        //if(list.indexOf(name) > -1)
        return Team(list.indexOf(name), list[list.indexOf(name)])
    }
}

