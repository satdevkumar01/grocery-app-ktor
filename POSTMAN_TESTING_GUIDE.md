# Grocery App API Testing Guide with Postman

This guide will help you test all the API endpoints of the Grocery App using Postman.

## Table of Contents
1. [Setting Up Postman](#setting-up-postman)
2. [Authentication APIs](#authentication-apis)
   - [User Registration](#user-registration)
   - [User Login](#user-login)
   - [Forgot Password](#forgot-password)
   - [Reset Password](#reset-password)
   - [Update User](#update-user)
3. [Product APIs](#product-apis)
   - [Get All Products](#get-all-products)
   - [Add a Product](#add-a-product)
   - [Update a Product](#update-a-product)
   - [Delete a Product](#delete-a-product)
4. [Testing Flow](#testing-flow)

## Setting Up Postman

1. **Download and Install Postman**:
   - If you haven't already, download Postman from [https://www.postman.com/downloads/](https://www.postman.com/downloads/)
   - Install and open the application

2. **Import the Collection**:
   - In this repository, you'll find a file named `Grocery_App_API.postman_collection.json`
   - In Postman, click on "Import" in the top left corner
   - Drag and drop the collection file or browse to select it
   - Click "Import" to add the collection to Postman

3. **Alternative: Create a New Collection Manually**:
   - If you prefer to create the collection manually, click on "Collections" in the sidebar
   - Click the "+" button to create a new collection
   - Name it "Grocery App API"
   - Add the requests as described in this guide

4. **Set Base URL**:
   - The API is running on `http://localhost:8080`
   - The imported collection already includes this as a variable
   - If you created the collection manually, you can create an environment variable for the base URL to make it easier to change later

## Authentication APIs

### User Registration

**Endpoint**: `POST /auth/register`

**Request Body**:
```json
{
    "email": "user@example.com",
    "password": "securepassword",
    "name": "John Doe",
    "phone": "1234567890"
}
```

**Steps**:
1. Create a new POST request in Postman
2. Set the URL to `http://localhost:8080/auth/register`
3. Go to the "Body" tab, select "raw" and "JSON"
4. Enter the JSON request body as shown above
5. Click "Send"

**Expected Response**:
- Status Code: 201 Created
- Body: User object (without password)
```json
{
    "id": 1,
    "email": "user@example.com",
    "password": "",
    "name": "John Doe",
    "phone": "1234567890",
    "resetToken": null,
    "resetTokenExpiry": null
}
```

**Possible Error Responses**:
- Status Code: 409 Conflict - If the email is already registered
- Status Code: 400 Bad Request - If the request body is invalid

### User Login

**Endpoint**: `POST /auth/login`

**Request Body**:
```json
{
    "email": "user@example.com",
    "password": "securepassword"
}
```

**Steps**:
1. Create a new POST request in Postman
2. Set the URL to `http://localhost:8080/auth/login`
3. Go to the "Body" tab, select "raw" and "JSON"
4. Enter the JSON request body as shown above
5. Click "Send"

**Expected Response**:
- Status Code: 200 OK
- Body: Token and user object
```json
{
    "token": "user_1_123456",
    "user": {
        "id": 1,
        "email": "user@example.com",
        "password": "",
        "name": "John Doe",
        "phone": "1234567890",
        "resetToken": null,
        "resetTokenExpiry": null
    }
}
```

**Possible Error Responses**:
- Status Code: 401 Unauthorized - If the credentials are invalid

### Forgot Password

**Endpoint**: `POST /auth/forgot-password`

**Request Body**:
```json
{
    "email": "user@example.com"
}
```

**Steps**:
1. Create a new POST request in Postman
2. Set the URL to `http://localhost:8080/auth/forgot-password`
3. Go to the "Body" tab, select "raw" and "JSON"
4. Enter the JSON request body as shown above
5. Click "Send"

**Expected Response**:
- Status Code: 200 OK
- Body: Message with reset token (in a real app, this would be sent via email)
```
"If your email is registered, you will receive a password reset link. Token: 123e4567-e89b-12d3-a456-426614174000"
```

**Note**: For security reasons, the API returns the same message whether the email exists or not. In this test implementation, the token is returned in the response for testing purposes.

### Reset Password

**Endpoint**: `POST /auth/reset-password`

**Request Body**:
```json
{
    "token": "123e4567-e89b-12d3-a456-426614174000",
    "newPassword": "newSecurePassword"
}
```

**Steps**:
1. Create a new POST request in Postman
2. Set the URL to `http://localhost:8080/auth/reset-password`
3. Go to the "Body" tab, select "raw" and "JSON"
4. Enter the JSON request body as shown above (use the token received from the forgot-password endpoint)
5. Click "Send"

**Expected Response**:
- Status Code: 200 OK
- Body: Success message
```
"Password has been reset successfully"
```

**Possible Error Responses**:
- Status Code: 400 Bad Request - If the token is invalid or expired

### Update User

**Endpoint**: `PUT /auth/user`

**Headers**:
- `X-User-ID`: The ID of the user (obtained from login response)

**Request Body**:
```json
{
    "name": "Updated Name",
    "phone": "9876543210",
    "email": "updated@example.com"
}
```

**Steps**:
1. Create a new PUT request in Postman
2. Set the URL to `http://localhost:8080/auth/user`
3. Go to the "Headers" tab and add `X-User-ID` with the value of the user ID
4. Go to the "Body" tab, select "raw" and "JSON"
5. Enter the JSON request body as shown above
6. Click "Send"

**Expected Response**:
- Status Code: 200 OK
- Body: Updated user object
```json
{
    "id": 1,
    "email": "updated@example.com",
    "password": "",
    "name": "Updated Name",
    "phone": "9876543210",
    "resetToken": null,
    "resetTokenExpiry": null
}
```

**Possible Error Responses**:
- Status Code: 401 Unauthorized - If the X-User-ID header is missing
- Status Code: 404 Not Found - If the user doesn't exist

## Product APIs

### Get All Products

**Endpoint**: `GET /products`

**Steps**:
1. Create a new GET request in Postman
2. Set the URL to `http://localhost:8080/products`
3. Click "Send"

**Expected Response**:
- Status Code: 200 OK
- Body: Array of product objects
```json
[
    {
        "id": 1,
        "name": "Apple",
        "price": 1.99,
        "quantity": 100
    },
    {
        "id": 2,
        "name": "Banana",
        "price": 0.99,
        "quantity": 150
    }
]
```

**Possible Response if No Products**:
```
"There is no Items at now please try after some time s"
```

### Add a Product

**Endpoint**: `POST /addproducts`

**Request Body**:
```json
{
    "name": "Orange",
    "price": 1.49,
    "quantity": 75
}
```

**Steps**:
1. Create a new POST request in Postman
2. Set the URL to `http://localhost:8080/addproducts`
3. Go to the "Body" tab, select "raw" and "JSON"
4. Enter the JSON request body as shown above
5. Click "Send"

**Expected Response**:
- Status Code: 201 Created
- Body: Message with new product ID
```json
{
    "new product is created id": 3
}
```

### Update a Product

**Endpoint**: `PUT /products/{id}`

**URL Parameters**:
- `id`: The ID of the product to update

**Request Body**:
```json
{
    "name": "Green Apple",
    "price": 2.49,
    "quantity": 50
}
```

**Steps**:
1. Create a new PUT request in Postman
2. Set the URL to `http://localhost:8080/products/1` (replace 1 with the actual product ID)
3. Go to the "Body" tab, select "raw" and "JSON"
4. Enter the JSON request body as shown above
5. Click "Send"

**Expected Response**:
- Status Code: 200 OK
- Body: Success message
```
"Product updated successfully"
```

**Possible Error Responses**:
- Status Code: 404 Not Found - If the product doesn't exist
- Status Code: 400 Bad Request - If the ID is invalid

### Delete a Product

**Endpoint**: `DELETE /products/{id}`

**URL Parameters**:
- `id`: The ID of the product to delete

**Steps**:
1. Create a new DELETE request in Postman
2. Set the URL to `http://localhost:8080/products/1` (replace 1 with the actual product ID)
3. Click "Send"

**Expected Response**:
- Status Code: 200 OK
- Body: Success message
```
"Product deleted successfully"
```

**Possible Error Responses**:
- Status Code: 404 Not Found - If the product doesn't exist
- Status Code: 400 Bad Request - If the ID is invalid

## Testing Flow

For a complete testing flow, follow these steps:

1. **Register a new user**
   - Use the User Registration API
   - Save the user ID from the response

2. **Login with the new user**
   - Use the User Login API with the registered email and password
   - Save the token and user ID from the response

3. **Update the user profile**
   - Use the Update User API with the user ID in the X-User-ID header

4. **Test the forgot password flow**
   - Use the Forgot Password API to get a reset token
   - Use the Reset Password API with the token to set a new password
   - Login again with the new password to verify it works

5. **Add a new product**
   - Use the Add a Product API
   - Save the product ID from the response

6. **Get all products**
   - Use the Get All Products API to verify the product was added

7. **Update the product**
   - Use the Update a Product API with the product ID

8. **Delete the product**
   - Use the Delete a Product API with the product ID

9. **Verify the product was deleted**
   - Use the Get All Products API again
