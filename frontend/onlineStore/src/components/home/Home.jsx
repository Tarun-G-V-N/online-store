import React, {useEffect, useState} from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { Link } from 'react-router-dom';
import Card from 'react-bootstrap/Card'
import Hero from '../hero/Hero'
import Paginator from '../common/Paginator';
import LoadSpinner from '../common/LoadSpinner';
import {toast, ToastContainer} from 'react-toastify';
import { setTotalItems } from '../../store/features/paginationSlice';
import { getDistinctProductsByName } from '../../store/features/productSlice';
import StockStatus from '../utils/StockStatus';
import ProductImage from '../utils/ProductImage';

const Home = () => {
  const dispatch = useDispatch();
  const [filteredProducts, setFilteredProducts] = useState([]);
  const {searchQuery, selectedCategory, imageSearchResults} = useSelector((state) => state.search);
  const distinctProducts = useSelector((state) => state.product.distinctProducts);
  const isLoading = useSelector((state) => state.product.isLoading);
  const {currentPage, itemsPerPage, totalItems} = useSelector((state) => state.pagination);
  const [errormessage, setErrorMessage] = useState(null);

  useEffect(() => {
    dispatch(getDistinctProductsByName());
  }, [dispatch]);

  useEffect(() => {
    const results = distinctProducts.filter((product) =>{
      const matchesQuery = product.name.toLowerCase().includes(searchQuery.toLowerCase());
      const matchesCategory = selectedCategory === "all" || product.category.name.toLowerCase().includes(selectedCategory.toLowerCase());
      const matchesByImage = imageSearchResults.length > 0 ? imageSearchResults.some((imageResults) => product.name.toLowerCase().includes(imageResults.name.toLowerCase())) : true;
      return matchesQuery && matchesCategory && matchesByImage;
    });
    setFilteredProducts(results);
  }, [searchQuery, selectedCategory, imageSearchResults, distinctProducts, dispatch]);

  useEffect(() => {
    dispatch(setTotalItems(filteredProducts.length));
  }, [dispatch, filteredProducts]);

  const indexOfLastItem = currentPage * itemsPerPage;
  const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  const currentProducts = filteredProducts.slice(indexOfFirstItem, indexOfLastItem);

  if (isLoading) {
    return <div>
      <LoadSpinner/>
    </div>;
  }
  
  return (
    <>
      <Hero/>
      <div className="d-flex flex-wrap justify-content-center p-5">
        <ToastContainer/>
        {currentProducts && currentProducts.map(product => (
          <Card key={product.id} className="home-product-card">
            <Link to={`products/${product.name}`} className="link">
              <div className="image-container">
                {product.images.length > 0  && (<ProductImage productId={product.images[0].id}/>)}
              </div>
            </Link>
            <Card.Body>
              <p className='product-description'>{product.name} - {product.description}</p>
              <h4 className='price'>₹{product.price}</h4>
              <StockStatus inventory={product.inventory}/>
              <Link to={`products/${product.name}`} className='shop-now-button'>{" "}Shop now</Link>
            </Card.Body>
          </Card>
        ) ) }
      </div>
      <Paginator/>
    </>
  )
}

export default Home