import React, {useState, useRef} from 'react'
import { nanoid } from '@reduxjs/toolkit';
import {useDispatch, useSelector} from 'react-redux'
import {uploadImages} from '../../store/features/imageSlice'
import { toast, ToastContainer } from 'react-toastify';
import { Link } from 'react-router-dom';
import {BsPlus, BsDash} from 'react-icons/bs'
import { ClipLoader } from 'react-spinners';

const ImageUploader = ({productId}) => {

    const dispatch = useDispatch();
    const fileInputRef = useRef([]);
    const [images, setImages] = useState([]);
    const [imageInputs, setImageInputs] = useState([{id: nanoid()}]);
    const [isLoading, setIsLoading] = useState(false);
    
    const handleImageChange = (e) => {
        const files = Array.from(e.target.files);
        const newImages = files.map(file => ({
            id: nanoid(),
            name: file.name,
            file,
        }));
        setImages(prevImages => [...prevImages, ...newImages]);
    }

    const handleAddImageInput = () => {
        setImageInputs(prevImageInputs => [...prevImageInputs, {id: nanoid()}]);
    }

    const handleRemoveImageInput = (id) => {
        setImageInputs(prevImageInputs => prevImageInputs.filter(input => input.id !== id));
    }

    const handleUploadImages = async (e) => {
        e.preventDefault();
        if(!productId) return;
        if(Array.isArray(images) && images.length > 0) {
            setIsLoading(true);
            const files = images.map(img => img.file);
            try {
                const result = await dispatch(uploadImages({productId, files})).unwrap();
                toast.success(result.message);
                clearFileInputs();
            } catch (error) {
                toast.error(error.message);
            }
            setIsLoading(false);
        }
    }

    const clearFileInputs = () => {
        fileInputRef.current.forEach(input => {
            if(input) input.value = null;
        });
    }

  return (
    <form onSubmit={handleUploadImages}>
        <div className='mt-4'>
            <h5>Upload Product Images</h5>
            <Link to={"#"} onClick={handleAddImageInput}><BsPlus className='icon'/>Add more images</Link>
            <div className='mt-2 mb-2'>
                {imageInputs.map((input, index) => (
                    <div key={input.id} className='d-flex align-items-center mb-2 input-group'>
                        <input type="file" accept='image/*' multiple onChange={handleImageChange} className='me-2 form-control' ref={(el) => fileInputRef.current[index] = el}/>
                        <button type='button' className='btn btn-danger' onClick={() => handleRemoveImageInput(input.id)}><BsDash/></button>
                    </div>
                ))}
            </div>
            {imageInputs.length > 0 && (<button type='submit' className='btn btn-primary btn-sm'> {isLoading ? <ClipLoader size={15} color='' /> : "Upload Images"} </button>)}
        </div>
    </form>
  )
}

export default ImageUploader