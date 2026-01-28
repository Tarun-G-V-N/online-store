import React, {useState} from 'react'
import{useDispatch, useSelector} from 'react-redux'
import {addNewProduct} from '../../store/features/productSlice'
import {toast, ToastContainer} from 'react-toastify'
import BrandSelector from '../common/BrandSelector'
import CategorySelector from '../common/CategorySelector'
import {Stepper, Step, StepLabel} from '@mui/material'
import ImageUploader from '../common/ImageUploader'

const AddProduct = () => {

    const dispatch = useDispatch();
    const [showNewBrandInput, setShowNewBrandInput] = useState(false);
    const [showNewCategoryInput, setShowNewCategoryInput] = useState(false);
    const [newBrand, setNewBrand] = useState("");
    const [newCategory, setNewCategory] = useState("");
    const [activeStep, setActiveStep] = useState(0);
    const steps = ["Add Product Details", "Upload Product Images"];
    const [productId, setProductId] = useState(null);
    const [product, setProduct] = useState({
        name: "",
        description: "",
        price: "",
        brand: "",
        category: "",
        inventory:"",
    });

    const handleChange = (e) => {
        const {name, value} = e.target;
        setProduct(prevState => ({
            ...prevState,
            [name]: value
        }));
    }

    const handleCategoryChange = (category) => {
        setProduct({...product, category});
        if(category === "New") {
            setShowNewCategoryInput(true);
        } else {
            setShowNewCategoryInput(false);
        }
    }

    const handleBrandChange = (brand) => {
        setProduct({...product, brand});
        if(brand === "New") {
            setShowNewBrandInput(true);
        } else {
            setShowNewBrandInput(false);
        }
    }


    const handleAddNewProduct = async (e) => {
        e.preventDefault();
        try {
            const result = await dispatch(addNewProduct(product)).unwrap();
            setProductId(result.data.id);
            toast.success(result.message);
            resetForm();
            setActiveStep(1);
        } catch (error) {
            toast.error(error.message);
        }
    }

    const handlePreviousStep = () => {
        setActiveStep(0);
    }

    const resetForm = () => {
        setProduct({
            name: "",
            description: "",
            price: "",
            brand: "",
            category: "",
            inventory:"",
        });
        setNewBrand("");
        setNewCategory("");
        setShowNewBrandInput(false);
        setShowNewCategoryInput(false);
    }

  return (
    <section className='container mt-5 mb-5'>
        <ToastContainer/>
        <div className='d-flex justify-content-center'>
            <div className='col-md-6 col-xs-12'>
                <h4>Add New Product</h4>
                <Stepper activeStep={activeStep} className='mb-4'>
                    {steps.map((label) => (
                        <Step key={label}>
                            <StepLabel>{label}</StepLabel>
                        </Step>
                    ))}
                </Stepper>
                <div>
                    {activeStep === 0 && (
                        <form onSubmit={handleAddNewProduct}>
                            <div className='mb-3'>
                                <label className='form-label' htmlFor='name'>Name: </label>
                                <input type='text' id='name' className='form-control' name='name' value={product.name} onChange={handleChange} required/>
                            </div>
                            <div className='mb-3'>
                                <label className='form-label' htmlFor='price'>Price: </label>
                                <input type='number' id='price' className='form-control' name='price' value={product.price} onChange={handleChange} required/>
                            </div>
                            <div className='mb-3'>
                                <label className='form-label' htmlFor='inventory'>Inventory: </label>
                                <input type='number' id='inventory' className='form-control' name='inventory' value={product.inventory} onChange={handleChange} required/>
                            </div>
                            <div className='mb-3'>
                                <BrandSelector selectedBrand={product.brand} onBrandChange={handleBrandChange} newBrand={newBrand} showNewBrandInput={showNewBrandInput} setNewBrand={setNewBrand} setShowNewBrandInput={setShowNewBrandInput}/>
                            </div>
                            <div className='mb-3'>
                                <CategorySelector SelectedCategory={product.category} onCategoryChange={handleCategoryChange} newCategory={newCategory} showNewCategoryInput={showNewCategoryInput} setNewCategory={setNewCategory} setShowNewCategoryInput={setShowNewCategoryInput}/>
                            </div>
                            <div className='mb-3'>
                                <label className='form-label' htmlFor='description'>Description: </label>
                                <textarea id='description' className='form-control' name='description' value={product.description} onChange={handleChange} required/>
                            </div>
                            <button type='submit' className='btn btn-secondary btn-sm'>Save Product</button>
                        </form>
                    )}
                    {activeStep === 1 && (
                        <div className='container'>
                            <ImageUploader productId={productId}/>
                            <button className='btn btn-secondary btn-sm mt-3' onClick={handlePreviousStep}>Add Another Product</button>
                        </div>
                    ) }
                </div>
            </div>
        </div>
    </section>
  )
}

export default AddProduct