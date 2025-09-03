'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Building, Save } from 'lucide-react';
import toast from 'react-hot-toast';
import Layout from '@/components/Layout/Layout';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/Card';
import Button from '@/components/ui/Button';
import Input from '@/components/ui/Input';
import api from '@/lib/api';
import { getStoredUser } from '@/lib/auth';

const companySchema = z.object({
  email: z.string().email('Invalid email address'),
  password: z.string().min(6, 'Password must be at least 6 characters'),
  companyName: z.string().min(1, 'Company name is required'),
  companyAddress: z.string().min(1, 'Company address is required'),
  companyPhone: z.string().min(1, 'Company phone is required'),
  contactPerson: z.string().min(1, 'Contact person is required'),
});

type CompanyForm = z.infer<typeof companySchema>;

export default function CreateCompany() {
  const [saving, setSaving] = useState(false);
  const router = useRouter();
  const user = getStoredUser();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<CompanyForm>({
    resolver: zodResolver(companySchema),
  });

  useEffect(() => {
    if (!user || user.role !== 'ADMIN') {
      router.push('/login');
      return;
    }
  }, [user, router]);

  const onSubmit = async (data: CompanyForm) => {
    setSaving(true);
    try {
      await api.post('/api/admin/company/create', data);
      toast.success('Company created successfully!');
      router.push('/admin/companies');
    } catch (error: any) {
      toast.error(error.response?.data || 'Failed to create company');
    } finally {
      setSaving(false);
    }
  };

  return (
    <Layout>
      <div className="space-y-8">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-bold text-gray-900">Create Company</h1>
          <p className="text-gray-600">Add a new radio/TV company to the system</p>
        </div>

        <div className="max-w-2xl mx-auto">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center space-x-2">
                <Building className="h-5 w-5" />
                <span>Company Information</span>
              </CardTitle>
            </CardHeader>
            <CardContent>
              <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                <div className="space-y-4">
                  <h3 className="text-lg font-medium text-gray-900">Account Details</h3>
                  
                  <Input
                    {...register('email')}
                    label="Email Address"
                    type="email"
                    placeholder="company@example.com"
                    error={errors.email?.message}
                  />

                  <Input
                    {...register('password')}
                    label="Password"
                    type="password"
                    placeholder="Create a secure password"
                    error={errors.password?.message}
                  />
                </div>

                <div className="space-y-4">
                  <h3 className="text-lg font-medium text-gray-900">Company Details</h3>
                  
                  <Input
                    {...register('companyName')}
                    label="Company Name"
                    placeholder="Radio Station XYZ"
                    error={errors.companyName?.message}
                  />

                  <Input
                    {...register('companyAddress')}
                    label="Company Address"
                    placeholder="123 Main Street, City, Country"
                    error={errors.companyAddress?.message}
                  />

                  <Input
                    {...register('companyPhone')}
                    label="Company Phone"
                    placeholder="+1234567890"
                    error={errors.companyPhone?.message}
                  />

                  <Input
                    {...register('contactPerson')}
                    label="Contact Person"
                    placeholder="John Manager"
                    error={errors.contactPerson?.message}
                  />
                </div>

                <div className="flex items-center space-x-4">
                  <Button
                    type="button"
                    variant="secondary"
                    onClick={() => router.back()}
                    className="flex-1"
                  >
                    Cancel
                  </Button>
                  <Button
                    type="submit"
                    loading={saving}
                    disabled={saving}
                    className="flex-1 flex items-center justify-center space-x-2"
                  >
                    <Save className="h-4 w-4" />
                    <span>Create Company</span>
                  </Button>
                </div>
              </form>
            </CardContent>
          </Card>
        </div>
      </div>
    </Layout>
  );
}