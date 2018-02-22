package party.liyin.protocolrouter

import java.util.concurrent.Semaphore
import java.util.function.Function

class OutputChannel {
    companion object {
        private val lock = Semaphore(1)
        fun addProtocolFunction(func:Function<RawDataBlock,Void>) : Int {
            lock.acquireUninterruptibly()
            if (GlobalStore.processfuncIndex.isEmpty()) {
                GlobalStore.processfuncIndex.add(GlobalStore.processfunc.size)
            }
            val index = GlobalStore.processfuncIndex.first()
            GlobalStore.processfunc.put(index, func)
            GlobalStore.processfuncIndex.remove(index)
            lock.release()
            return index
        }
        fun removeProtocolFunction(index:Int) {
            lock.acquireUninterruptibly()
            GlobalStore.processfunc.remove(index)
            GlobalStore.processfuncIndex.add(index)
            lock.release()
        }
    }
}