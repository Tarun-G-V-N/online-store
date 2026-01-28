import React, { useRef, useState } from 'react'
import { useDispatch } from 'react-redux'
import { Spinner } from 'react-bootstrap'
import { searchByImage, setImageSearch } from '../../store/features/searchSlice'

const ImageSearch = () => {

    const [imageFile, setImageFile] = useState(null);
    const [imagePreview, setImagePreview] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const fileInputRef = useRef(null);
    const dispatch = useDispatch();

    const handleImageUpload = (event) => {
        const file = event.target.files[0];
        setImageFile(file);
        setImagePreview(URL.createObjectURL(file));
        dispatch(setImageSearch(file.name));
    }

    const handleDragOver = (event) => {
        event.preventDefault(event);
    }

    const handleDrop = (event) => {
        event.preventDefault();
        const file = event.dataTransfer.files[0];
        if(file && file.type.strtswith('image/')) {
            setImageFile(file);
            setImagePreview(URL.createObjectURL(file));
            dispatch(setImageSearch(file.name));
        }
    }

    const handleUploadClick = () => {
        fileInputRef.current.click();
    }

    const handleSearch = async (e) => {
        e.preventDefault();
        if(!imageFile) return;
        setIsLoading(true);
        try {
            await dispatch(searchByImage(imageFile)).unwrap();
        } catch (error) {
            
        } finally {
            setIsLoading(false);
        }
    }

  return (
    <div className='image-search-container'>
        <form onSubmit={handleSearch}>
            <div className='image-uploader' onDrag={handleDragOver} onDrop={handleDrop} onClick={handleUploadClick}>
                {imagePreview ? (<img src={imagePreview} alt='Preview' className='image-preview'/>) : (
                    <>
                        <svg xmlns='http://www.w3.org/2000/svg' width={40} height={40} viewBox='0 0 24 24' fill='none' stroke='currentColor' strokeWidth={2}>
                            <rect x={3} y={3} width={18} height={18} rx={2} ry={2}/>
                            <circle cx={8.5} cy={8.5} r={1.5}/>
                            <polyline points='21 15 16 10 5 21'/>
                        </svg>
                        <div>Drag an image here or{" "}<span style={{color: "yellow"}}>Choose an image to search for product.</span></div>
                    </>
                )}
                <input type="file" accept='image/*' onChange={handleImageUpload} style={{display: 'none'}} ref={fileInputRef}/>
            </div>
            <div className='mt-2 mb-4'>
                <button className='image-search-button' disabled={isLoading}>
                    {isLoading ? (
                        <>
                            {" "} <Spinner animation='border' size='sm' color='#a88c3d' isLoading={isLoading}/> Searching for similar Products...
                        </>
                    ) : ("Search")}
                </button>
            </div>
        </form>
    </div>
  )
}

export default ImageSearch