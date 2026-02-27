# olympp_bank_kata
A basic "fake bank" project based on Olympp's KATA.

Project includes a docker config for convenience. Deploy using "docker compose up --build" at the root.

----------------------------------------------

Backend : Java 25 + Spring  
Frontend : Angular 17

Disclaimer: I DID NOT MAKE THE FRONTEND. It is 100% courtesy of my good friend ClaudeAI and meant for convenient testing purposes.  
You can also test the endpoints manually through curl or Postman.  
If you do not want to bother with the Angular frontend (and its heavy dependencies), I recommend checking out release tag v1.0, which only contains the backend and a simple html file for a test frontpage.

Deploys backend on http://localhost:8080/  
Frontend on http://localhost:4200/

Exposes four endpoints :

/api/account/{accountId} on GET

/api/account/{accountId}/deposit on POST  
    (expects " {"amount": 100.00} " JSON body)

/api/account/{accountId}/withdraw on POST  
    (expects " {"amount": 100.00} " JSON body)

/api/account/{accountId}/statement on GET


Database is per-session in-memory managed with H2.  
Data is initialized with one single account with accountId = 1.

Database can be consulted for debug purposes at http://localhost:8080/h2-console  
JDBC URL : jdbc:h2:mem:testdb  
user name : sa  
password : (leave blank)  


Some (*some*) unit tests have been implemented for demonstration purposes. Test coverage is limited.  
Same thing for error management. 


Non-exhaustive list of improvable points :  

More test coverage, and more test types (integration testing, etc)  
Robust Logger system for error monitoring  
Authentication and security  
Validate input data through @Valid  
Generate an API doc through springdoc-openapi  
Proper database  
CI/CD  
Reconquer Constantinople and restore the empire for the glory of Rome.  
Other...
