[1]: https://github.com/DV8FromTheWorld/JDA/

# JDA-Kotlinx

Extensions and utilities for building Discord bots using [JDA][1] in Kotlin.

## Usage

To use any of the features, first register the suspending event manager.

```kotlin
val jda = JDABuilder.createDefault("token")
    .useSuspendEventManager()
    .build()
```

```kotlin
val shardManager = DefaultShardManagerBuilder.createDefault("token")
    .useSuspendEventManager()
    .build()
```

### Events

This library provides utility functions for listening to and awaiting events. The consumers for handling events are
suspending, allowing for the use of suspending functions within them.

```kotlin
jda.on<MessageReceivedEvent> {
    val reply = message.reply("Waiting for a message...").await()

    jda.await<MessageReceivedEvent> {
        reply?.editMessage("Received another message!")?.queue()
    }
}
```

There are also extension functions on channels and messages for awaiting messages/reactions respectively.

```kotlin
jda.on<MessageReceivedEvent> {
    val reply = message.reply("Waiting for messages...").await()!!

    channel.awaitMessage {
        reply.editMessage("Received a message by ${author.name}!").queue()
    }
}
```

```kotlin
jda.on<MessageReceivedEvent> {
    val reply = message.reply("Waiting for reactions...").await()!!

    reply.awaitReaction {
        reply.editMessage("Received a reaction by ${user?.name}!").queue()
    }
}
```

The `await` functions wait for any single event of the given type within thirty seconds by default. This can be changed
using the optional function parameters.

```kotlin
jda.on<MessageReceivedEvent> {
    val reply = message.reply("Waiting for reactions...").await()!!

    var reactions = 0
    reply.awaitReaction(
        predicate = { reactionEmote.isEmoji }, // Only handle emoji reactions
        limit = 5, // Handle the next five matching events
        timeout = 60_000, // Wait for events for one minute
        onTimeout = {
            reply.editMessage("Received $reactions reactions within one minute!").queue()
        } // Edit the message after timeout expires
    ) {
        reply.editMessage("Received ${++reactions} reactions!").queue()
    }
}
```

## Delegates

There are a couple of utility delegates for logging and environment variables.

```kotlin
val log by Logger // = LoggerFactory.getLogger(this::class.java)
```

```kotlin
object SampleConfig {
    val shardsTotal by Env.Int // = System.getenv("SAMPLE_CONFIG_SHARDS_TOTAL")?.toInt()

    object Discord {
        val token by Env // = System.getenv("SAMPLE_CONFIG_DISCORD_TOKEN")
    }
}
// ...
val shardManager = DefaultShardManagerBuilder.createDefault(SampleConfig.Discord.token)
    .setShardsTotal(SampleConfig.shardsTotal ?: 1)
    .build()
```

## Download

### Gradle

```gradle
repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases")
    maven("https://jitpack.io/")
}

dependencies {
    implementation("net.dv8tion:JDA:${JDA_VERSION}")
    implementation("com.github.ruffrick:jda-kotlinx:${COMMIT}")
}
```

### Maven

```maven
<repository>
    <id>dv8tion</id>
    <name>m2-dv8tion</name>
    <url>https://m2.dv8tion.net/releases</url>
</repository>
<repository>
    <id>jitpack</id>
    <name>jitpack</name>
    <url>https://jitpack.io/</url>
</repository>
```

```maven
<dependency>
  <groupId>net.dv8tion</groupId>
  <artifactId>JDA</artifactId>
  <version>$JDA_VERSION</version>
</dependency>
<dependency>
  <groupId>com.github.ruffrick</groupId>
  <artifactId>jda-kotlinx</artifactId>
  <version>$COMMIT</version>
</dependency>
```
