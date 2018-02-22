package party.liyin.protocolrouter

import java.nio.channels.Pipe
import java.nio.channels.SelectionKey

class InputChannel {
    companion object {
        fun register(name: String) : Pipe.SinkChannel{
            val pipe = Pipe.open()
            GlobalStore.pipeList.put(name,pipe)
            pipe.source().apply { this.configureBlocking(false) }.register(GlobalStore.selector,SelectionKey.OP_READ, name)
            return pipe.sink().apply { this.configureBlocking(false) }
        }
        fun unregister(name:String) {
            val keys = GlobalStore.selector.keys().filter { it.attachment() == name }.firstOrNull()
            keys?.interestOps(0)
            GlobalStore.pipeList.remove(name)
        }
    }
}