import React from 'react'
import { useDispatch, useSelector } from 'react-redux';
import { Card } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import ProductImage from '../utils/ProductImage';
import StockStatus from '../utils/StockStatus';
import {deleteProduct} from '../../store/features/productSlice'
import {toast, ToastContainer} from 'react-toastify';

const ProductCard = ({products}) => {

  const dispatch = useDispatch();
  const userRoles = useSelector((state) => state.auth.roles);

  const handleDelete = async (productId) => {
    try {
      const result = await dispatch(deleteProduct(productId)).unwrap();
      toast.success(result.message);
    } catch (error) {
      toast.error(error.message);
    }
  }

  return (
    <main className="row m-2">
        {products.map(product => (
          <div className='col-12 col-sm-6 col-md-4 col-lg-2' key={product.id}>
            <Card className='mt-2 mb-2'>
                <Link to={`/product/${product.id}/details`} className="link">
                  <div className="image-container">
                      {product.images.length > 0  && (<ProductImage productId={product.images[0].id}/>)}
                  </div>
                </Link>
                <Card.Body>
                <p className='product-description'>{product.name} - {product.description}</p>
                <h4 className='price'>₹{product.price}</h4>
                <StockStatus inventory={product.inventory}/>
                <div className='d-flex gap-2'>
                  {userRoles.includes("ROLE_ADMIN") && (
                    <>
                      <Link to={`/update-product/${product.id}/update`} className="link">Edit</Link>
                      <Link to={`/delete-product/${product.id}/delete`} className="link" onClick={() => {handleDelete(product.id)}}>Delete</Link>
                    </>
                  )}
                  <button className='shop-now-button'>Add to Cart</button>
                </div>
                </Card.Body>
            </Card>
          </div>
        ) ) }
      </main>
  )
}

export default ProductCard