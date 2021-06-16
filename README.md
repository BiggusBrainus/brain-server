<div align="center">
    <h1>
        🧠
        <br />
        BigBrain
    </h1>
    <p>
      <img alt="GitHub Workflow Status" src="https://img.shields.io/github/workflow/status/BiggusBrainus/brain-server/Java%20CI%20with%20Maven?style=for-the-badge">
      <img alt="GitHub repo size" src="https://img.shields.io/github/repo-size/BiggusBrainus/brain-server?style=for-the-badge">
      <img alt="GitHub last commit" src="https://img.shields.io/github/last-commit/BiggusBrainus/brain-server?style=for-the-badge">
    </p>
</div>

---

## Setup

For the server to work, the `db_access.properties` file from the [Trello board](https://trello.com/c/11hcpalx/38-%F0%9F%93%88-datenbank-zugangsdaten) needs to be put into the `src/main/java/[...]/res/` directory.

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

## Endpoints

In case you want to develop your own *BigBrain* client or you just want to see, what the HTTP endpoints are like, you can check out the API documentation exported from Postman @ [documenter.getpostman.com](https://documenter.getpostman.com/view/10746759/TzeUo8qT).
