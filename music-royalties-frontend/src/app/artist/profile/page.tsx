'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { User, Save, Upload, FileText, Image } from 'lucide-react';
import toast from 'react-hot-toast';
import Layout from '@/components/Layout/Layout';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card';
import Button from '@/components/ui/Button';
import Input from '@/components/ui/Input';
import FileUpload from '@/components/FileUpload';
import api from '@/lib/api';
import { getStoredUser } from '@/lib/auth';
import { MemberDetails, DocumentUpload } from '@/types';
import { getStatusColor } from '@/lib/utils';

const profileSchema = z.object({
  firstName: z.string().min(1, 'First name is required'),
  surname: z.string().min(1, 'Surname is required'),
  phoneNumber: z.string().min(1, 'Phone number is required'),
  email: z.string().email('Invalid email address'),
  nationality: z.string().min(1, 'Nationality is required'),
  occupation: z.string().min(1, 'Occupation is required'),
  accountHolderName: z.string().min(1, 'Account holder name is required'),
  bankAccountNumber: z.string().min(1, 'Bank account number is required'),
  pseudonym: z.string().optional(),
  groupNameORStageName: z.string().optional(),
  line1: z.string().optional(),
  line2: z.string().optional(),
  city: z.string().optional(),
  region: z.string().optional(),
  country: z.string().optional(),
  birthDate: z.string().optional(),
  placeOfBirth: z.string().optional(),
  idOrPassportNumber: z.string().optional(),
});

type ProfileForm = z.infer<typeof profileSchema>;

