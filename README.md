# SkriptBot
Java rewrite of Pikachu's Skript bot

Made for the skUnity discord server.

# Building

Lombok is used! If you're using an IDE, make sure to install the appropriate Lombok plugin. 
(IntelliJ has it bundled as of 2020.3.4)

Building is done through Gradle, just use the command

``gradlew build test``

If you mess with the build.gradle, make sure to instead run

``gradlew clean build test``

to make sure the build isn't just using cache'd dependencies.
