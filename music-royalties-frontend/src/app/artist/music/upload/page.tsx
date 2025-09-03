'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Upload, Music, Save } from 'lucide-react';
import toast from 'react-hot-toast';
import Layout from '@/components/Layout/Layout';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card';
import Button from '@/components/ui/Button';
import Input from '@/components/ui/Input';
import FileUpload from '@/components/FileUpload';
import api from '@/lib/api';
import { getStoredUser } from '@/lib/auth';

const musicSchema = z.object({
  title: z.string().min(1, 'Title is required'),
  artist: z.string().min(1, 'Artist name is required'),
  albumName: z.string().optional(),
  groupOrBandOrStageName: z.string().optional(),
  featuredArtist: z.string().optional(),
  producer: z.string().optional(),
  country: z.string().optional(),
  duration: z.string().optional(),
  composer: z.string().optional(),
  author: z.string().optional(),
  arranger: z.string().optional(),
  publisher: z.string().optional(),
  publishersName: z.string().optional(),
  publisherAddress: z.string().optional(),
  publisherTelephone: z.string().optional(),
  recordedBy: z.string().optional(),
  addressOfRecordingCompany: z.string().optional(),
  labelName: z.string().optional(),
  dateRecorded: z.string().optional(),
});

type MusicForm = z.infer<typeof musicSchema>;

