# Coding challenge - Demo BANK API

Author: Martin Juiz

This API is a technical test developed to simulate a banking API handling transactions.

Please read below the installation notes before running the project. 

## Installation

This project was built using the following tools (therefore all of them are required in order to make it working):

[Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

Please verify you have a valid JDK installed in your computer by running the command shown below. Otherwise, you can
download it from the link provided above.

```bash
java -version
```


[Apache Maven 3.5.2](https://maven.apache.org/download.cgi)

Please verify you have a valid Maven installation in your computer by running the command shown below. Otherwise, you can
download it from the link provided above.

```bash
mvn -version
```

## Environment configuration

This API comes with an out-of-the-box, in-memory database implementation so no environment configurations are required.


## Starting the application

Once you have validated all the pre-requisites, just run this command from the root folder (where the pom.xml file is located):

```bash
mvn spring-boot:run
```

This command starts the application and makes all the RESTFul endpoints available at [https://localhost:8080/marvel](https://localhost:8080/marvel)


## API documentation

This API is documented using Swagger libraries.

Once the application is started, the Swagger UI is available at [http://localhost:8080/marvel/swagger-ui.html](http://localhost:8080/marvel/swagger-ui.html)


## Operations

### GET /transactions

Fetches lists of comic characters IDs and keep data cached to serve it quicker once the first call finishes correctly.

Request : `GET http://localhost:8080/marvel/characters`

Response body:
```json
[
    1011334,
    1017100,
    1009144,
    1010699,
    ...
]
```


### GET /transactions/search/{account_iban}?sort={amount}

Fetches a single character resource. It is the canonical URI for any character resource provided by the API.

Request : `GET http://localhost:8080/marvel/characters/1009233`

Response body:
```json
{
    "id": 1009233,
    "name": "Chamber",
    "description": "",
    "thumbnail": {
        "path": "http://i.annihil.us/u/prod/marvel/i/mg/2/80/4c00406e4731b",
        "extension": "jpg"
    }
}
```

### POST /transactions

Fetches a single character resource plus the specific powers for the character

Optionally, it allows a query parameter so the character's powers can be translated to the specific
language code (the format must be compliant with [ISO-639-1](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) standardized nomenclature).

Request without `languageCode` parameter : `GET http://localhost:8080/marvel/characters/1009430/powers`

Response body:
```json
{
    "id": 1009430,
    "name": "Rachel Grey",
    "description": "",
    "thumbnail": {
        "path": "http://i.annihil.us/u/prod/marvel/i/mg/d/10/52741143108e7",
        "extension": "jpg"
    },
    "powers": [
        {
            "power": "durability",
            "rating": "4"
        },
        {
            "power": "energy",
            "rating": "7"
        },
        {
            "power": "fighting skills",
            "rating": "4"
        },
        {
            "power": "intelligence",
            "rating": "3"
        },
        {
            "power": "speed",
            "rating": "6"
        },
        {
            "power": "strength",
            "rating": "3"
        }
    ]
}
```


Request with `languageCode` query parameter and no powers array returned : `GET -H 'Authorization: accessToken' http://localhost:8080/marvel/characters/1009688/powers`

Before submitting the request, you'll need to generate an access token. To do that, please open a new terminal session and run this command:

```bash
gcloud auth application-default print-access-token
```

Copy the value generated and paste into the `Authorization` header value.

Response body:
```json
{
    "id": 1009688,
    "name": "Carmella Unuscione",
    "description": "Carmella Unuscione was one of the first members of the Magneto's Acolytes.",
    "thumbnail": {
        "path": "http://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available",
        "extension": "jpg"
    },
    "powers": []
}
```


Request with `languageCode` query parameter and character's powers translated : `GET -H 'Authorization: accessToken' http://localhost:8080/marvel/characters/1009233/powers?language=es`

Before submitting the request, you'll need to generate an access token. To do that, please open a new terminal session and run this command:

```bash
gcloud auth application-default print-access-token
```

Copy the value generated and paste into the `Authorization` header value.

Response body:
```json
{
    "id": 1009233,
    "name": "Chamber",
    "description": "",
    "thumbnail": {
        "path": "http://i.annihil.us/u/prod/marvel/i/mg/2/80/4c00406e4731b",
        "extension": "jpg"
    },
    "powers": [
        {
            "power": "inteligencia",
            "rating": "3"
        },
        {
            "power": "fuerza",
            "rating": "2"
        },
        {
            "power": "durabilidad",
            "rating": "6"
        },
        {
            "power": "energía",
            "rating": "4"
        },
        {
            "power": "velocidad",
            "rating": "2"
        },
        {
            "power": "habilidades de lucha",
            "rating": "4"
        }
    ]
}
```

Notes:

1. The parameter `languageCode` follows the format defined by ISO-639-1
2. Any `languageCode` not available in this [list](https://cloud.google.com/translate/docs/languages) will throw an exception.
3. The source language is always **'en'** (English) as Marvel Wiki is written in this language.
4. As per described in the previous point, the target language cannot be **'en'**: if you send this value, the API will throw an exception.


## Testing the API

TDD was used as the strategy to develop the entire Spring Boot project. Each service / controller has a specific test class
and different unit test cases to prove all scenarios were covered.

To run all the tests, run this command from the project root folder:

```bash
mvn test
```


The project also comes with an out-of-the-box Postman collection with all the RESTFul API calls ready to test.

It can be found in the project root under **/postman** folder.