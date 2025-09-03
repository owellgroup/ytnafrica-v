export interface User {
  id: number;
  email: string;
  role: 'ARTIST' | 'COMPANY' | 'ADMIN';
  enabled: boolean;
  emailVerified: boolean;
}

export interface MemberDetails {
  id?: number;
  firstName: string;
  surname: string;
  phoneNumber: string;
  email: string;
  nationality: string;
  occupation: string;
  accountHolderName: string;
  bankAccountNumber: string;
  pseudonym?: string;
  groupNameORStageName?: string;
  line1?: string;
  line2?: string;
  city?: string;
  region?: string;
  country?: string;
  birthDate?: string;
  placeOfBirth?: string;
  idOrPassportNumber?: string;
  status?: {
    id: number;
    status: 'PENDING' | 'APPROVED' | 'REJECTED';
  };
  IPI_number?: string;
  notes?: string;
}

export interface ArtistWork {
  id: number;
  title: string;
  artist: string;
  albumName?: string;
  fileUrl: string;
  fileType: string;
  uploadedDate: string;
  status: {
    id: number;
    status: 'PENDING' | 'APPROVED' | 'REJECTED';
  };
  ISRC_code?: string;
  notes?: string;
  user: User;
}

export interface Company {
  id: number;
  companyName: string;
  companyAddress: string;
  companyPhone: string;
  companyEmail: string;
  contactPerson: string;
  user: User;
}

export interface LogSheet {
  id: number;
  title: string;
  createdDate: string;
  company: Company;
  selectedMusic: ArtistWork[];
}

export interface DocumentUpload {
  id: number;
  documentTitle?: string;
  imageTitle?: string;
  fileUrl: string;
  fileType: string;
  datePosted: string;
  user: User;
}