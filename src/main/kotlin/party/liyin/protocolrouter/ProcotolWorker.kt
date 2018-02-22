package party.liyin.protocolrouter

import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.channels.Pipe
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore

class ProcotolWorker {
    companion object {
        private var thread: Thread? = null
        private var service = Executors.newFixedThreadPool(10)
        private var lock = Semaphore(1)
        private var threadWorking = false
        fun startUp() {
            if (thread != null) {
                return
            }
            threadWorking = true
            thread = Thread(Runnable {
                while (threadWorking) {
                    try {
                        val readyChannels = GlobalStore.selector.selectNow()
                        if (readyChannels == 0) continue
                        val selectionKeyIter = GlobalStore.selector.selectedKeys().iterator()
                        while (selectionKeyIter.hasNext()) {
                            lock.acquireUninterruptibly()
                            val it = selectionKeyIter.next()
                            val channel = it.channel() as Pipe.SourceChannel
                            val buffer = ByteBuffer.allocate(1024)
                            val outputStream = ByteArrayOutputStream()
                            try {
                                var length: Int = channel.read(buffer)
                                while (length > 0) {
                                    buffer.flip()
                                    outputStream.write(buffer.array(), 0, length)
                                    buffer.clear()
                                    length = channel.read(buffer)
                                }
                                if (outputStream.size() > 0) {
                                    val data = RawDataBlock(outputStream.toByteArray(), it.attachment() as String)
                                    GlobalStore.processfunc.forEach {
                                        service.execute {
                                            it.value.apply(data)
                                        }
                                    }
                                }
                            } catch(interrupt : java.lang.Exception) {
                                interrupt.printStackTrace()
                            } finally {
                                lock.release()
                                it.cancel()
                            }
                        }
                    } catch (ignore: java.lang.Exception) {
                        ignore.printStackTrace()
                    }
                }
            }).apply {
                this.isDaemon = true
                this.start()
            }
        }

        fun stop() {
            threadWorking = false
            thread?.join()
            thread = null
        }

    }
}