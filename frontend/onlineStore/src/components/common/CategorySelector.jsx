import React, {useEffect} from 'react'
import{useDispatch, useSelector} from 'react-redux'
import {getAllProductCategories, addCategory} from '../../store/features/categorySlice'

const CategorySelector = ({SelectedCategory, onCategoryChange, newCategory, showNewCategoryInput, setNewCategory, setShowNewCategoryInput}) => {

    const dispatch = useDispatch();
    const categories = useSelector(state => state.category.categories);

    const handleAddCategory = () => {
            if(newCategory !== "") {
                dispatch(addCategory(newCategory));
                onCategoryChange(newCategory);
                setNewCategory("");
                setShowNewCategoryInput(false);
            }
            console.log("on Category Component:", categories);
        }
    
        const handleCategoryChange = (e) => {
            if(e.target.value === "New") {
                setShowNewCategoryInput(true);
            } else {
                onCategoryChange(e.target.value);
            }
        }
    
        const handleNewCategoryChange = (e) => {
            setNewCategory(e.target.value);
        }
    
        useEffect(() => {
            dispatch(getAllProductCategories());
        }, [dispatch]);

  return (
    <div className='mb-3'>
        <label className='form-label'>Categories: </label>
        <select className='form-select'  required value={SelectedCategory} onChange={handleCategoryChange}>
            <option value="">All Categories</option>
            <option value="New">Add New Category</option>
            {categories.map((category, index) => (<option key={index} value={category.name}>{category.name}</option>))}
        </select>
        {showNewCategoryInput && (
            <div className='input-group'>
                <input type='text' className='form-control' placeholder='Enter new Category' value={newCategory} onChange={handleNewCategoryChange}/>
                <button className='btn btn-secondary btn-sm' type='button' onClick={handleAddCategory}>Add new Category</button>
            </div>
        )}
    </div>
  )
}

export default CategorySelector