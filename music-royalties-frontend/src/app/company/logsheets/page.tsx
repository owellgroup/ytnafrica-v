'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { FileText, Plus, Eye, Music } from 'lucide-react';
import Layout from '@/components/Layout/Layout';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card';
import Button from '@/components/ui/Button';
import api from '@/lib/api';
import { getStoredUser } from '@/lib/auth';
import { LogSheet } from '@/types';
import { formatDate } from '@/lib/utils';

export default function CompanyLogSheets() {
  const [logSheets, setLogSheets] = useState<LogSheet[]>([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();
  const user = getStoredUser();

  useEffect(() => {
    if (!user || user.role !== 'COMPANY') {
      router.push('/login');
      return;
    }

    fetchLogSheets();
  }, [user, router]);

  const fetchLogSheets = async () => {
    try {
      const response = await api.get('/api/company/logsheets');
      setLogSheets(response.data);
    } catch (error) {
      console.error('Error fetching logsheets:', error);
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
            <h1 className="text-3xl font-bold text-gray-900">My LogSheets</h1>
            <p className="text-gray-600">Manage your music tracking logsheets</p>
          </div>
          <Button
            onClick={() => router.push('/company/logsheet/create')}
            className="flex items-center space-x-2"
          >
            <Plus className="h-4 w-4" />
            <span>Create LogSheet</span>
          </Button>
        </div>

        {/* LogSheets List */}
        {logSheets.length === 0 ? (
          <Card>
            <CardContent className="text-center py-12">
              <FileText className="h-12 w-12 text-gray-400 mx-auto mb-4" />
              <h3 className="text-lg font-medium text-gray-900 mb-2">No logsheets created yet</h3>
              <p className="text-gray-600 mb-6">
                Create your first logsheet to start tracking music usage
              </p>
              <Button onClick={() => router.push('/company/logsheet/create')}>
                Create Your First LogSheet
              </Button>
            </CardContent>
          </Card>
        ) : (
          <div className="grid grid-cols-1 gap-6">
            {logSheets.map((logSheet) => (
              <Card key={logSheet.id} className="hover:shadow-lg transition-shadow">
                <CardContent className="p-6">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-4">
                      <div className="p-3 bg-blue-100 rounded-lg">
                        <FileText className="h-6 w-6 text-blue-600" />
                      </div>
                      <div>
                        <h3 className="text-lg font-semibold text-gray-900">{logSheet.title}</h3>
                        <p className="text-gray-600">
                          Created: {formatDate(logSheet.createdDate)}
                        </p>
                        <p className="text-sm text-gray-500">
                          {logSheet.selectedMusic.length} tracks selected
                        </p>
                      </div>
                    </div>

                    <div className="flex items-center space-x-2">
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => router.push(`/company/logsheets/${logSheet.id}`)}
                        className="flex items-center space-x-1"
                      >
                        <Eye className="h-4 w-4" />
                        <span>View</span>
                      </Button>
                    </div>
                  </div>

                  {/* Preview of selected tracks */}
                  {logSheet.selectedMusic.length > 0 && (
                    <div className="mt-4 pt-4 border-t border-gray-200">
                      <h4 className="text-sm font-medium text-gray-900 mb-2">Selected Tracks:</h4>
                      <div className="space-y-1">
                        {logSheet.selectedMusic.slice(0, 3).map((track) => (
                          <div key={track.id} className="flex items-center space-x-2 text-sm text-gray-600">
                            <Music className="h-3 w-3" />
                            <span>{track.title} - {track.artist}</span>
                          </div>
                        ))}
                        {logSheet.selectedMusic.length > 3 && (
                          <p className="text-sm text-gray-500">
                            +{logSheet.selectedMusic.length - 3} more tracks
                          </p>
                        )}
                      </div>
                    </div>
                  )}
                </CardContent>
              </Card>
            ))}
          </div>
        )}
      </div>
    </Layout>
  );
}