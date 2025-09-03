'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Users, Music, Building, CheckCircle, Clock, XCircle } from 'lucide-react';
import Layout from '@/components/Layout/Layout';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card';
import Button from '@/components/ui/Button';
import api from '@/lib/api';
import { getStoredUser } from '@/lib/auth';
import { MemberDetails, ArtistWork } from '@/types';
import { getStatusColor, formatDate } from '@/lib/utils';
import toast from 'react-hot-toast';

export default function AdminDashboard() {
  const [pendingProfiles, setPendingProfiles] = useState<MemberDetails[]>([]);
  const [pendingMusic, setPendingMusic] = useState<ArtistWork[]>([]);
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState<string | null>(null);
  const router = useRouter();
  const user = getStoredUser();

  useEffect(() => {
    if (!user || user.role !== 'ADMIN') {
      router.push('/login');
      return;
    }

    fetchData();
  }, [user, router]);

  const fetchData = async () => {
    try {
      const [profilesRes, musicRes] = await Promise.all([
        api.get('/api/admin/pending-profiles'),
        api.get('/api/admin/pending-music'),
      ]);

      setPendingProfiles(profilesRes.data);
      setPendingMusic(musicRes.data);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleProfileAction = async (memberId: number, action: 'approve' | 'reject', data: any) => {
    setActionLoading(`profile-${memberId}-${action}`);
    try {
      await api.post(`/api/admin/profile/${action}/${memberId}`, data);
      toast.success(`Profile ${action}d successfully!`);
      fetchData();
    } catch (error: any) {
      toast.error(error.response?.data || `Failed to ${action} profile`);
    } finally {
      setActionLoading(null);
    }
  };

  const handleMusicAction = async (musicId: number, action: 'approve' | 'reject', data: any) => {
    setActionLoading(`music-${musicId}-${action}`);
    try {
      await api.post(`/api/admin/music/${action}/${musicId}`, data);
      toast.success(`Music ${action}d successfully!`);
      fetchData();
    } catch (error: any) {
      toast.error(error.response?.data || `Failed to ${action} music`);
    } finally {
      setActionLoading(null);
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
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Admin Dashboard</h1>
          <p className="text-gray-600">Manage profiles, music, and system operations</p>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
          <Card>
            <CardContent className="p-6">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-yellow-100 rounded-lg">
                  <Clock className="h-6 w-6 text-yellow-600" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Pending Profiles</p>
                  <p className="text-2xl font-bold text-gray-900">{pendingProfiles.length}</p>
                </div>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-blue-100 rounded-lg">
                  <Music className="h-6 w-6 text-blue-600" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Pending Music</p>
                  <p className="text-2xl font-bold text-gray-900">{pendingMusic.length}</p>
                </div>
              </div>
            </CardContent>
          </Card>

          <Card className="cursor-pointer hover:shadow-lg transition-shadow" onClick={() => router.push('/admin/companies')}>
            <CardContent className="p-6">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-green-100 rounded-lg">
                  <Building className="h-6 w-6 text-green-600" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Companies</p>
                  <p className="text-2xl font-bold text-gray-900">Manage</p>
                </div>
              </div>
            </CardContent>
          </Card>

          <Card className="cursor-pointer hover:shadow-lg transition-shadow" onClick={() => router.push('/admin/users')}>
            <CardContent className="p-6">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-purple-100 rounded-lg">
                  <Users className="h-6 w-6 text-purple-600" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Users</p>
                  <p className="text-2xl font-bold text-gray-900">Manage</p>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Pending Profiles */}
        {pendingProfiles.length > 0 && (
          <Card>
            <CardHeader>
              <CardTitle>Pending Profile Approvals</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {pendingProfiles.map((profile) => (
                  <div key={profile.id} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                    <div className="flex items-center space-x-3">
                      <Users className="h-5 w-5 text-gray-400" />
                      <div>
                        <h4 className="font-medium text-gray-900">
                          {profile.firstName} {profile.surname}
                        </h4>
                        <p className="text-sm text-gray-500">{profile.email}</p>
                        <p className="text-sm text-gray-500">{profile.occupation}</p>
                      </div>
                    </div>
                    <div className="flex items-center space-x-2">
                      <Button
                        size="sm"
                        variant="secondary"
                        onClick={() => router.push(`/admin/profiles/${profile.id}`)}
                      >
                        Review
                      </Button>
                      <Button
                        size="sm"
                        loading={actionLoading === `profile-${profile.id}-approve`}
                        onClick={() => {
                          const ipiNumber = prompt('Enter IPI Number:');
                          if (ipiNumber) {
                            handleProfileAction(profile.id!, 'approve', { ipiNumber });
                          }
                        }}
                      >
                        <CheckCircle className="h-4 w-4 mr-1" />
                        Approve
                      </Button>
                      <Button
                        size="sm"
                        variant="danger"
                        loading={actionLoading === `profile-${profile.id}-reject`}
                        onClick={() => {
                          const notes = prompt('Enter rejection reason:');
                          if (notes) {
                            handleProfileAction(profile.id!, 'reject', { notes });
                          }
                        }}
                      >
                        <XCircle className="h-4 w-4 mr-1" />
                        Reject
                      </Button>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        )}

        {/* Pending Music */}
        {pendingMusic.length > 0 && (
          <Card>
            <CardHeader>
              <CardTitle>Pending Music Approvals</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {pendingMusic.map((track) => (
                  <div key={track.id} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                    <div className="flex items-center space-x-3">
                      <Music className="h-5 w-5 text-gray-400" />
                      <div>
                        <h4 className="font-medium text-gray-900">{track.title}</h4>
                        <p className="text-sm text-gray-500">{track.artist}</p>
                        <p className="text-sm text-gray-500">
                          Uploaded: {formatDate(track.uploadedDate)}
                        </p>
                        <p className="text-sm text-gray-500">
                          By: {track.user.email}
                        </p>
                      </div>
                    </div>
                    <div className="flex items-center space-x-2">
                      <a
                        href={track.fileUrl}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-purple-600 hover:text-purple-800 text-sm"
                      >
                        Listen
                      </a>
                      <Button
                        size="sm"
                        loading={actionLoading === `music-${track.id}-approve`}
                        onClick={() => {
                          const isrcCode = prompt('Enter ISRC Code:');
                          if (isrcCode) {
                            handleMusicAction(track.id, 'approve', { isrcCode });
                          }
                        }}
                      >
                        <CheckCircle className="h-4 w-4 mr-1" />
                        Approve
                      </Button>
                      <Button
                        size="sm"
                        variant="danger"
                        loading={actionLoading === `music-${track.id}-reject`}
                        onClick={() => {
                          const notes = prompt('Enter rejection reason:');
                          if (notes) {
                            handleMusicAction(track.id, 'reject', { notes });
                          }
                        }}
                      >
                        <XCircle className="h-4 w-4 mr-1" />
                        Reject
                      </Button>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        )}

        {/* Quick Actions */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card className="hover:shadow-lg transition-shadow cursor-pointer" onClick={() => router.push('/admin/companies/create')}>
            <CardHeader>
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-blue-100 rounded-lg">
                  <Building className="h-6 w-6 text-blue-600" />
                </div>
                <CardTitle>Create Company</CardTitle>
              </div>
            </CardHeader>
            <CardContent>
              <p className="text-gray-600">Add new radio/TV companies to the system</p>
            </CardContent>
          </Card>

          <Card className="hover:shadow-lg transition-shadow cursor-pointer" onClick={() => router.push('/admin/users')}>
            <CardHeader>
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-green-100 rounded-lg">
                  <Users className="h-6 w-6 text-green-600" />
                </div>
                <CardTitle>Manage Users</CardTitle>
              </div>
            </CardHeader>
            <CardContent>
              <p className="text-gray-600">View and manage all system users</p>
            </CardContent>
          </Card>

          <Card className="hover:shadow-lg transition-shadow cursor-pointer" onClick={() => router.push('/admin/reports')}>
            <CardHeader>
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-purple-100 rounded-lg">
                  <CheckCircle className="h-6 w-6 text-purple-600" />
                </div>
                <CardTitle>Reports</CardTitle>
              </div>
            </CardHeader>
            <CardContent>
              <p className="text-gray-600">View system reports and analytics</p>
            </CardContent>
          </Card>
        </div>
      </div>
    </Layout>
  );
}