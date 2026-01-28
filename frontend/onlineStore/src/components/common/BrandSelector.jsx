import React, {useEffect} from 'react'
import{useDispatch, useSelector} from 'react-redux'
import {getAllBrands, addBrand} from '../../store/features/productSlice'

const BrandSelector = ({selectedBrand, onBrandChange, newBrand, showNewBrandInput, setNewBrand, setShowNewBrandInput}) => {

    const dispatch = useDispatch();
    const brands = useSelector(state => state.product.brands);

    const handleAddBrand = () => {
        if(newBrand !== "") {
            dispatch(addBrand(newBrand));
            onBrandChange(newBrand);
            setNewBrand("");
            setShowNewBrandInput(false);
            console.log("on Brand Component:", brands);
        }
    }

    const handleBrandChange = (e) => {
        if(e.target.value === "New") {
            setShowNewBrandInput(true);
        } else {
            onBrandChange(e.target.value);
        }
    }

    const handleNewBrandChange = (e) => {
        setNewBrand(e.target.value);
    }

    useEffect(() => {
        dispatch(getAllBrands());
    }, [dispatch]);

  return (
    <div className='mb-3'>
        <label className='form-label'>Brands: </label>
        <select className='form-select'  required value={selectedBrand} onChange={handleBrandChange}>
            <option value="">All Brands</option>
            <option value="New">Add New Brand</option>
            {brands.map((brand, index) => (<option key={index} value={brand}>{brand}</option>))}
        </select>
        {showNewBrandInput && (
            <div className='input-group'>
                <input type='text' className='form-control' placeholder='Enter new Brand' value={newBrand} onChange={handleNewBrandChange}/>
                <button className='btn btn-secondary btn-sm' type='button' onClick={handleAddBrand}>Add new Brand</button>
            </div>
        )}
    </div>
  )
}

export default BrandSelector