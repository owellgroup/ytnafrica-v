'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Building, Music, FileText, Plus } from 'lucide-react';
import Layout from '@/components/Layout/Layout';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card';
import Button from '@/components/ui/Button';
import api from '@/lib/api';
import { getStoredUser } from '@/lib/auth';
import { Company, ArtistWork, LogSheet } from '@/types';
import { formatDate } from '@/lib/utils';

export default function CompanyDashboard() {
  const [company, setCompany] = useState<Company | null>(null);
  const [approvedMusic, setApprovedMusic] = useState<ArtistWork[]>([]);
  const [logSheets, setLogSheets] = useState<LogSheet[]>([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();
  const user = getStoredUser();

  useEffect(() => {
    if (!user || user.role !== 'COMPANY') {
      router.push('/login');
      return;
    }

    fetchData();
  }, [user, router]);

  const fetchData = async () => {
    try {
      const [companyRes, musicRes, logSheetsRes] = await Promise.all([
        api.get('/api/company/profile'),
        api.get('/api/company/approved-music'),
        api.get('/api/company/logsheets'),
      ]);

      setCompany(companyRes.data);
      setApprovedMusic(musicRes.data);
      setLogSheets(logSheetsRes.data);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
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
            <h1 className="text-3xl font-bold text-gray-900">Company Dashboard</h1>
            <p className="text-gray-600">
              Welcome back, {company?.companyName || user?.email}
            </p>
          </div>
        </div>

        {/* Stats Cards */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card>
            <CardContent className="p-6">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-green-100 rounded-lg">
                  <Music className="h-6 w-6 text-green-600" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Available Music</p>
                  <p className="text-2xl font-bold text-gray-900">{approvedMusic.length}</p>
                </div>
              </div>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-blue-100 rounded-lg">
                  <FileText className="h-6 w-6 text-blue-600" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">LogSheets</p>
                  <p className="text-2xl font-bold text-gray-900">{logSheets.length}</p>
                </div>
              </div>
            </CardContent>
          </Card>

          <Card className="hover:shadow-lg transition-shadow cursor-pointer" onClick={() => router.push('/company/logsheet/create')}>
            <CardContent className="p-6">
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-purple-100 rounded-lg">
                  <Plus className="h-6 w-6 text-purple-600" />
                </div>
                <div>
                  <p className="text-sm text-gray-600">Create LogSheet</p>
                  <p className="text-sm text-purple-600 font-medium">Click to create</p>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Quick Actions */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Card className="hover:shadow-lg transition-shadow cursor-pointer" onClick={() => router.push('/company/music')}>
            <CardHeader>
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-green-100 rounded-lg">
                  <Music className="h-6 w-6 text-green-600" />
                </div>
                <CardTitle>Browse Music</CardTitle>
              </div>
            </CardHeader>
            <CardContent>
              <p className="text-gray-600">Browse and select from approved music tracks</p>
              <p className="text-sm text-gray-500 mt-1">{approvedMusic.length} tracks available</p>
            </CardContent>
          </Card>

          <Card className="hover:shadow-lg transition-shadow cursor-pointer" onClick={() => router.push('/company/logsheets')}>
            <CardHeader>
              <div className="flex items-center space-x-3">
                <div className="p-2 bg-blue-100 rounded-lg">
                  <FileText className="h-6 w-6 text-blue-600" />
                </div>
                <CardTitle>My LogSheets</CardTitle>
              </div>
            </CardHeader>
            <CardContent>
              <p className="text-gray-600">View and manage your music logsheets</p>
              <p className="text-sm text-gray-500 mt-1">{logSheets.length} logsheets created</p>
            </CardContent>
          </Card>
        </div>

        {/* Recent LogSheets */}
        {logSheets.length > 0 && (
          <Card>
            <CardHeader>
              <CardTitle>Recent LogSheets</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {logSheets.slice(0, 5).map((logSheet) => (
                  <div key={logSheet.id} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                    <div className="flex items-center space-x-3">
                      <FileText className="h-5 w-5 text-gray-400" />
                      <div>
                        <h4 className="font-medium text-gray-900">{logSheet.title}</h4>
                        <p className="text-sm text-gray-500">
                          Created: {formatDate(logSheet.createdDate)}
                        </p>
                        <p className="text-sm text-gray-500">
                          {logSheet.selectedMusic.length} tracks selected
                        </p>
                      </div>
                    </div>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => router.push(`/company/logsheets/${logSheet.id}`)}
                    >
                      View
                    </Button>
                  </div>
                ))}
                {logSheets.length > 5 && (
                  <Button
                    variant="ghost"
                    onClick={() => router.push('/company/logsheets')}
                    className="w-full"
                  >
                    View All LogSheets ({logSheets.length})
                  </Button>
                )}
              </div>
            </CardContent>
          </Card>
        )}

        {/* Company Profile */}
        {company && (
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center space-x-2">
                <Building className="h-5 w-5" />
                <span>Company Information</span>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <p className="text-sm text-gray-600">Company Name</p>
                  <p className="font-medium text-gray-900">{company.companyName}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600">Contact Person</p>
                  <p className="font-medium text-gray-900">{company.contactPerson}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600">Phone</p>
                  <p className="font-medium text-gray-900">{company.companyPhone}</p>
                </div>
                <div>
                  <p className="text-sm text-gray-600">Email</p>
                  <p className="font-medium text-gray-900">{company.companyEmail}</p>
                </div>
                <div className="md:col-span-2">
                  <p className="text-sm text-gray-600">Address</p>
                  <p className="font-medium text-gray-900">{company.companyAddress}</p>
                </div>
              </div>
            </CardContent>
          </Card>
        )}
      </div>
    </Layout>
  );
}