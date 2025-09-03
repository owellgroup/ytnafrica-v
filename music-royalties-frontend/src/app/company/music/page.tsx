'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Music, Search, Play, Plus } from 'lucide-react';
import Layout from '@/components/Layout/Layout';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card';
import Button from '@/components/ui/Button';
import Input from '@/components/ui/Input';
import api from '@/lib/api';
import { getStoredUser } from '@/lib/auth';
import { ArtistWork } from '@/types';
import { formatDate } from '@/lib/utils';

export default function CompanyMusic() {
  const [music, setMusic] = useState<ArtistWork[]>([]);
  const [filteredMusic, setFilteredMusic] = useState<ArtistWork[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedTracks, setSelectedTracks] = useState<number[]>([]);
  const [loading, setLoading] = useState(true);
  const router = useRouter();
  const user = getStoredUser();

  useEffect(() => {
    if (!user || user.role !== 'COMPANY') {
      router.push('/login');
      return;
    }

    fetchMusic();
  }, [user, router]);

  useEffect(() => {
    const filtered = music.filter(track =>
      track.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      track.artist.toLowerCase().includes(searchTerm.toLowerCase()) ||
      (track.albumName && track.albumName.toLowerCase().includes(searchTerm.toLowerCase()))
    );
    setFilteredMusic(filtered);
  }, [music, searchTerm]);

  const fetchMusic = async () => {
    try {
      const response = await api.get('/api/company/approved-music');
      setMusic(response.data);
      setFilteredMusic(response.data);
    } catch (error) {
      console.error('Error fetching music:', error);
    } finally {
      setLoading(false);
    }
  };

  const toggleTrackSelection = (trackId: number) => {
    setSelectedTracks(prev =>
      prev.includes(trackId)
        ? prev.filter(id => id !== trackId)
        : [...prev, trackId]
    );
  };

  const createLogSheetWithSelected = () => {
    if (selectedTracks.length === 0) {
      alert('Please select at least one track');
      return;
    }

    // Store selected tracks in sessionStorage and navigate to create logsheet
    sessionStorage.setItem('selectedTracks', JSON.stringify(selectedTracks));
    router.push('/company/logsheet/create');
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
            <h1 className="text-3xl font-bold text-gray-900">Browse Music</h1>
            <p className="text-gray-600">Select approved music for your logsheets</p>
          </div>
          {selectedTracks.length > 0 && (
            <Button
              onClick={createLogSheetWithSelected}
              className="flex items-center space-x-2"
            >
              <Plus className="h-4 w-4" />
              <span>Create LogSheet ({selectedTracks.length})</span>
            </Button>
          )}
        </div>

        {/* Search */}
        <Card>
          <CardContent className="p-6">
            <div className="relative">
              <Search className="absolute left-3 top-3 h-5 w-5 text-gray-400" />
              <Input
                placeholder="Search by title, artist, or album..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </CardContent>
        </Card>

        {/* Music Grid */}
        {filteredMusic.length === 0 ? (
          <Card>
            <CardContent className="text-center py-12">
              <Music className="h-12 w-12 text-gray-400 mx-auto mb-4" />
              <h3 className="text-lg font-medium text-gray-900 mb-2">
                {searchTerm ? 'No music found' : 'No approved music available'}
              </h3>
              <p className="text-gray-600">
                {searchTerm 
                  ? 'Try adjusting your search terms'
                  : 'Check back later for newly approved tracks'
                }
              </p>
            </CardContent>
          </Card>
        ) : (
          <div className="grid grid-cols-1 gap-4">
            {filteredMusic.map((track) => (
              <Card 
                key={track.id} 
                className={`hover:shadow-lg transition-all cursor-pointer ${
                  selectedTracks.includes(track.id) ? 'ring-2 ring-purple-500 bg-purple-50' : ''
                }`}
                onClick={() => toggleTrackSelection(track.id)}
              >
                <CardContent className="p-6">
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-4">
                      <div className={`p-3 rounded-lg ${
                        selectedTracks.includes(track.id) ? 'bg-purple-200' : 'bg-purple-100'
                      }`}>
                        <Music className={`h-6 w-6 ${
                          selectedTracks.includes(track.id) ? 'text-purple-700' : 'text-purple-600'
                        }`} />
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

                    <div className="flex items-center space-x-2">
                      <a
                        href={track.fileUrl}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="p-2 text-gray-600 hover:text-purple-600 transition-colors"
                        title="Play track"
                        onClick={(e) => e.stopPropagation()}
                      >
                        <Play className="h-5 w-5" />
                      </a>
                      
                      <div className={`w-6 h-6 rounded-full border-2 flex items-center justify-center ${
                        selectedTracks.includes(track.id)
                          ? 'bg-purple-600 border-purple-600'
                          : 'border-gray-300'
                      }`}>
                        {selectedTracks.includes(track.id) && (
                          <div className="w-2 h-2 bg-white rounded-full"></div>
                        )}
                      </div>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        )}

        {/* Recent LogSheets */}
        {logSheets.length > 0 && (
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <CardTitle>Recent LogSheets</CardTitle>
                <Button
                  variant="ghost"
                  onClick={() => router.push('/company/logsheets')}
                >
                  View All
                </Button>
              </div>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {logSheets.slice(0, 3).map((logSheet) => (
                  <div key={logSheet.id} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
                    <div className="flex items-center space-x-3">
                      <FileText className="h-5 w-5 text-gray-400" />
                      <div>
                        <h4 className="font-medium text-gray-900">{logSheet.title}</h4>
                        <p className="text-sm text-gray-500">
                          Created: {formatDate(logSheet.createdDate)}
                        </p>
                        <p className="text-sm text-gray-500">
                          {logSheet.selectedMusic.length} tracks
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
              </div>
            </CardContent>
          </Card>
        )}
      </div>
    </Layout>
  );
}