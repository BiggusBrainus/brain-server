<div align="center">
    <h1>
        🧠
        <br />
        BigBrain
    </h1>
</div>

---

## Setup

For the server to work, the `db_access.properties` file from the [Trello board](https://trello.com/b/Ea9Urrpt/4dbigbrain) needs to be put into the `src/main/java/[...]/res/` directory.

Apart from that, no special setup is required, since *both* databases are hosted using [ElephantSQL](https://www.elephantsql.com/) and therefore don't run on the same machine as the *BigBrain* server.

## Run

Running the server is as easy as loading it into *IntelliJ* and starting it or executing something like these two commands:

```bash
mvn clean package && java -jar target/server-0.0.1-SNAPSHOT.jar
```

## Config

If, for some reason, you wanted to change the port the server is running on - e.g. because it is already in use - you simply need to change the port specified in the [src/main/resources/application.yml](src/main/resources/application.yml) file:

```yml
server:
  port: 8090
```
