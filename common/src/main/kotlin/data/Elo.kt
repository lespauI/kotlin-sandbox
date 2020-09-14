package kt.sandbox.data

data class Elo(val elo: Int, val name: String)
{
    override fun toString(): String {
        return "$elo - ${name.replace("@","")}\n"
    }
}
