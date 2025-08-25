# Music Royalties Collection System

A comprehensive Spring Boot application for managing music royalties collection from radio and TV channels. The system handles artist registration, profile verification, music file uploads, and company logsheet management.

## Features

- **User Management**: Artist, Company, and Admin roles
- **Artist Profile Management**: Complete profile creation with document uploads
- **File Upload System**: Support for images (passport photos) and PDFs (documents)
- **Music Management**: MP3 and video file uploads with approval workflow
- **Email Notifications**: Automated emails for profile and music approval/rejection
- **Company Logsheets**: Music selection and tracking for companies
- **JWT Authentication**: Secure API access with role-based permissions

## Technology Stack

- **Backend**: Spring Boot 3.2.0
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT
- **File Storage**: Local file system
- **Email**: Gmail SMTP
- **Build Tool**: Maven
- **Java Version**: 17

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Gmail account with App Password

## Setup Instructions

### 1. Database Setup

1. Create a PostgreSQL database:
```sql
CREATE DATABASE music_royalties_db;
```

2. Update `src/main/resources/application.properties` with your database credentials:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 2. Email Configuration

1. Enable 2-Factor Authentication on your Gmail account
2. Generate an App Password
3. Update `src/main/resources/application.properties`:
```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

### 3. JWT Configuration

Update the JWT secret in `application.properties`:
```properties
jwt.secret=your-very-long-and-secure-secret-key-here
```

### 4. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Documentation

### Authentication Endpoints

#### 1. Artist Registration
```
POST /api/auth/register/artist
Content-Type: application/json

{
    "email": "artist@example.com",
    "password": "password123"
}
```

#### 2. Company Registration (Admin Only)
```
POST /api/auth/register/company
Content-Type: application/json

{
    "email": "company@example.com",
    "password": "password123"
}
```

#### 3. Login
```
POST /api/auth/login
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123"
}
```

#### 4. Email Verification
```
GET /api/auth/verify?token={verification_token}
```

### Artist Endpoints

#### 1. Create Profile
```
POST /api/artist/profile
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
    "firstName": "John",
    "surname": "Doe",
    "phoneNumber": "+1234567890",
    "email": "john@example.com",
    "nationality": "American",
    "occupation": "Musician",
    "accountHolderName": "John Doe",
    "bankAccountNumber": "1234567890"
}
```

#### 2. Upload Passport Photo
```
POST /api/artist/passport-photo
Authorization: Bearer {jwt_token}
Content-Type: multipart/form-data

file: [image file]
imageTitle: "Passport Photo"
```

#### 3. Upload Proof of Payment
```
POST /api/artist/proof-of-payment
Authorization: Bearer {jwt_token}
Content-Type: multipart/form-data

file: [PDF file]
documentTitle: "Proof of Payment"
```

#### 4. Upload Bank Confirmation Letter
```
POST /api/artist/bank-confirmation-letter
Authorization: Bearer {jwt_token}
Content-Type: multipart/form-data

file: [PDF file]
documentTitle: "Bank Confirmation Letter"
```

#### 5. Upload ID Document
```
POST /api/artist/id-document
Authorization: Bearer {jwt_token}
Content-Type: multipart/form-data

file: [PDF file]
documentTitle: "ID Document"
```

#### 6. Upload Music
```
POST /api/artist/music/upload
Authorization: Bearer {jwt_token}
Content-Type: multipart/form-data

