# Project 0: RESTful API

## Technology Used
- Java 1.8
- Javalin
- JDBC
- Logback / SLF4J
- JUnit
- Mockito
- MariaDB

## Client Endpoints
- `POST /clients`: Creates a new client
- `GET /clients`: Gets all clients
- `GET /clients/{id}`: Get client with an id of X (if the client exists)
- `PUT /clients/{id}`: Update client with an id of X (if the client exists)
- `DELETE /clients/{id}`: Delete client with an id of X (if the client exists)

#### Related Fields
- `id` *integer*
    -  table primary key
    - generated on creation
- `fName` *varchar* **required**
    - first name field
    - 255 character limit
- `lName` *varchar* **required**
    - last name field
    - 255 charactrer limit
- `joined` *datetime*
    - date time field
    - generated on creation

## Account Endpoints

- `POST /clients/{client_id}/accounts`: Create a new account for a client with id of X (if client exists)
- `GET /clients/{client_id}/accounts`: Get all accounts for client with id of X (if client exists)
- `GET /clients/{client_id}/accounts?amountLessThan=2000&amountGreaterThan=400`: Get all accounts for client id of X with balances between 400 and 2000 (if client exists)
- `GET /clients/{client_id}/accounts/{account_id}`: Get account with id of Y belonging to client with id of X (if client and account exist AND if account belongs to client)
- `PUT /clients/{client_id}/accounts/{account_id}`: Update account with id of Y belonging to client with id of X (if client and account exist AND if account belongs to client)
- `DELETE /clients/{client_id}/accounts/{account_id}`: Delete account with id of Y belonging to client with id of X (if client and account exist AND if account belongs to client)

#### Related Fields
- `id` *integer*
    -  table primary key
    - generated on creation
- `fk` *integer*
    - foreign key
    - relates account to client table
- `type` *varchar* **required**
    - 255 character limit
-  `balance` *decimal* **required**
    - value associated with account
    - four digits of precision
- `acc_num` *integer* **required**
    - immutable
    - uniquely identifies account
- `created` *datetime*
    - date time field
    - generated on creation

## Design Layers
- Controller Layer
    - Handles user input from routes
    - Designates action depending on HTTP verb
    - Serialises JSON response
    - Responds with appropriate HTTP status code
        - Exception Layer
            - Handles errors and replies with a meaningful error message
            - Responds with appropriate HTTP error code
- Object Transfer Layer
    - Serialises incoming user input data
- Service Layer
    - Validates data before data layer
- Data Layer
    - Performs database transactions
