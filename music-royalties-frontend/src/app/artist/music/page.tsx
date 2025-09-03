'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Music, Plus, Eye, Download } from 'lucide-react';
import Layout from '@/components/Layout/Layout';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card';
import Button from '@/components/ui/Button';
import api from '@/lib/api';
import { getStoredUser } from '@/lib/auth';
import { ArtistWork } from '@/types';
import { getStatusColor, formatDate } from '@/lib/utils';

export default function ArtistMusic() {
  const [music, setMusic] = useState<ArtistWork[]>([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();
  const user = getStoredUser();

  useEffect(() => {
    if (!user || user.role !== 'ARTIST') {
      router.push('/login');
      return;
    }

    fetchMusic();
  }, [user, router]);

  const fetchMusic = async () => {
    try {
      const response = await api.get('/api/artist/music');
      setMusic(response.data);
    } catch (error) {
      console.error('Error fetching music:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDownload = async (musicId: number) => {
    try {
      const response = await api.get(`/api/music/download/${musicId}`, {
        responseType: 'blob',
      });
      
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `music_${musicId}`);
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      console.error('Download failed:', error);
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
            <h1 className="text-3xl font-bold text-gray-900">My Music</h1>
            <p className="text-gray-600">Manage your uploaded music files</p>
          </div>
          <Button
            onClick={() => router.push('/artist/music/upload')}
            className="flex items-center space-x-2"
          >
            <Plus className="h-4 w-4" />
            <span>Upload Music</span>
          </Button>
        </div>

        {/* Music List */}
        {music.length === 0 ? (
          <Card>
            <CardContent className="text-center py-12">
              <Music className="h-12 w-12 text-gray-400 mx-auto mb-4" />
              <h3 className="text-lg font-medium text-gray-900 mb-2">No music uploaded yet</h3>
              <p className="text-gray-600 mb-6">
                Upload your first music file to get started with royalties collection
              </p>
              <Button onClick={() => router.push('/artist/music/upload')}>
                Upload Your First Track
              </Button>
            </CardContent>
          </Card>
        ) : (
          <div className="grid grid-cols-1 gap-6">
            {music.map((track) => (
              <Card key={track.id} className="hover:shadow-lg transition-shadow">
                <CardContent className="p-6">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-4">
                      <div className="p-3 bg-purple-100 rounded-lg">
                        <Music className="h-6 w-6 text-purple-600" />
                      </div>
                      <div>
                        <h3 className="text-lg font-semibold text-gray-900">{track.title}</h3>
                        <p className="text-gray-600">{track.artist}</p>
                        {track.albumName && (
                          <p className="text-sm text-gray-500">Album: {track.albumName}</p>
                        )}
                        <p className="text-sm text-gray-500">
                          Uploaded: {formatDate(track.uploadedDate)}
                        </p>
                        {track.ISRC_code && (
                          <p className="text-sm text-gray-500">ISRC: {track.ISRC_code}</p>
                        )}
                      </div>
                    </div>

                    <div className="flex items-center space-x-4">
                      <span className={`px-3 py-1 rounded-full text-sm font-medium ${getStatusColor(track.status.status)}`}>
                        {track.status.status}
                      </span>
                      
                      <div className="flex items-center space-x-2">
                        <a
                          href={track.fileUrl}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="p-2 text-gray-600 hover:text-purple-600 transition-colors"
                          title="View file"
                        >
                          <Eye className="h-4 w-4" />
                        </a>
                        <button
                          onClick={() => handleDownload(track.id)}
                          className="p-2 text-gray-600 hover:text-purple-600 transition-colors"
                          title="Download file"
                        >
                          <Download className="h-4 w-4" />
                        </button>
                      </div>
                    </div>
                  </div>

                  {track.notes && track.status.status === 'REJECTED' && (
                    <div className="mt-4 p-3 bg-red-50 border border-red-200 rounded-lg">
                      <p className="text-sm text-red-800">
                        <strong>Rejection reason:</strong> {track.notes}
                      </p>
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