file: [audio/video file]
title: "Song Title"
```

#### 7. Get My Music
```
GET /api/artist/music
Authorization: Bearer {jwt_token}
```

#### 8. Get My Documents
```
GET /api/artist/documents
Authorization: Bearer {jwt_token}
```

### Admin Endpoints

#### 1. Get Pending Profiles
```
GET /api/admin/pending-profiles
Authorization: Bearer {jwt_token}
```

#### 2. Approve Profile
```
POST /api/admin/profile/approve/{memberId}
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
    "ipiNumber": "IPI123456789"
}
```

#### 3. Reject Profile
```
POST /api/admin/profile/reject/{memberId}
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
    "notes": "Missing required documents"
}
```

#### 4. Get Pending Music
```
GET /api/admin/pending-music
Authorization: Bearer {jwt_token}
```

#### 5. Approve Music
```
POST /api/admin/music/approve/{musicId}
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
    "isrcCode": "ISRC123456789"
}
```

#### 6. Reject Music
```
POST /api/admin/music/reject/{musicId}
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
    "notes": "Audio quality too low"
}
```

#### 7. Create Company
```
POST /api/admin/company/create
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
    "email": "company@example.com",
    "password": "password123",
    "companyName": "Radio Station XYZ",
    "companyAddress": "123 Main St, City",
    "companyPhone": "+1234567890",
    "contactPerson": "John Manager"
}
```

### Company Endpoints

#### 1. Get Company Profile
```
GET /api/company/profile
Authorization: Bearer {jwt_token}
```

#### 2. Get Approved Music
```
GET /api/company/approved-music
Authorization: Bearer {jwt_token}
```

#### 3. Create LogSheet
```
POST /api/company/logsheet
Authorization: Bearer {jwt_token}
Content-Type: application/json

{
    "title": "Morning Show Playlist",
    "musicIds": [1, 2, 3]
}
```

#### 4. Get LogSheets
```
GET /api/company/logsheets
Authorization: Bearer {jwt_token}
```

## Testing with Postman

### 1. Setup Postman Collection

1. Create a new collection called "Music Royalties System"
2. Set up environment variables:
   - `base_url`: `http://localhost:8080`
   - `jwt_token`: (leave empty initially)

### 2. Test Artist Registration

1. **Register Artist**
   - Method: `POST`
   - URL: `{{base_url}}/api/auth/register/artist`
   - Body (raw JSON):
   ```json
   {
       "email": "testartist@example.com",
       "password": "password123"
   }
   ```

2. **Check Email** for verification link

3. **Verify Email**
   - Method: `GET`
   - URL: `{{base_url}}/api/auth/verify?token={token_from_email}`

4. **Login Artist**
   - Method: `POST`
   - URL: `{{base_url}}/api/auth/login`
   - Body (raw JSON):
   ```json
   {
       "email": "testartist@example.com",
       "password": "password123"
   }
   ```
   - Save the JWT token to environment variable `jwt_token`

### 3. Test Profile Creation

1. **Create Artist Profile**
   - Method: `POST`
   - URL: `{{base_url}}/api/artist/profile`
   - Headers: `Authorization: Bearer {{jwt_token}}`
   - Body (raw JSON):
   ```json
   {
       "firstName": "Test",
       "surname": "Artist",
       "phoneNumber": "+1234567890",
       "email": "testartist@example.com",
       "nationality": "American",
       "occupation": "Musician",
       "accountHolderName": "Test Artist",
       "bankAccountNumber": "1234567890"
   }
   ```

### 4. Test File Uploads

1. **Upload Passport Photo**
   - Method: `POST`
   - URL: `{{base_url}}/api/artist/passport-photo`
   - Headers: `Authorization: Bearer {{jwt_token}}`
   - Body (form-data):
     - `file`: Select image file
     - `imageTitle`: "My Passport Photo"

2. **Upload Documents** (repeat for each document type)
   - Method: `POST`
   - URL: `{{base_url}}/api/artist/{document-type}`
   - Headers: `Authorization: Bearer {{jwt_token}}`
   - Body (form-data):
     - `file`: Select PDF file
     - `documentTitle`: "Document Title"

### 5. Test Admin Operations

1. **Login as Admin** (create admin user first)
   - Method: `POST`
   - URL: `{{base_url}}/api/auth/login`
   - Body (raw JSON):
   ```json
   {
       "email": "admin@example.com",
       "password": "admin123"
   }
   ```

2. **Get Pending Profiles**
   - Method: `GET`
   - URL: `{{base_url}}/api/admin/pending-profiles`
   - Headers: `Authorization: Bearer {{admin_jwt_token}}`

3. **Approve Profile**
   - Method: `POST`
   - URL: `{{base_url}}/api/admin/profile/approve/{memberId}`
   - Headers: `Authorization: Bearer {{admin_jwt_token}}`
   - Body (raw JSON):
   ```json
   {
       "ipiNumber": "IPI123456789"
   }
   ```