export default function UploadMusic() {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [uploading, setUploading] = useState(false);
  const [profile, setProfile] = useState<any>(null);
  const router = useRouter();
  const user = getStoredUser();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<MusicForm>({
    resolver: zodResolver(musicSchema),
  });

  useEffect(() => {
    if (!user || user.role !== 'ARTIST') {
      router.push('/login');
      return;
    }

    checkProfile();
  }, [user, router]);

  const checkProfile = async () => {
    try {
      const response = await api.get('/api/artist/profile');
      setProfile(response.data);
      
      if (response.data.status?.status !== 'APPROVED') {
        toast.error('Your profile must be approved before uploading music');
        router.push('/artist/profile');
      }
    } catch (error) {
      toast.error('Please complete your profile first');
      router.push('/artist/profile');
    }
  };

  const onSubmit = async (data: MusicForm) => {
    if (!selectedFile) {
      toast.error('Please select a music file');
      return;
    }

    setUploading(true);
    try {
      const formData = new FormData();
      formData.append('file', selectedFile);
      formData.append('title', data.title);
      formData.append('ArtistId', profile?.artistId || '');
      formData.append('albumName', data.albumName || '');
      formData.append('artist', data.artist);
      formData.append('GroupOrBandOrStageName', data.groupOrBandOrStageName || '');
      formData.append('featuredArtist', data.featuredArtist || '');
      formData.append('producer', data.producer || '');
      formData.append('country', data.country || '');
      formData.append('uploadedDate', new Date().toISOString().split('T')[0]);
      formData.append('artistUploadTypeId', '1'); // Default value
      formData.append('artistWorkTypeId', '1'); // Default value
      formData.append('Duration', data.duration || '');
      formData.append('composer', data.composer || '');
      formData.append('author', data.author || '');
      formData.append('arranger', data.arranger || '');
      formData.append('publisher', data.publisher || '');
      formData.append('publishersName', data.publishersName || '');
      formData.append('publisherAdress', data.publisherAddress || '');
      formData.append('publisherTelephone', data.publisherTelephone || '');
      formData.append('recordedBy', data.recordedBy || '');
      formData.append('AddressOfRecordingCompany', data.addressOfRecordingCompany || '');
      formData.append('labelName', data.labelName || '');
      formData.append('dateRecorded', data.dateRecorded || '');

      await api.post('/api/artist/music/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' },
      });

      toast.success('Music uploaded successfully!');
      router.push('/artist/music');
    } catch (error: any) {
      toast.error(error.response?.data || 'Upload failed');
    } finally {
      setUploading(false);
    }
  };

  if (!profile) {
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
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Upload Music</h1>
          <p className="text-gray-600">Upload your music files for royalties collection</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* File Upload */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center space-x-2">
                <Upload className="h-5 w-5" />
                <span>Music File</span>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <FileUpload
                label="Select Music File"
                accept="audio/*,video/*"
                description="MP3, MP4, WAV, AVI (max 50MB)"
                onFileSelect={setSelectedFile}
              />
            </CardContent>
          </Card>

          {/* Music Information Form */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center space-x-2">
                <Music className="h-5 w-5" />
                <span>Music Information</span>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
                <Input
                  {...register('title')}
                  label="Song Title"
                  error={errors.title?.message}
                />

                <Input
                  {...register('artist')}
                  label="Artist Name"
                  error={errors.artist?.message}
                />

                <Input
                  {...register('albumName')}
                  label="Album Name (Optional)"
                  error={errors.albumName?.message}
                />

                <Input
                  {...register('groupOrBandOrStageName')}
                  label="Group/Band/Stage Name (Optional)"
                  error={errors.groupOrBandOrStageName?.message}
                />

                <Input
                  {...register('featuredArtist')}
                  label="Featured Artist (Optional)"
                  error={errors.featuredArtist?.message}
                />

                <Input
                  {...register('producer')}
                  label="Producer (Optional)"
                  error={errors.producer?.message}
                />

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <Input
                    {...register('country')}
                    label="Country (Optional)"
                    error={errors.country?.message}
                  />
                  <Input
                    {...register('duration')}
                    label="Duration (Optional)"
                    placeholder="e.g., 3:45"
                    error={errors.duration?.message}
                  />
                </div>

                <div className="space-y-4">
                  <h3 className="text-lg font-medium text-gray-900">Credits</h3>
                  
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <Input
                      {...register('composer')}
                      label="Composer (Optional)"
                      error={errors.composer?.message}
                    />
                    <Input
                      {...register('author')}
                      label="Author (Optional)"
                      error={errors.author?.message}
                    />
                  </div>

                  <Input
                    {...register('arranger')}
                    label="Arranger (Optional)"
                    error={errors.arranger?.message}
                  />
                </div>

                <div className="space-y-4">
                  <h3 className="text-lg font-medium text-gray-900">Publishing Information</h3>
                  
                  <Input
                    {...register('publisher')}
                    label="Publisher (Optional)"
                    error={errors.publisher?.message}
                  />

                  <Input
                    {...register('publishersName')}
                    label="Publisher's Name (Optional)"
                    error={errors.publishersName?.message}
                  />

                  <Input
                    {...register('publisherAddress')}
                    label="Publisher Address (Optional)"
                    error={errors.publisherAddress?.message}
                  />

                  <Input
                    {...register('publisherTelephone')}
                    label="Publisher Telephone (Optional)"
                    error={errors.publisherTelephone?.message}
                  />
                </div>

                <div className="space-y-4">
                  <h3 className="text-lg font-medium text-gray-900">Recording Information</h3>
                  
                  <Input
                    {...register('recordedBy')}
                    label="Recorded By (Optional)"
                    error={errors.recordedBy?.message}
                  />

                  <Input
                    {...register('addressOfRecordingCompany')}
                    label="Recording Company Address (Optional)"
                    error={errors.addressOfRecordingCompany?.message}
                  />

                  <Input
                    {...register('labelName')}
                    label="Label Name (Optional)"
                    error={errors.labelName?.message}
                  />

                  <Input
                    {...register('dateRecorded')}
                    label="Date Recorded (Optional)"
                    type="date"
                    error={errors.dateRecorded?.message}
                  />
                </div>

                <Button
                  type="submit"
                  loading={uploading}
                  disabled={uploading || !selectedFile}
                  className="w-full flex items-center justify-center space-x-2"
                >
                  <Save className="h-4 w-4" />
                  <span>Upload Music</span>
                </Button>
              </form>
            </CardContent>
          </Card>
        </div>
      </div>
    </Layout>
  );
}