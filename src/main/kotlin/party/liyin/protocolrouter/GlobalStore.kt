package party.liyin.protocolrouter

import java.nio.channels.Pipe
import java.nio.channels.Selector
import java.util.*
import java.util.function.Function

class GlobalStore {
    companion object {
        val pipeList = mutableMapOf<String,Pipe>()
        val selector = Selector.open()
        val processfunc : MutableMap<Int, Function<RawDataBlock,Unit>> = mutableMapOf()
        var processfuncIndex : MutableSet<Int> = mutableSetOf()
    }
}
data class RawDataBlock(val data: ByteArray,val name: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RawDataBlock

        if (!Arrays.equals(data, other.data)) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = Arrays.hashCode(data)
        result = 31 * result + name.hashCode()
        return result
    }
}