export default function ArtistProfile() {
  const [profile, setProfile] = useState<MemberDetails | null>(null);
  const [documents, setDocuments] = useState<any>({});
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [uploading, setUploading] = useState<string | null>(null);
  const router = useRouter();
  const user = getStoredUser();

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<ProfileForm>({
    resolver: zodResolver(profileSchema),
  });

  useEffect(() => {
    if (!user || user.role !== 'ARTIST') {
      router.push('/login');
      return;
    }

    fetchData();
  }, [user, router]);

  const fetchData = async () => {
    try {
      const [profileRes, documentsRes] = await Promise.all([
        api.get('/api/artist/profile').catch(() => ({ data: null })),
        api.get('/api/artist/documents').catch(() => ({ data: {} })),
      ]);

      if (profileRes.data) {
        setProfile(profileRes.data);
        reset(profileRes.data);
      }
      setDocuments(documentsRes.data);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const onSubmit = async (data: ProfileForm) => {
    setSaving(true);
    try {
      const response = profile 
        ? await api.put('/api/artist/profile', data)
        : await api.post('/api/artist/profile', data);
      
      setProfile(response.data);
      toast.success('Profile saved successfully!');
    } catch (error: any) {
      toast.error(error.response?.data || 'Failed to save profile');
    } finally {
      setSaving(false);
    }
  };

  const handleFileUpload = async (file: File, endpoint: string, title: string) => {
    setUploading(endpoint);
    try {
      const formData = new FormData();
      formData.append('file', file);
      formData.append(endpoint === 'passport-photo' ? 'imageTitle' : 'documentTitle', title);

      await api.post(`/api/artist/${endpoint}`, formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });

      toast.success('File uploaded successfully!');
      fetchData(); // Refresh documents
    } catch (error: any) {
      toast.error(error.response?.data || 'File upload failed');
    } finally {
      setUploading(null);
    }
  };

  if (loading) {
    return (
      <Layout>
        <div className="flex items-center justify-center h-64">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-purple-600"></div>
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="space-y-8">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Artist Profile</h1>
            <p className="text-gray-600">Complete your profile to start uploading music</p>
          </div>
          {profile?.status && (
            <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(profile.status.status)}`}>
              {profile.status.status}
            </span>
          )}
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* Profile Form */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center space-x-2">
                <User className="h-5 w-5" />
                <span>Personal Information</span>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <Input
                    {...register('firstName')}
                    label="First Name"
                    error={errors.firstName?.message}
                  />
                  <Input
                    {...register('surname')}
                    label="Surname"
                    error={errors.surname?.message}
                  />
                </div>

                <Input
                  {...register('email')}
                  label="Email"
                  type="email"
                  error={errors.email?.message}
                />

                <Input
                  {...register('phoneNumber')}
                  label="Phone Number"
                  error={errors.phoneNumber?.message}
                />

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <Input
                    {...register('nationality')}
                    label="Nationality"
                    error={errors.nationality?.message}
                  />
                  <Input
                    {...register('occupation')}
                    label="Occupation"
                    error={errors.occupation?.message}
                  />
                </div>

                <Input
                  {...register('pseudonym')}
                  label="Pseudonym (Optional)"
                  error={errors.pseudonym?.message}
                />

                <Input
                  {...register('groupNameORStageName')}
                  label="Group/Stage Name (Optional)"
                  error={errors.groupNameORStageName?.message}
                />

                <div className="space-y-4">
                  <h3 className="text-lg font-medium text-gray-900">Address Information</h3>
                  <Input
                    {...register('line1')}
                    label="Address Line 1"
                    error={errors.line1?.message}
                  />
                  <Input
                    {...register('line2')}
                    label="Address Line 2"
                    error={errors.line2?.message}
                  />
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <Input
                      {...register('city')}
                      label="City"
                      error={errors.city?.message}
                    />
                    <Input
                      {...register('region')}
                      label="Region/State"
                      error={errors.region?.message}
                    />
                  </div>
                  <Input
                    {...register('country')}
                    label="Country"
                    error={errors.country?.message}
                  />
                </div>

                <div className="space-y-4">
                  <h3 className="text-lg font-medium text-gray-900">Banking Information</h3>
                  <Input
                    {...register('accountHolderName')}
                    label="Account Holder Name"
                    error={errors.accountHolderName?.message}
                  />
                  <Input
                    {...register('bankAccountNumber')}
                    label="Bank Account Number"
                    error={errors.bankAccountNumber?.message}
                  />
                </div>

                <div className="space-y-4">
                  <h3 className="text-lg font-medium text-gray-900">Additional Information</h3>
                  <Input
                    {...register('birthDate')}
                    label="Birth Date"
                    type="date"
                    error={errors.birthDate?.message}
                  />
                  <Input
                    {...register('placeOfBirth')}
                    label="Place of Birth"
                    error={errors.placeOfBirth?.message}
                  />
                  <Input
                    {...register('idOrPassportNumber')}
                    label="ID/Passport Number"
                    error={errors.idOrPassportNumber?.message}
                  />
                </div>

                <Button
                  type="submit"
                  loading={saving}
                  disabled={saving}
                  className="w-full flex items-center justify-center space-x-2"
                >
                  <Save className="h-4 w-4" />
                  <span>Save Profile</span>
                </Button>
              </form>
            </CardContent>
          </Card>

          {/* Document Uploads */}
          <div className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center space-x-2">
                  <Image className="h-5 w-5" />
                  <span>Passport Photo</span>
                </CardTitle>
              </CardHeader>
              <CardContent>
                <FileUpload
                  label="Upload Passport Photo"
                  accept="image/*"
                  description="JPG, PNG, or GIF (max 50MB)"
                  currentFile={documents.passportPhoto?.imageUrl}
                  onFileSelect={(file) => handleFileUpload(file, 'passport-photo', 'Passport Photo')}
                />
                {uploading === 'passport-photo' && (
                  <div className="mt-2 text-sm text-blue-600">Uploading...</div>
                )}
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle className="flex items-center space-x-2">
                  <FileText className="h-5 w-5" />
                  <span>Proof of Payment</span>
                </CardTitle>
              </CardHeader>
              <CardContent>
                <FileUpload
                  label="Upload Proof of Payment"
                  accept=".pdf"
                  description="PDF only (max 50MB)"
                  currentFile={documents.proofOfPayment?.fileUrl}
                  onFileSelect={(file) => handleFileUpload(file, 'proof-of-payment', 'Proof of Payment')}
                />
                {uploading === 'proof-of-payment' && (
                  <div className="mt-2 text-sm text-blue-600">Uploading...</div>
                )}
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle className="flex items-center space-x-2">
                  <FileText className="h-5 w-5" />
                  <span>Bank Confirmation Letter</span>
                </CardTitle>
              </CardHeader>
              <CardContent>
                <FileUpload
                  label="Upload Bank Confirmation Letter"
                  accept=".pdf"
                  description="PDF only (max 50MB)"
                  currentFile={documents.bankConfirmationLetter?.fileUrl}
                  onFileSelect={(file) => handleFileUpload(file, 'bank-confirmation-letter', 'Bank Confirmation Letter')}
                />
                {uploading === 'bank-confirmation-letter' && (
                  <div className="mt-2 text-sm text-blue-600">Uploading...</div>
                )}
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <CardTitle className="flex items-center space-x-2">
                  <FileText className="h-5 w-5" />
                  <span>ID Document</span>
                </CardTitle>
              </CardHeader>
              <CardContent>
                <FileUpload
                  label="Upload ID Document"
                  accept=".pdf"
                  description="PDF only (max 50MB)"
                  currentFile={documents.idDocument?.fileUrl}
                  onFileSelect={(file) => handleFileUpload(file, 'id-document', 'ID Document')}
                />
                {uploading === 'id-document' && (
                  <div className="mt-2 text-sm text-blue-600">Uploading...</div>
                )}
              </CardContent>
            </Card>
          </div>
        </div>
      </div>
    </Layout>
  );
}