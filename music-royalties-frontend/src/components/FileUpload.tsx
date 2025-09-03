import { useState, useRef } from 'react';
import { Upload, File, X } from 'lucide-react';
import Button from '@/components/ui/Button';
import { cn } from '@/lib/utils';

interface FileUploadProps {
  onFileSelect: (file: File) => void;
  accept: string;
  maxSize?: number;
  label: string;
  description?: string;
  currentFile?: string;
  onRemove?: () => void;
}

export default function FileUpload({
  onFileSelect,
  accept,
  maxSize = 50 * 1024 * 1024, // 50MB default
  label,
  description,
  currentFile,
  onRemove,
}: FileUploadProps) {
  const [dragActive, setDragActive] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  const handleDrag = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    if (e.type === 'dragenter' || e.type === 'dragover') {
      setDragActive(true);
    } else if (e.type === 'dragleave') {
      setDragActive(false);
    }
  };

  const handleDrop = (e: React.DragEvent) => {
    e.preventDefault();
    e.stopPropagation();
    setDragActive(false);

    if (e.dataTransfer.files && e.dataTransfer.files[0]) {
      handleFileSelection(e.dataTransfer.files[0]);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.preventDefault();
    if (e.target.files && e.target.files[0]) {
      handleFileSelection(e.target.files[0]);
    }
  };

  const handleFileSelection = (file: File) => {
    if (file.size > maxSize) {
      alert(`File size must be less than ${maxSize / (1024 * 1024)}MB`);
      return;
    }

    setSelectedFile(file);
    onFileSelect(file);
  };

  const onButtonClick = () => {
    inputRef.current?.click();
  };

  const removeFile = () => {
    setSelectedFile(null);
    if (inputRef.current) {
      inputRef.current.value = '';
    }
    onRemove?.();
  };

  return (
    <div className="space-y-2">
      <label className="block text-sm font-medium text-gray-700">{label}</label>
      
      {currentFile && !selectedFile ? (
        <div className="flex items-center justify-between p-4 bg-green-50 border border-green-200 rounded-lg">
          <div className="flex items-center space-x-2">
            <File className="h-5 w-5 text-green-600" />
            <span className="text-sm text-green-800">File uploaded</span>
          </div>
          <div className="flex items-center space-x-2">
            <a
              href={currentFile}
              target="_blank"
              rel="noopener noreferrer"
              className="text-sm text-purple-600 hover:text-purple-800"
            >
              View
            </a>
            {onRemove && (
              <button
                onClick={removeFile}
                className="text-red-600 hover:text-red-800"
              >
                <X className="h-4 w-4" />
              </button>
            )}
          </div>
        </div>
      ) : selectedFile ? (
        <div className="flex items-center justify-between p-4 bg-blue-50 border border-blue-200 rounded-lg">
          <div className="flex items-center space-x-2">
            <File className="h-5 w-5 text-blue-600" />
            <span className="text-sm text-blue-800">{selectedFile.name}</span>
          </div>
          <button
            onClick={removeFile}
            className="text-red-600 hover:text-red-800"
          >
            <X className="h-4 w-4" />
          </button>
        </div>
      ) : (
        <div
          className={cn(
            'relative border-2 border-dashed rounded-lg p-6 transition-colors duration-200',
            dragActive ? 'border-purple-400 bg-purple-50' : 'border-gray-300 hover:border-gray-400'
          )}
          onDragEnter={handleDrag}
          onDragLeave={handleDrag}
          onDragOver={handleDrag}
          onDrop={handleDrop}
        >
          <input
            ref={inputRef}
            type="file"
            className="absolute inset-0 w-full h-full opacity-0 cursor-pointer"
            onChange={handleChange}
            accept={accept}
          />
          
          <div className="text-center">
            <Upload className="mx-auto h-12 w-12 text-gray-400" />
            <div className="mt-4">
              <Button type="button" variant="secondary" onClick={onButtonClick}>
                Choose file
              </Button>
              <p className="mt-2 text-sm text-gray-600">or drag and drop</p>
            </div>
            {description && (
              <p className="mt-2 text-xs text-gray-500">{description}</p>
            )}
          </div>
        </div>
      )}
    </div>
  );
}