### 6. Test Music Upload (After Profile Approval)

1. **Upload Music**
   - Method: `POST`
   - URL: `{{base_url}}/api/artist/music/upload`
   - Headers: `Authorization: Bearer {{jwt_token}}`
   - Body (form-data):
     - `file`: Select audio/video file
     - `title`: "My Song"

### 7. Test Company Operations

1. **Create Company** (Admin only)
   - Method: `POST`
   - URL: `{{base_url}}/api/admin/company/create`
   - Headers: `Authorization: Bearer {{admin_jwt_token}}`
   - Body (raw JSON):
   ```json
   {
       "email": "company@example.com",
       "password": "password123",
       "companyName": "Test Radio Station",
       "companyAddress": "123 Radio St, City",
       "companyPhone": "+1234567890",
       "contactPerson": "Radio Manager"
   }
   ```

2. **Login as Company**
   - Method: `POST`
   - URL: `{{base_url}}/api/auth/login`
   - Body (raw JSON):
   ```json
   {
       "email": "company@example.com",
       "password": "password123"
   }
   ```

3. **Get Approved Music**
   - Method: `GET`
   - URL: `{{base_url}}/api/company/approved-music`
   - Headers: `Authorization: Bearer {{company_jwt_token}}`

4. **Create LogSheet**
   - Method: `POST`
   - URL: `{{base_url}}/api/company/logsheet`
   - Headers: `Authorization: Bearer {{company_jwt_token}}`
   - Body (raw JSON):
   ```json
   {
       "title": "Test Playlist",
       "musicIds": [1]
   }
   ```

## File Upload Guidelines

### Supported File Types

- **Passport Photos**: JPG, PNG, GIF (images only)
- **Documents**: PDF only
- **Music Files**: MP3, MP4, WAV, AVI (audio/video files)

### File Size Limits

- Maximum file size: 50MB
- Maximum request size: 50MB

### File Storage

Files are stored in the following directories:
- `uploads/images/` - Passport photos
- `uploads/documents/` - PDF documents
- `uploads/music/` - Audio/video files

## Email Configuration

The system sends automated emails for:
- Email verification
- Profile approval/rejection
- Music approval/rejection

### Email Templates

1. **Verification Email**: Contains verification link
2. **Approval Email**: Confirms profile/music approval
3. **Rejection Email**: Contains rejection reason and notes

## Security Features

- JWT-based authentication
- Role-based access control
- Password encryption (BCrypt)
- Secure file upload validation
- CORS configuration

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify PostgreSQL is running
   - Check database credentials in `application.properties`

2. **Email Not Sending**
   - Verify Gmail credentials
   - Check if 2FA is enabled
   - Generate new App Password

3. **File Upload Fails**
   - Check file type restrictions
   - Verify file size limits
   - Ensure upload directories exist

4. **JWT Token Issues**
   - Check token expiration
   - Verify Authorization header format
   - Ensure token is valid

### Logs

Check application logs for detailed error information:
```bash
tail -f logs/spring.log
```

## Development

### Project Structure

```
src/main/java/com/example/musicroyalties/
├── config/          # Configuration classes
├── controllers/     # REST controllers
├── models/          # Entity models
├── repositories/    # Data access layer
├── services/        # Business logic
└── MusicRoyaltiesApplication.java
```

### Adding New Features

1. Create model classes in `models/` package
2. Create repository interfaces in `repositories/` package
3. Implement business logic in `services/` package
4. Create REST endpoints in `controllers/` package
5. Update security configuration if needed

## Production Deployment

### Environment Variables

Set the following environment variables in production:
- `SPRING_PROFILES_ACTIVE=prod`
- `DATABASE_URL`
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`
- `JWT_SECRET`
- `EMAIL_USERNAME`
- `EMAIL_PASSWORD`

### Security Considerations

- Use strong JWT secrets
- Enable HTTPS
- Configure proper CORS policies
- Implement rate limiting
- Use secure file storage (e.g., AWS S3)

## Support

For issues and questions:
1. Check the troubleshooting section
2. Review application logs
3. Verify configuration settings
4. Test with Postman collection

## License

This project is licensed under the MIT License.
