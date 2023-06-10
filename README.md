# kottbot
A useful Discord Bot for you!

## Installation
<details>
    <summary>Docker</summary>
1. Install Docker on your system (See https://docs.docker.com/engine/install/ubuntu/)
2. With docker-compose
    
```
    version: "3"
    services:
        kottbot:
            image: <image name>
            container_name: kottbot
            volumes:
                - ./:/home/kottbot/
```


2. With docker run
    
```docker run <image name> -v ./:/home/kottbot```
    
</details>

<details>
    <summary>Directly on the machine</summary>
    ## Steps
    - Download the latest release (or clone the repo and build from source).
    - Check if java is installed with `java --version`
    - Execute  `java -jar <jar file name>.jar` to start the Bot: 
    - Type `exit` to shutdown the bot!

    ## Run in the Background:
    - Install `screen`
    ==> Ubuntu: `apt install screen`

    - screen -AmdS kottbot java -jar kottbot.jar
    ==> You can also write it into a .sh file, if you need it more often :D
    `echo "screen -AmdS kottbot java -jar <kottbot file>.jar" >> start.sh`
    Run this in the terminal to generate your shell script.

    - See if your bot is already running with `screen -ls`

    - Attach to to terminal of your bot with `screen -r <name>`

</details>




## Features
- Fully functional Music-Bot
- Join-To-Create Channel

## TODO:
- [ ] Playlist-System
- [ ] Ticket-System
- [ ] Level-System

JDA: https://github.com/DV8FromTheWorld/JDA

LavaPlayer: https://github.com/sedmelluq/lavaplayer

SimplYaml: https://github.com/Carleslc/Simple-YAML

Inspiration: https://www.youtube.com/c/CoasterFreak-NL2
