'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { Music, Users, Building, Shield, Upload, CheckCircle } from 'lucide-react';
import Layout from '@/components/Layout/Layout';
import Button from '@/components/ui/Button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card';
import { getStoredUser } from '@/lib/auth';

export default function HomePage() {
  const router = useRouter();
  const user = getStoredUser();

  useEffect(() => {
    if (user) {
      switch (user.role) {
        case 'ARTIST':
          router.push('/artist/dashboard');
          break;
        case 'COMPANY':
          router.push('/company/dashboard');
          break;
        case 'ADMIN':
          router.push('/admin/dashboard');
          break;
      }
    }
  }, [user, router]);

  const features = [
    {
      icon: Users,
      title: 'Artist Management',
      description: 'Complete artist registration and profile management system',
    },
    {
      icon: Upload,
      title: 'File Upload System',
      description: 'Secure upload for documents, photos, and music files',
    },
    {
      icon: Building,
      title: 'Company Operations',
      description: 'Logsheet creation and music selection for radio/TV stations',
    },
    {
      icon: Shield,
      title: 'Admin Control',
      description: 'Comprehensive approval workflow and user management',
    },
    {
      icon: CheckCircle,
      title: 'Approval Workflow',
      description: 'Streamlined approval process with email notifications',
    },
    {
      icon: Music,
      title: 'Music Royalties',
      description: 'Track and manage music royalties collection efficiently',
    },
  ];

  if (user) {
    return null; // Will redirect based on role
  }

  return (
    <Layout>
      <div className="space-y-16">
        {/* Hero Section */}
        <section className="text-center space-y-8">
          <div className="space-y-4">
            <h1 className="text-4xl md:text-6xl font-bold text-gray-900">
              Music Royalties
              <span className="block text-purple-600">Collection System</span>
            </h1>
            <p className="text-xl text-gray-600 max-w-3xl mx-auto">
              Comprehensive platform for managing music royalties collection from radio and TV channels.
              Streamlined workflows for artists, companies, and administrators.
            </p>
          </div>
          
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link href="/register">
              <Button size="lg" className="w-full sm:w-auto">
                Get Started as Artist
              </Button>
            </Link>
            <Link href="/login">
              <Button variant="secondary" size="lg" className="w-full sm:w-auto">
                Login to Your Account
              </Button>
            </Link>
          </div>
        </section>

        {/* Features Grid */}
        <section className="space-y-8">
          <div className="text-center">
            <h2 className="text-3xl font-bold text-gray-900">Platform Features</h2>
            <p className="mt-4 text-lg text-gray-600">
              Everything you need to manage music royalties efficiently
            </p>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {features.map((feature, index) => (
              <Card key={index} className="hover:shadow-lg transition-shadow duration-300">
                <CardHeader>
                  <div className="flex items-center space-x-3">
                    <div className="p-2 bg-purple-100 rounded-lg">
                      <feature.icon className="h-6 w-6 text-purple-600" />
                    </div>
                    <CardTitle>{feature.title}</CardTitle>
                  </div>
                </CardHeader>
                <CardContent>
                  <p className="text-gray-600">{feature.description}</p>
                </CardContent>
              </Card>
            ))}
          </div>
        </section>

        {/* How It Works */}
        <section className="space-y-8">
          <div className="text-center">
            <h2 className="text-3xl font-bold text-gray-900">How It Works</h2>
            <p className="mt-4 text-lg text-gray-600">
              Simple steps to get started with royalties collection
            </p>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="text-center space-y-4">
              <div className="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mx-auto">
                <span className="text-2xl font-bold text-purple-600">1</span>
              </div>
              <h3 className="text-xl font-semibold text-gray-900">Register & Verify</h3>
              <p className="text-gray-600">
                Create your artist account and complete email verification
              </p>
            </div>
            
            <div className="text-center space-y-4">
              <div className="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mx-auto">
                <span className="text-2xl font-bold text-purple-600">2</span>
              </div>
              <h3 className="text-xl font-semibold text-gray-900">Complete Profile</h3>
              <p className="text-gray-600">
                Fill out your profile and upload required documents for approval
              </p>
            </div>
            
            <div className="text-center space-y-4">
              <div className="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mx-auto">
                <span className="text-2xl font-bold text-purple-600">3</span>
              </div>
              <h3 className="text-xl font-semibold text-gray-900">Upload Music</h3>
              <p className="text-gray-600">
                Upload your music files and start earning royalties
              </p>
            </div>
          </div>
        </section>

        {/* CTA Section */}
        <section className="bg-gradient-to-r from-purple-600 to-indigo-600 rounded-2xl p-8 md:p-12 text-center text-white">
          <h2 className="text-3xl md:text-4xl font-bold mb-4">
            Ready to Start Collecting Royalties?
          </h2>
          <p className="text-xl mb-8 opacity-90">
            Join thousands of artists already using our platform
          </p>
          <Link href="/register">
            <Button size="lg" className="bg-white text-purple-600 hover:bg-gray-100">
              Register Now
            </Button>
          </Link>
        </section>
      </div>
    </Layout>
  );
}