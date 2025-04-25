# API Testing with Postman

This repository includes resources to help you test the Grocery App API endpoints using Postman.

## What's Included

1. **Comprehensive Testing Guide**: 
   - `POSTMAN_TESTING_GUIDE.md` - A detailed guide explaining how to test each API endpoint, with request formats, expected responses, and a recommended testing flow.

2. **Ready-to-Use Postman Collection**:
   - `Grocery_App_API.postman_collection.json` - A Postman collection file that you can import directly into Postman to quickly start testing the APIs.

## API Endpoints Overview

### Authentication APIs
- **Register**: `POST /auth/register` - Create a new user account
- **Login**: `POST /auth/login` - Authenticate and get a token
- **Forgot Password**: `POST /auth/forgot-password` - Request a password reset token
- **Reset Password**: `POST /auth/reset-password` - Reset password using a token
- **Update User**: `PUT /auth/user` - Update user profile information

### Product APIs
- **Get All Products**: `GET /products` - Retrieve all products
- **Add Product**: `POST /addproducts` - Add a new product
- **Update Product**: `PUT /products/{id}` - Update an existing product
- **Delete Product**: `DELETE /products/{id}` - Delete a product

## Getting Started

1. Make sure the Grocery App server is running on `http://localhost:8080`
2. Import the Postman collection as described in the testing guide
3. Follow the testing flow in the guide to test all endpoints

## Notes

- The server uses an H2 in-memory database, so data will be reset when the server restarts
- For the Update User API, you need to include the user ID in the `X-User-ID` header
- The Forgot Password API returns the reset token in the response for testing purposes (in a real application, this would be sent via email)

## Testing Flow

For the most effective testing, follow this sequence:

1. Register a new user
2. Login with the new user credentials
3. Update the user profile
4. Test the password reset flow
5. Add, update, and delete products

Refer to the detailed testing guide for specific instructions on each step.