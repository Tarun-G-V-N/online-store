import React, { use, useEffect, useState } from 'react'
import { useSelector, useDispatch } from 'react-redux';
import { getAllProductCategories } from '../../store/features/categorySlice';
import { setSearchQuery, setSelectedCategory, clearFilters } from '../../store/features/searchSlice';
import { useParams, useNavigate } from 'react-router-dom';
import ImageSearch from './ImageSearch';
import UploadIcon from '../../assets/images/upload.jpeg';

const SearchBar = () => {

  const {categoryId} = useParams();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const categories = useSelector((state) => state.category.categories);
  const {searchQuery, selectedCategory} = useSelector((state) => state.search);
  const [showImageSearch, setShowImageSearch] = useState(false);

  useEffect(() => {
    if(categoryId && categories.length > 0) {
      const category = categories.find((category) => category.id === parseInt(categoryId, 10));
      if(category) {
        dispatch(setSelectedCategory(category.name));
      }else {
        dispatch(setSelectedCategory("all"));
      }
    }
   }, [categoryId, categories, dispatch]);

  const handleSearchQueryChange = (e) => {
    dispatch(setSearchQuery(e.target.value));
  }

  const handleCategoryChange = (e) => {
    dispatch(setSelectedCategory(e.target.value));
  }

  const handleClearFilters = () => {
    dispatch(clearFilters());
    // navigate('/products');
    setShowImageSearch(false);
  }

  useEffect(() => {
    dispatch(getAllProductCategories());
  }, [dispatch])

  return (
    <>
      <div className='search-bar input-group input-group-sm'>
        <select value={selectedCategory} onChange={handleCategoryChange} className='form-control-sm'>
            <option value="all">All Categories</option>
            {categories.map((category, index) => (
              <option key={index} value={category.name}>{category.name}</option>
            ))}
        </select>
        <input type="text" className='form-control' value={searchQuery} onChange={handleSearchQueryChange} placeholder='search for a product...'/>
        <img src={UploadIcon} alt="image search" className='search-image-icon' onClick={() => setShowImageSearch((prev) => !prev)}/>
        <button className='search-button' onClick={handleClearFilters}>clear filter</button>
      </div>
      {showImageSearch && <ImageSearch/>}
    </>
  )
}

export default SearchBar