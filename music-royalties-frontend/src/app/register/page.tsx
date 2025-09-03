'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Music, Mail, Lock, UserPlus } from 'lucide-react';
import toast from 'react-hot-toast';
import api from '@/lib/api';
import Button from '@/components/ui/Button';
import Input from '@/components/ui/Input';

const registerSchema = z.object({
  email: z.string().email('Invalid email address'),
  password: z.string().min(6, 'Password must be at least 6 characters'),
  confirmPassword: z.string(),
}).refine((data) => data.password === data.confirmPassword, {
  message: "Passwords don't match",
  path: ["confirmPassword"],
});

type RegisterForm = z.infer<typeof registerSchema>;

export default function RegisterPage() {
  const [loading, setLoading] = useState(false);
  const router = useRouter();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterForm>({
    resolver: zodResolver(registerSchema),
  });

  const onSubmit = async (data: RegisterForm) => {
    setLoading(true);
    try {
      await api.post('/api/auth/register/artist', {
        email: data.email,
        password: data.password,
      });
      
      toast.success('Registration successful! Please check your email for verification.');
      router.push('/login');
    } catch (error: any) {
      toast.error(error.response?.data || 'Registration failed');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-50 to-indigo-100 flex items-center justify-center p-4">
      <div className="max-w-md w-full space-y-8">
        <div className="text-center">
          <div className="flex justify-center">
            <div className="p-3 bg-purple-600 rounded-full">
              <Music className="h-8 w-8 text-white" />
            </div>
          </div>
          <h2 className="mt-6 text-3xl font-bold text-gray-900">Join YTN Africa</h2>
          <p className="mt-2 text-sm text-gray-600">
            Create your artist account and start collecting royalties
          </p>
        </div>

        <div className="bg-white py-8 px-6 shadow-xl rounded-xl">
          <form className="space-y-6" onSubmit={handleSubmit(onSubmit)}>
            <div className="relative">
              <Mail className="absolute left-3 top-9 h-5 w-5 text-gray-400" />
              <Input
                {...register('email')}
                label="Email address"
                type="email"
                placeholder="Enter your email"
                className="pl-10"
                error={errors.email?.message}
              />
            </div>

            <div className="relative">
              <Lock className="absolute left-3 top-9 h-5 w-5 text-gray-400" />
              <Input
                {...register('password')}
                label="Password"
                type="password"
                placeholder="Create a password"
                className="pl-10"
                error={errors.password?.message}
              />
            </div>

            <div className="relative">
              <Lock className="absolute left-3 top-9 h-5 w-5 text-gray-400" />
              <Input
                {...register('confirmPassword')}
                label="Confirm Password"
                type="password"
                placeholder="Confirm your password"
                className="pl-10"
                error={errors.confirmPassword?.message}
              />
            </div>

            <Button
              type="submit"
              className="w-full flex items-center justify-center space-x-2"
              loading={loading}
              disabled={loading}
            >
              <UserPlus className="h-5 w-5" />
              <span>Create Account</span>
            </Button>
          </form>

          <div className="mt-6">
            <div className="relative">
              <div className="absolute inset-0 flex items-center">
                <div className="w-full border-t border-gray-300" />
              </div>
              <div className="relative flex justify-center text-sm">
                <span className="px-2 bg-white text-gray-500">Already have an account?</span>
              </div>
            </div>

            <div className="mt-6 text-center">
              <Link
                href="/login"
                className="text-purple-600 hover:text-purple-500 font-medium"
              >
                Sign in to your account
              </Link>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}