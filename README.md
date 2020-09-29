# Coding challenge - Demo BANK API

Author: Martin Juiz

This project is a Spring-Boot microservice developed to simulate a banking API handling transactions.

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

This command starts the application and makes all the RESTFul endpoints available at [http://localhost:8080/](http://localhost:8080/)


## Assumptions

- No currency was specified: the microservice does not handle conversions / currency outputs.
- When the account balance is updated, the new balance is rounded up to 2 decimals.
- The application creates 3 accounts with positive balance just for testing purposes.
- When there is an incoming transaction, the transaction and the account balance update are handled into 2 separate transactions. 
Ideally, this should be handled in one single transaction but was done in this way to keep it simple and due to time constraints.


## API documentation

This API is self-documented using Swagger annotations.

Once the application is started, the Swagger UI is available at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


## Operations

### GET /accounts

Lists all the available accounts.

Request : `GET http://localhost:8080/accounts`

Response body:
```json
[
    {
        "id": "0",
        "name": "Sample account 1",
        "balance": 100.2,
        "account_iban": "ES9820385778983000760236"
    },
    {
        "id": "1",
        "name": "Sample account 2",
        "balance": 1500.11,
        "account_iban": "ES0420805237723522472951"
    },
    {
        "id": "2",
        "name": "Sample account 3",
        "balance": 6500.0,
        "account_iban": "ES1314656632903771443652"
    }
]
```


### GET /transactions?reference={reference}&channel={channel}

Fetches a single transaction given a valid reference and channel.

Request : `GET http://localhost:8080/transactions?reference=Q8PQABUHYX&channel=INTERNAL`

Response body:
```json
{
    "reference": "Q8PQABUHYX",
    "status": "PENDING",
    "amount": -10.01,
    "fee": 1.01
}
```

### GET /transactions/searching/?account_iban={iban}&sort={sortBy}

List all the transactions for a specific account and sort them by amount in ascending or descending order.

Request : `GET http://localhost:8080/transactions/searching/?account_iban=ES9820385778983000760236&sort=amount:asc`

Response body:
```json
{
    "transactions": [
        {
            "reference": "Q8PQABUHYX",
            "iban": "ES9820385778983000760236",
            "date": "2020-09-29T11:50:36.872Z",
            "amount": -10.01,
            "fee": 1.01,
            "description": "Card payment"
        },
        {
            "reference": "3O22YR71EA",
            "iban": "ES9820385778983000760236",
            "date": "2020-09-29T13:41:02.425Z",
            "amount": 1.01,
            "fee": 1.01,
            "description": "Card payment"
        },
        {
            "reference": "CIOWPV1OE4",
            "iban": "ES9820385778983000760236",
            "date": "2020-09-29T13:41:07.838Z",
            "amount": 18.01,
            "fee": 1.01,
            "description": "Card payment"
        }
    ]
}
```

### POST /transactions

Creates a new transaction and returns the transaction reference (please check all the considerations listed below):

- Request without `reference` value will make the backend to generate one.
- Request with `reference` value has to have a length between 1 and 10 chars.
- Request without `date` value will make the backend to fill the current date.
- Request with `date` value must use the pattern yyyy-MM-dd'T'HH:mm:ss.SSS'Z'.

Response body:
```json
{
    "reference": "CIOWPV1OE4"
}
```


## Testing the API

ATDD was used as the strategy to develop the entire application. Each layer has specific test cases to prove 
all scenarios were covered.

To run all the tests, run this command from the project root folder:

```bash
mvn test
```


The project also comes with an out-of-the-box Postman collection with all the RESTFul API calls ready to test.

It can be found in the project root under **/postman** folder.