# Music Royalties Frontend

A modern Next.js frontend for the Music Royalties Collection System.

## Features

- **Role-based Authentication**: Artist, Company, and Admin dashboards
- **Artist Management**: Profile creation, document uploads, music uploads
- **Company Operations**: Browse approved music, create logsheets
- **Admin Controls**: Approve/reject profiles and music, manage companies
- **File Upload System**: Secure file uploads with drag-and-drop
- **Responsive Design**: Mobile-first design with Tailwind CSS
- **Real-time Notifications**: Toast notifications for user feedback

## Technology Stack

- **Framework**: Next.js 14 with App Router
- **Styling**: Tailwind CSS
- **Forms**: React Hook Form with Zod validation
- **HTTP Client**: Axios
- **Icons**: Lucide React
- **Notifications**: React Hot Toast
- **TypeScript**: Full type safety

## Getting Started

1. **Install Dependencies**
   ```bash
   npm install
   ```

2. **Environment Setup**
   Create `.env.local`:
   ```
   NEXT_PUBLIC_API_URL=http://localhost:8080
   ```

3. **Start Development Server**
   ```bash
   npm run dev
   ```

4. **Build for Production**
   ```bash
   npm run build
   npm start
   ```

## Project Structure

```
src/
├── app/                    # Next.js App Router pages
│   ├── artist/            # Artist-specific pages
│   ├── company/           # Company-specific pages
│   ├── admin/             # Admin-specific pages
│   ├── login/             # Authentication pages
│   └── register/
├── components/            # Reusable components
│   ├── ui/               # UI components
│   └── Layout/           # Layout components
├── lib/                  # Utilities and configurations
│   ├── api.ts           # Axios configuration
│   ├── auth.ts          # Authentication utilities
│   └── utils.ts         # General utilities
└── types/               # TypeScript type definitions
```

## Key Features

### Authentication System
- JWT-based authentication
- Role-based access control
- Email verification flow
- Automatic token refresh

### Artist Features
- Complete profile management
- Document upload system (passport, ID, bank documents)
- Music file uploads with metadata
- Track approval status monitoring

### Company Features
- Browse approved music catalog
- Create and manage logsheets
- Music selection for playlists
- Company profile management

### Admin Features
- Profile approval/rejection workflow
- Music approval/rejection workflow
- Company creation and management
- User management system

### File Upload System
- Drag-and-drop file uploads
- File type validation
- Progress indicators
- Preview capabilities

## API Integration

The frontend integrates with the Spring Boot backend through:
- RESTful API endpoints
- JWT authentication headers
- Multipart form data for file uploads
- Error handling and user feedback

## Responsive Design

- Mobile-first approach
- Responsive grid layouts
- Touch-friendly interactions
- Optimized for all screen sizes

## Security Features

- JWT token management
- Role-based route protection
- Secure file upload validation
- XSS protection

## Development

### Adding New Pages
1. Create page component in appropriate `app/` directory
2. Add route protection if needed
3. Implement API integration
4. Add to navigation if required

### Styling Guidelines
- Use Tailwind CSS utility classes
- Follow consistent spacing (8px grid)
- Maintain color consistency
- Implement hover states and transitions

### State Management
- React hooks for local state
- localStorage for authentication
- API calls for server state
- Form state with React Hook Form

## Deployment

The frontend can be deployed to:
- Vercel (recommended for Next.js)
- Netlify
- Any static hosting service

Make sure to update the `NEXT_PUBLIC_API_URL` environment variable to point to your production backend.

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

1. Follow the existing code structure
2. Use TypeScript for type safety
3. Implement proper error handling
4. Add loading states for async operations
5. Test on multiple screen sizes