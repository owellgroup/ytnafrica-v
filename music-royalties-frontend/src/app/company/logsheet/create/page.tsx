'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { FileText, Music, Save, X } from 'lucide-react';
import toast from 'react-hot-toast';
import Layout from '@/components/Layout/Layout';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card';
import Button from '@/components/ui/Button';
import Input from '@/components/ui/Input';
import api from '@/lib/api';
import { getStoredUser } from '@/lib/auth';
import { ArtistWork } from '@/types';

const logSheetSchema = z.object({
  title: z.string().min(1, 'Title is required'),
});

type LogSheetForm = z.infer<typeof logSheetSchema>;

export default function CreateLogSheet() {
  const [selectedTracks, setSelectedTracks] = useState<ArtistWork[]>([]);
  const [availableMusic, setAvailableMusic] = useState<ArtistWork[]>([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const router = useRouter();
  const user = getStoredUser();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LogSheetForm>({
    resolver: zodResolver(logSheetSchema),
  });

  useEffect(() => {
    if (!user || user.role !== 'COMPANY') {
      router.push('/login');
      return;
    }

    fetchData();
  }, [user, router]);

  const fetchData = async () => {
    try {
      const response = await api.get('/api/company/approved-music');
      setAvailableMusic(response.data);

      // Check if there are pre-selected tracks from the music browser
      const preSelected = sessionStorage.getItem('selectedTracks');
      if (preSelected) {
        const selectedIds = JSON.parse(preSelected);
        const preSelectedTracks = response.data.filter((track: ArtistWork) =>
          selectedIds.includes(track.id)
        );
        setSelectedTracks(preSelectedTracks);
        sessionStorage.removeItem('selectedTracks');
      }
    } catch (error) {
      console.error('Error fetching music:', error);
    } finally {
      setLoading(false);
    }
  };

  const addTrack = (track: ArtistWork) => {
    if (!selectedTracks.find(t => t.id === track.id)) {
      setSelectedTracks([...selectedTracks, track]);
    }
  };

  const removeTrack = (trackId: number) => {
    setSelectedTracks(selectedTracks.filter(t => t.id !== trackId));
  };

  const onSubmit = async (data: LogSheetForm) => {
    if (selectedTracks.length === 0) {
      toast.error('Please select at least one track');
      return;
    }

    setSaving(true);
    try {
      const payload = {
        title: data.title,
        musicIds: selectedTracks.map(track => track.id),
      };

      await api.post('/api/company/logsheet', payload);
      toast.success('LogSheet created successfully!');
      router.push('/company/logsheets');
    } catch (error: any) {
      toast.error(error.response?.data || 'Failed to create logsheet');
    } finally {
      setSaving(false);
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
          <h1 className="text-3xl font-bold text-gray-900">Create LogSheet</h1>
          <p className="text-gray-600">Create a new music logsheet for tracking</p>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
          {/* LogSheet Form */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center space-x-2">
                <FileText className="h-5 w-5" />
                <span>LogSheet Details</span>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                <Input
                  {...register('title')}
                  label="LogSheet Title"
                  placeholder="e.g., Morning Show Playlist"
                  error={errors.title?.message}
                />

                <div className="space-y-4">
                  <h3 className="text-lg font-medium text-gray-900">
                    Selected Tracks ({selectedTracks.length})
                  </h3>
                  
                  {selectedTracks.length === 0 ? (
                    <div className="text-center py-8 text-gray-500">
                      <Music className="h-8 w-8 mx-auto mb-2 text-gray-400" />
                      <p>No tracks selected</p>
                      <p className="text-sm">Select tracks from the available music</p>
                    </div>
                  ) : (
                    <div className="space-y-2 max-h-64 overflow-y-auto">
                      {selectedTracks.map((track) => (
                        <div key={track.id} className="flex items-center justify-between p-3 bg-purple-50 rounded-lg">
                          <div className="flex items-center space-x-3">
                            <Music className="h-4 w-4 text-purple-600" />
                            <div>
                              <p className="font-medium text-gray-900">{track.title}</p>
                              <p className="text-sm text-gray-600">{track.artist}</p>
                            </div>
                          </div>
                          <button
                            type="button"
                            onClick={() => removeTrack(track.id)}
                            className="text-red-600 hover:text-red-800"
                          >
                            <X className="h-4 w-4" />
                          </button>
                        </div>
                      ))}
                    </div>
                  )}
                </div>

                <Button
                  type="submit"
                  loading={saving}
                  disabled={saving || selectedTracks.length === 0}
                  className="w-full flex items-center justify-center space-x-2"
                >
                  <Save className="h-4 w-4" />
                  <span>Create LogSheet</span>
                </Button>
              </form>
            </CardContent>
          </Card>

          {/* Available Music */}
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center space-x-2">
                <Music className="h-5 w-5" />
                <span>Available Music</span>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4 max-h-96 overflow-y-auto">
                {availableMusic
                  .filter(track => !selectedTracks.find(t => t.id === track.id))
                  .map((track) => (
                    <div key={track.id} className="flex items-center justify-between p-3 bg-gray-50 rounded-lg hover:bg-gray-100 transition-colors">
                      <div className="flex items-center space-x-3">
                        <Music className="h-4 w-4 text-gray-400" />
                        <div>
                          <p className="font-medium text-gray-900">{track.title}</p>
                          <p className="text-sm text-gray-600">{track.artist}</p>
                          {track.albumName && (
                            <p className="text-xs text-gray-500">{track.albumName}</p>
                          )}
                        </div>
                      </div>
                      <div className="flex items-center space-x-2">
                        <a
                          href={track.fileUrl}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="text-purple-600 hover:text-purple-800 text-sm"
                          onClick={(e) => e.stopPropagation()}
                        >
                          Play
                        </a>
                        <Button
                          size="sm"
                          variant="ghost"
                          onClick={(e) => {
                            e.stopPropagation();
                            addTrack(track);
                          }}
                        >
                          <Plus className="h-4 w-4" />
                        </Button>
                      </div>
                    </div>
                  ))}
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </Layout>
  );
}