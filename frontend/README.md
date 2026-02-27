# Bank Angular App - Demo Frontend

This is an Angular frontend application for the Bank Demo API. It demonstrates the ability to work with Angular, TypeScript, RxJS, and REST APIs.

## Prerequisites

Before running this application, make sure you have the following installed:

- **Node.js** (version 18 or higher) - [Download here](https://nodejs.org/)
- **npm** (comes with Node.js)
- **Angular CLI** (will be installed as part of setup)

## Project Structure

```
bank-angular-app/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   └── account/          # Main account component
│   │   ├── models/
│   │   │   └── account.model.ts  # TypeScript interfaces and types
│   │   ├── services/
│   │   │   └── account.service.ts # HTTP service for API calls
│   │   ├── app.component.*       # Root component
│   │   └── app.module.ts         # Main module
│   ├── index.html
│   ├── main.ts
│   └── styles.css
├── angular.json                   # Angular configuration
├── package.json                   # Dependencies
└── tsconfig.json                  # TypeScript configuration
```

## Setup Instructions

### 1. Install Dependencies

Open a terminal in the `bank-angular-app` directory and run:

```bash
npm install
```

This will install all required dependencies including Angular, RxJS, and TypeScript. It may take a few minutes.

### 2. Verify Backend is Running

Make sure your Spring Boot backend is running on `http://localhost:8080`.

The backend should have the `/api/account` endpoints available with CORS enabled.

### 3. Run the Application

Start the Angular development server:

```bash
npm start
```

Or alternatively:

```bash
npx ng serve --port 4200
```

The application will be available at: **http://localhost:4200**

## Features

### Implemented Features:
- ✅ Display account information (name, balance)
- ✅ Warning message when balance is negative
- ✅ Deposit functionality with amount validation
- ✅ Withdrawal functionality with amount validation
- ✅ Transaction history viewer (toggle on/off)
- ✅ Proper error handling for all backend exceptions:
  - INSUFFICIENT_FUNDS
  - INVALID_AMOUNT
  - ACCOUNT_NOT_FOUND
  - Generic errors
- ✅ Auto-refresh balance after transactions
- ✅ Success/error messages with auto-dismiss
- ✅ TypeScript type safety throughout
- ✅ Reactive programming with RxJS Observables
- ✅ Clean component architecture
- ✅ Responsive design

### Angular Concepts Demonstrated:
- **Components**: Modular UI components with clear separation of concerns
- **Services**: HTTP service using HttpClient for API communication
- **Dependency Injection**: Service injection into components
- **RxJS**: Observables for async operations
- **Two-way data binding**: Using `[(ngModel)]` for form inputs
- **Event binding**: Click and keyup events
- **Property binding**: Dynamic CSS classes and attributes
- **Structural directives**: `*ngIf`, `*ngFor`
- **TypeScript**: Strong typing with interfaces and enums
- **Error handling**: Proper error handling with HttpErrorResponse

## API Endpoints Used

The application connects to these Spring Boot endpoints:

- `GET /api/account/1` - Get account information
- `POST /api/account/1/deposit` - Make a deposit
- `POST /api/account/1/withdraw` - Make a withdrawal
- `GET /api/account/1/statement` - Get transaction history

## Development Notes

### Key Files to Review:

1. **account.service.ts** - Demonstrates HTTP service pattern, RxJS operators, error handling
2. **account.component.ts** - Shows component logic, state management, lifecycle hooks
3. **account.model.ts** - TypeScript interfaces matching Java DTOs
4. **app.module.ts** - Shows module structure and dependency imports

### Why This Approach?

This implementation follows Angular best practices:
- **Separation of concerns**: Services handle API calls, components handle UI logic
- **Type safety**: TypeScript interfaces prevent runtime errors
- **Reactive patterns**: RxJS Observables for async operations
- **Clean architecture**: Easy to test and extend
- **Production-ready structure**: Organized like a real Angular application

## Troubleshooting

### CORS Issues
If you see CORS errors in the console, make sure your Spring Boot controller has:
```java
@CrossOrigin(origins = "*")
```

### Backend Not Running
If you see "Failed to load account information", verify that:
1. Spring Boot is running on port 8080
2. The `/api/account` endpoints are accessible
3. Account with ID 1 exists in your database

### Port Already in Use
If port 4200 is already in use, you can run on a different port:
```bash
ng serve --port 4201
```

## Building for Production

To create a production build:

```bash
npm run build
```

The build artifacts will be stored in the `dist/` directory.

## Testing the Application

1. Start your Spring Boot backend
2. Run this Angular app with `npm start`
3. Navigate to http://localhost:4200
4. You should see:
   - Welcome message with account holder name
   - Current balance
   - Deposit and withdrawal forms
   - Button to view transaction history

## Next Steps / Potential Enhancements

If this were a real project, consider adding:
- Unit tests (Jasmine/Karma)
- E2E tests (Protractor/Cypress)
- Route guards and authentication
- Multiple account views with routing
- Form validation with reactive forms
- Loading indicators during API calls
- Internationalization (i18n)
- More sophisticated error handling
- Environment configuration for different API URLs

## Notes for Interview

This implementation demonstrates:
- Understanding of Angular architecture and best practices
- TypeScript proficiency
- RxJS and reactive programming
- HTTP client usage and error handling
- Component-based architecture
- Service-oriented design
- Proper separation of concerns

The code is deliberately kept simple and clean for a demo, but follows patterns that scale to production applications.