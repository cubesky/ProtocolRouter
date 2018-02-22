import party.liyin.protocolrouter.InputChannel;
import party.liyin.protocolrouter.OutputChannel;
import party.liyin.protocolrouter.ProcotolWorker;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

public class Test {
    @org.junit.Test
    public void test() throws IOException {
        System.out.println("[I] Register");
        Pipe.SinkChannel sink = InputChannel.Companion.register("testchannel");
        System.out.println("[I] Send Test-Pre");
        sink.write(ByteBuffer.wrap("Test-Pre".getBytes()));
        System.out.println("[I] Startup");
        ProcotolWorker.Companion.startUp();
        System.out.println("[I] Add Protocol");
        int index = OutputChannel.Companion.addProtocolFunction(rawDataBlock -> {
            System.out.println("Load: " + new String(rawDataBlock.getData()) + " on " + rawDataBlock.getName());
            return null;
        });
        System.out.println("[I] Send Test");
        sink.write(ByteBuffer.wrap("Test".getBytes()));
        System.out.println("[I] Stop");
        ProcotolWorker.Companion.stop();
        System.out.println("[I] Send Test-Stop");
        sink.write(ByteBuffer.wrap("Test-Stop".getBytes()));
        System.out.println("[I] Startup");
        ProcotolWorker.Companion.startUp();
        System.out.println("[I] Send Test-Restart");
        sink.write(ByteBuffer.wrap("Test-Restart".getBytes()));
        System.out.println("[I] Remove Protocol");
        OutputChannel.Companion.removeProtocolFunction(index);
        System.out.println("[I] Send Test-Remove");
        sink.write(ByteBuffer.wrap("Test-Remove".getBytes()));
        System.out.println("[I] Add Protocol");
        OutputChannel.Companion.addProtocolFunction(rawDataBlock -> {
            System.out.println("Load: " + new String(rawDataBlock.getData()) + " on " + rawDataBlock.getName());
            return null;
        });
        System.out.println("[I] UnRegister");
        InputChannel.Companion.unregister("testchannel");
        System.out.println("[I] Send Test-NoChannel");
        try {
            sink.write(ByteBuffer.wrap("Test-NoChannel".getBytes()));
        }catch (Exception e) {
            System.out.println("Pass");
        }
        System.out.println("[I] Register");
        sink = InputChannel.Companion.register("testchannel");
        System.out.println("[I] Send Test-NewChannel");
        sink.write(ByteBuffer.wrap("Test-NewChannel".getBytes()));
        System.out.println("[I] Stop");
        ProcotolWorker.Companion.stop();
    }
}
