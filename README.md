# Pokedex API
Modyo Challenge with java & spring-boot, by Pablo Damaso

### Running locally

By using gradle task, app will run in `server.port=8081`
```sh
$ ./gradlew bootRun
```
By using heroku cli, app will run in `server.port=5000`
```sh
$ ./gradlew build
$ heroku local web
```

### Accessing api-docs

```http request
GET http://localhost:{server.port}/swagger-ui/index.html
```

### API endpoints

#### Get pokemons 

To get pokemon's list, using pagination (offset=0, limit=20 are default values), use:
```http request
GET http://localhost:{server.port}/pokemon/?limit=20&offset=0
```
Please, follow HATEOAS `_links.prev.href` and `_links.next.href` links to navigate results by page.

#### Get pokemon
To get pokemon details, you can follow HATEOAS `content[*]._links.self.href` link, by resource. 

Or you can get pokemons detail specifying pokemon `name` in this enpoint
```http request
http://localhost:{server.port}/pokemon/{name}
```

### Deploying to cloud, using Heroku

```sh
$ heroku login
$ heroku create
$ git push heroku master
$ heroku open
```
