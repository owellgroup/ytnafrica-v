'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { User, Upload, Music, FileText, CheckCircle, Clock, XCircle } from 'lucide-react';
import Layout from '@/components/Layout/Layout';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card';
import Button from '@/components/ui/Button';
import { getStoredUser } from '@/lib/auth';
import { MemberDetails, ArtistWork } from '@/types';
import api from '@/lib/api';
import { getStatusColor } from '@/lib/utils';
import toast from 'react-hot-toast';

export default function ArtistDashboard() {
  const [profile, setProfile] = useState<MemberDetails | null>(null);
  const [music, setMusic] = useState<ArtistWork[]>([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();
  const user = getStoredUser();

  useEffect(() => {
    if (!user || user.role !== 'ARTIST') {
      router.push('/login');
      return;
    }

    fetchData();
  }, [user, router]);

  const fetchData = async () => {
    try {
      const [profileRes, musicRes] = await Promise.all([
        api.get('/api/artist/profile').catch(() => ({ data: null })),
        api.get('/api/artist/music').catch(() => ({ data: [] })),
      ]);

      setProfile(profileRes.data);
      setMusic(musicRes.data);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const getProfileStatus = () => {
    if (!profile) return 'incomplete';
    return profile.status?.status.toLowerCase() || 'pending';
  };

  const canUploadMusic = () => {
    return profile?.status?.status === 'APPROVED';
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

  const profileStatus = getProfileStatus();

  return (
    <Layout>
      <div className="space-y-8">
        {/* Header */}
        <div className="flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Artist Dashboard</h1>
            <p className="text-gray-600">Welcome back, {user?.email}</p>
          </div>
          <div className="flex items-center space-x-2">
            <div className={`px-3 py-1 rounded-full text-sm font-medium ${
              user?.emailVerified 
                ? 'bg-green-100 text-green-800' 
                : 'bg-yellow-100 text-yellow-800'
            }`}>
              {user?.emailVerified ? 'Verified' : 'Unverified'}
            </div>
          </div>
        </div>

        {/* Profile Status Alert */}
        {profileStatus === 'incomplete' && (
          <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
            <div className="flex items-center space-x-2">
              <Clock className="h-5 w-5 text-blue-600" />
              <div>
                <h3 className="font-medium text-blue-900">Complete Your Profile</h3>
                <p className="text-sm text-blue-700">
                  You need to complete your profile and upload required documents before you can upload music.
                </p>
              </div>
            </div>
          </div>
        )}

        {profileStatus === 'pending' && (
          <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
            <div className="flex items-center space-x-2">
              <Clock className="h-5 w-5 text-yellow-600" />
              <div>
                <h3 className="font-medium text-yellow-900">Profile Under Review</h3>
                <p className="text-sm text-yellow-700">
                  Your profile is being reviewed by our team. You'll receive an email once it's approved.
                </p>
              </div>
            </div>
          </div>
        )}

        {profileStatus === 'rejected' && (
          <div className="bg-red-50 border border-red-200 rounded-lg p-4">
            <div className="flex items-center space-x-2">
              <XCircle className="h-5 w-5 text-red-600" />
              <div>
                <h3 className="font-medium text-red-900">Profile Rejected</h3>
                <p className="text-sm text-red-700">
                  {profile?.notes || 'Your profile was rejected. Please review and resubmit.'}
                </p>
              </div>
            </div>
          </div>
        )}

        {profileStatus === 'approved' && (
          <div className="bg-green-50 border border-green-200 rounded-lg p-4">
            <div className="flex items-center space-x-2">
              <CheckCircle className="h-5 w-5 text-green-600" />
              <div>
                <h3 className="font-medium text-green-900">Profile Approved</h3>
                <p className="text-sm text-green-700">
                  Your profile has been approved! You can now upload music files.
                  {profile?.IPI_number && ` Your IPI number: ${profile.IPI_number}`}
                </p>
              </div>
            </div>
          </div>
        )}

        {/* Quick Actions */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card className="hover:shadow-lg transition-shadow cursor-pointer" onClick={() => router.push('/artist/profile')}>
            <CardHeader>
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-purple-100 rounded-lg">
                  <User className="h-6 w-6 text-purple-600" />
                </div>
                <CardTitle>Profile</CardTitle>
              </div>
            </CardHeader>
            <CardContent>
              <p className="text-gray-600">Manage your artist profile and documents</p>
              <div className="mt-2">
                <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(profileStatus.toUpperCase())}`}>
                  {profileStatus.charAt(0).toUpperCase() + profileStatus.slice(1)}
                </span>
              </div>
            </CardContent>
          </Card>

          <Card className={`hover:shadow-lg transition-shadow ${canUploadMusic() ? 'cursor-pointer' : 'opacity-50'}`} 
                onClick={() => canUploadMusic() && router.push('/artist/music/upload')}>
            <CardHeader>
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-indigo-100 rounded-lg">
                  <Upload className="h-6 w-6 text-indigo-600" />
                </div>
                <CardTitle>Upload Music</CardTitle>
              </div>
            </CardHeader>
            <CardContent>
              <p className="text-gray-600">
                {canUploadMusic() ? 'Upload your music files' : 'Complete profile approval first'}
              </p>
            </CardContent>
          </Card>

          <Card className="hover:shadow-lg transition-shadow cursor-pointer" onClick={() => router.push('/artist/music')}>
            <CardHeader>
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-green-100 rounded-lg">
                  <Music className="h-6 w-6 text-green-600" />
                </div>
                <CardTitle>My Music</CardTitle>
              </div>
            </CardHeader>
            <CardContent>
              <p className="text-gray-600">View and manage your uploaded music</p>
              <p className="text-sm text-gray-500 mt-1">{music.length} files uploaded</p>
            </CardContent>
          </Card>
        </div>

        {/* Recent Music */}
        {music.length > 0 && (
          <Card>
            <CardHeader>
              <CardTitle>Recent Music Uploads</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {music.slice(0, 5).map((track) => (
                  <div key={track.id} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                    <div className="flex items-center space-x-3">
                      <Music className="h-5 w-5 text-gray-400" />
                      <div>
                        <h4 className="font-medium text-gray-900">{track.title}</h4>
                        <p className="text-sm text-gray-500">{track.artist}</p>
                      </div>
                    </div>
                    <div className="flex items-center space-x-2">
                      <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(track.status.status)}`}>
                        {track.status.status}
                      </span>
                    </div>
                  </div>
                ))}
                {music.length > 5 && (
                  <Button
                    variant="ghost"
                    onClick={() => router.push('/artist/music')}
                    className="w-full"
                  >
                    View All Music ({music.length})
                  </Button>
                )}
              </div>
            </CardContent>
          </Card>
        )}

        {/* Getting Started */}
        {profileStatus === 'incomplete' && (
          <Card>
            <CardHeader>
              <CardTitle>Getting Started</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div className="flex items-center space-x-3">
                  <div className="w-8 h-8 bg-purple-100 rounded-full flex items-center justify-center">
                    <span className="text-sm font-bold text-purple-600">1</span>
                  </div>
                  <div>
                    <h4 className="font-medium text-gray-900">Complete Your Profile</h4>
                    <p className="text-sm text-gray-600">Fill out your personal and banking information</p>
                  </div>
                </div>
                
                <div className="flex items-center space-x-3">
                  <div className="w-8 h-8 bg-gray-100 rounded-full flex items-center justify-center">
                    <span className="text-sm font-bold text-gray-400">2</span>
                  </div>
                  <div>
                    <h4 className="font-medium text-gray-500">Upload Documents</h4>
                    <p className="text-sm text-gray-500">Upload required documents for verification</p>
                  </div>
                </div>
                
                <div className="flex items-center space-x-3">
                  <div className="w-8 h-8 bg-gray-100 rounded-full flex items-center justify-center">
                    <span className="text-sm font-bold text-gray-400">3</span>
                  </div>
                  <div>
                    <h4 className="font-medium text-gray-500">Upload Music</h4>
                    <p className="text-sm text-gray-500">Start uploading your music files</p>
                  </div>
                </div>

                <Button onClick={() => router.push('/artist/profile')} className="w-full mt-4">
                  Complete Profile
                </Button>
              </div>
            </CardContent>
          </Card>
        )}
      </div>
    </Layout>
  );
}