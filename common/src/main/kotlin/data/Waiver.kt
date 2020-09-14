package kt.sandbox.data

data class Waiver(val id: Int, val text: String)
{
    override fun toString(): String {
        return text + "\n"
    }
}
