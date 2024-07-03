# GS-Budgets Backend

Questo è il mio Capstone Project per il Bootcamp Epicode, classe FS0124A.

È il Back-End dell'app Front-End, scritta in Angular, che puoi trovare a questo link: [**CapstoneProjectFE**](https://github.com/GabScognamiglio/CapstoneProjectFE) 

GS-Budgets è un'applicazione di gestione delle spese e delle entrate progettata per aiutarti a tenere traccia delle tue finanze personali in modo semplice ed efficace.

## Installazione e utilizzo

1. Clonare il repository
2. Creare un database con PostgreSQL (usando opzionalmente PG Admin) col nome "gs-budgets"
3. Creare un file `env.properties` dove modificare le variabili di ambiente che trovi nel file `application.properties`
4. Avviare `GsBudgetsApplication.java`. L'app sarà disponibile su `http://localhost:8080`

## Tecnologie utilizzate

![Logo](https://skillicons.dev/icons?i=java,postgres,maven,spring,hibernate)

- Java 22
- Spring Boot
- Spring Data JPA
- Spring Security
- PostgreSQL
- Maven

## API Endpoints principali

### Autenticazione (JWT)

- `POST /auth/signup` - registrazione nuovo utente
- `POST /auth/login` - login utente registrato

### Spese ed Entrate

- `GET` `POST` `PUT` `DELETE` `api/gs-budgets/expenses` - lettura, creazione, modifica e eliminazione delle spese
- `GET` `POST` `PUT` `DELETE` `api/gs-budgets/incomes` - lettura, creazione, modifica e eliminazione delle entrate

### Conto

- `GET` `api/gs-budgets/accounts` - diversi endpoint per ricevere bilanci e statistiche del conto, mensili, annuali e totali

### Obiettivi di spesa e tickets di supporto

- `GET` `POST` `PUT` `DELETE` `api/gs-budgets/saving-goals` - lettura, creazione, modifica e eliminazione degli obiettivi di risparmio dell'utente
- `GET` `POST` `PUT` `DELETE` `api/gs-budgets/tickets` - lettura, creazione e eliminazione dei ticket di richiesta supporto dell'utente, al quale un'altro utente di ruolo ADMIN può rispondere

### Autore

- [**Gabriele Scognamiglio**](https://github.com/GabScognamiglio) 

