# Protocol Data Router
A Library written in kotlin to transform low-level data byte array to Protocol with mark and route them to another system.

## Install
You can import this library via [CubeSky Repo](https://cubesky-mvn.github.io)

## API
This API use Pipe NIO to transform data between systems.

### Startup ProcotolWorker
```java
ProcotolWorker.Companion.startUp();
```

### Stop ProcotolWorker
```java
ProcotolWorker.Companion.stop();
```

### Register an InputChannel
```java
Pipe.SinkChannel sink = InputChannel.Companion.register("channelname");
```

### Unregister an InputChannel
```java
InputChannel.Companion.unregister("channelname");
```

### Send data to channel
```java
sink.write(ByteBuffer.wrap("Data".getBytes()));
```

If this is called after `unregister` , it will return an Exception.

### Register an OutputChannel
```java
int id = OutputChannel.Companion.addProtocolFunction(rawDataBlock -> {
     System.out.println("Load: " + new String(rawDataBlock.getData()) + " on " + rawDataBlock.getName());
     return;
 });
```

This method will return an `id` of this `OutputChannel`.

This function will called when InputChannel input any data.

### Unregister an OutputChannel
```java
OutputChannel.Companion.removeProtocolFunction(id);
```

## License
Apache License 2.0