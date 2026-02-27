# olympp_bank_kata
A basic "fake bank" project based on Olympp's KATA.

Backend : java 25 + Spring  
Frontend : quick and dirty HTML file for testing purposes (courtesy of my good friend ClaudeAI).

Deploys on http://localhost:8080/

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
