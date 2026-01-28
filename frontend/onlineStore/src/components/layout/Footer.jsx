import React, {useEffect} from 'react'
import { Link } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { getAllProductCategories } from '../../store/features/categorySlice';
import { FaFacebookF, FaTwitter, FaInstagram } from 'react-icons/fa';

const Footer = () => {

  const dispatch = useDispatch();
  const categories = useSelector((state) => state.category.categories);

   useEffect(() => {
      dispatch(getAllProductCategories());
    }, [dispatch])

  return (
    <footer className='mega-footer'>
        <div className='footer-container'>
            <div className='footer-section'>
                <h3>About Us</h3>
                <p>This is a prototype of an e-commerce application where you can search for and order the products you want to buy.</p>
            </div>
            <div className='footer-section'>
                <h3>Category</h3>
                <ul>
                    {categories.map((category, index) => (
                      <li key={index}><Link to={`/products/category/${category.id}/products`}>{category.name}</Link></li>
                    ))}
                </ul>
            </div>
            <div className='footer-section'>
                <h3>Contact Us</h3>
                <p>Email: tarun@gmail.com</p>
                <p>Phone: 1234567890</p>
            </div>
            <div className='footer-section'>
                <h3>Follow Us</h3>
                <div className='social-icons'>
                    <a href="https://www.facebook.com/" target='_blank' rel='noopener noreferrer'><FaFacebookF/></a>
                    <a href="https://www.twitter.com/" target='_blank' rel='noopener noreferrer'><FaTwitter/></a>
                    <a href="https://www.instagram.com/" target='_blank' rel='noopener noreferrer'><FaInstagram/></a>
                </div>
            </div>
            <div className='footer-section'>
                <p>&copy; 2025 onlinestore.com. All rights reserved.</p>
            </div>
        </div>
    </footer>
  )
}

export default Footer