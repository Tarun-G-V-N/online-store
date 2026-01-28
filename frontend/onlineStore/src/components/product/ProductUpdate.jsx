import React, {useState, useEffect} from 'react'
import{useDispatch, useSelector} from 'react-redux'
import { useParams, Link } from 'react-router-dom';
import { getProductById, updateProduct } from '../../store/features/productSlice';
import {deleteProductImage} from '../../store/features/imageSlice'
import LoadSpinner from '../common/LoadSpinner';
import {toast, ToastContainer} from 'react-toastify';
import BrandSelector from '../common/BrandSelector'
import CategorySelector from '../common/CategorySelector'
import ProductImage from '../utils/ProductImage';
import ImageUpdater from '../image/ImageUpdater';


const ProductUpdate = () => {
     
    const {productId} = useParams();
    const dispatch = useDispatch();
    const [showNewBrandInput, setShowNewBrandInput] = useState(false);
    const [showNewCategoryInput, setShowNewCategoryInput] = useState(false);
    const [newBrand, setNewBrand] = useState("");
    const [newCategory, setNewCategory] = useState("");
    const [isLoading, setIsLoading] = useState(false);
    const [showImageModal, setShowImageModal] = useState(false);
    const [selectedImageId, setSelectedImageId] = useState(null);
    const newProductImages = useSelector((state) => state.image.uploadedImages);

    const [updatedProduct, setUpdatedProduct] = useState({
        name: "",
        description: "",
        price: 0,
        brand: "",
        category: "",
        inventory:"",
        images: []
    });

    useEffect(() => {
        const fetchProduct = async () => {
            setIsLoading(true);
            try {
                const result = await dispatch(getProductById(productId)).unwrap();
                setUpdatedProduct(result.data);
            } catch (error) {
                toast.error(error.message);
            } finally {
                setTimeout(() => {
                    setIsLoading(false);
                }, 1000);
            }
        };
        fetchProduct();
    }, [dispatch, productId, newProductImages]);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setUpdatedProduct(prevState => ({
            ...prevState,
            [name]: value
        }));
    }

    const handleCategoryChange = (category) => {
        setUpdatedProduct({...updatedProduct, category});
        if(category === "New") {
            setShowNewCategoryInput(true);
        } else {
            setShowNewCategoryInput(false);
        }
    }

    const handleBrandChange = (brand) => {
        setUpdatedProduct({...updatedProduct, brand});
        if(brand === "New") {
            setShowNewBrandInput(true);
        } else {
            setShowNewBrandInput(false);
        }
    }

    const handleUpdateProduct = async (e) => {
        e.preventDefault();
        try {
            console.log(updatedProduct);
            const result = await dispatch(updateProduct({productId, updatedProduct})).unwrap();
            toast.success(result.message);
        } catch (error) {
            toast.error(error.message);
        }
    }

    const handleDeleteImage = async (imageId) => {
        try {
            const result = await dispatch(deleteProductImage(imageId)).unwrap();
            setUpdatedProduct((previousProduct) => ({
                ...previousProduct,
                images: previousProduct.images.filter((image) => image.id !== imageId)
            }))
            toast.success(result.message);
        } catch (error) {
            toast.error(error.message);
        }
    }

    const handleImageUpdate = () => {
        dispatch(getProductById(productId));
    };

    const handleEditImage = (imageId) => {
        setSelectedImageId(imageId);
        setShowImageModal(true);
    };

    const handleAddImage = () => {
        setSelectedImageId(null);
        setShowImageModal(true);
    };

    const handleCloseImageModal = () => {
        setSelectedImageId(null);
        setShowImageModal(false);
    };

    if(isLoading) {
        return <LoadSpinner/>;
    }

  return (
    <div className='container mt-5 mb-5'>
        <ToastContainer/>
        <div className='row'>
            <div className='col-md-6 me-4'>
                <h4 className='mb-4'>Update Product</h4>
                <form onSubmit={handleUpdateProduct}>
                    <div className='mb-3'>
                        <label className='form-label' htmlFor='name'>Name: </label>
                        <input type='text' id='name' className='form-control' name='name' value={updatedProduct.name} onChange={handleChange} required/>
                    </div>
                    <div className='mb-3'>
                        <label className='form-label' htmlFor='price'>Price: </label>
                        <input type='number' id='price' className='form-control' name='price' value={updatedProduct.price} onChange={handleChange} required/>
                    </div>
                    <div className='mb-3'>
                        <label className='form-label' htmlFor='inventory'>Inventory: </label>
                        <input type='number' id='inventory' className='form-control' name='inventory' value={updatedProduct.inventory} onChange={handleChange} required/>
                    </div>
                    <div className='mb-3'>
                        <BrandSelector selectedBrand={updatedProduct.brand} onBrandChange={handleBrandChange} newBrand={newBrand} showNewBrandInput={showNewBrandInput} setNewBrand={setNewBrand} setShowNewBrandInput={setShowNewBrandInput}/>
                    </div>
                    <div className='mb-3'>
                        <CategorySelector SelectedCategory={updatedProduct.category.name} onCategoryChange={handleCategoryChange} newCategory={newCategory} showNewCategoryInput={showNewCategoryInput} setNewCategory={setNewCategory} setShowNewCategoryInput={setShowNewCategoryInput}/>
                    </div>
                    <div className='mb-3'>
                        <label className='form-label' htmlFor='description'>Description: </label>
                        <textarea id='description' className='form-control' name='description' value={updatedProduct.description} onChange={handleChange} required/>
                    </div>
                    <button type='submit' className='btn btn-secondary btn-sm'>Save Product Update</button>
                </form>
            </div>

            <div className='col-md-3'>
                <table className='table table-bordered text-center'>
                    <tbody>
                        {updatedProduct.images.map((image, index) => (
                            <tr key={index}>
                                <td className='update-image-container'>
                                    <ProductImage productId={image.id}/>
                                    <div className='d-flex gap-4 mt-2 mb-2'>
                                        <Link to={"#"} onClick={() => {handleEditImage(image.id)}}>Edit</Link>
                                        <Link to={"#"} onClick={() => {handleDeleteImage(image.id)}}>Remove</Link>
                                    </div>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                <Link to={"#"} onClick={handleAddImage}>Add more images</Link>
            </div>
        </div>
        <ImageUpdater show={showImageModal} handleClose={handleCloseImageModal} productId={productId} selectedImageId={selectedImageId}/>
    </div>
  )
}

export default ProductUpdate