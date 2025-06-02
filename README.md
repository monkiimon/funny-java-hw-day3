# Person and Loan Management API

## Features
- Manage persons (CRUD operations)
- Manage loans (CRUD operations)
- Get a person's details along with their loans
- Many-to-One relationship between Loan and Person

## API Endpoints

### Person Endpoints
- `GET /api/persons` - Get all persons
- `GET /api/persons/{personId}` - Get a person by ID
- `POST /api/persons` - Create a new person
- `PUT /api/persons/{personId}` - Update a person
- `DELETE /api/persons/{personId}` - Delete a person
- `GET /api/persons/{personId}/loans` - Get a person with their loans

### Loan Endpoints
- `GET /api/loans` - Get all loans
- `GET /api/loans/{loanId}` - Get a loan by ID
- `POST /api/loans` - Create a new loan

----------

## Homework
The `Loan` table has a `person_id` column that forms a Many-to-One relationship with the `Person` table.
This allows querying all loans belonging to a specific person via `/api/persons/{personId}/loans`

#### Add to loan table:
```sql
CREATE TABLE IF NOT EXISTS loan (
    ...
    person_id VARCHAR(50) NOT NULL,
    CONSTRAINT fk_loan_person FOREIGN KEY (person_id) REFERENCES person(person_id)
);
```


## Example Requests/Responses

### 1. Get Person with Loans
**Request:**
```http
GET /api/persons/P001/loans
```
**Response:**
```
{
    "personId": "P001",
    "name": "John Doe",
    "loans": [
        {
            "loanId": "L001",
            "amount": 10000,
            "interestRate": 5.5,
            "tenure": 12
        },
        {
            "loanId": "L002",
            "amount": 20000,
            "interestRate": 6.0,
            "tenure": 24
        }
    ]
}
```
