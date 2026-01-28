import React, { useState, useEffect } from 'react'
import { useDispatch } from 'react-redux';
import { getCountryNames } from '../../store/features/userSlice';
import { Form, Row, Col } from 'react-bootstrap'
import { FaCheck, FaTimes } from 'react-icons/fa'

const AddressForm = ({address, onChange, onSubmit, isEditing, onCancel, showButtons, showCheck, showTitle}) => {

    const dispatch = useDispatch();
    const [countries, setCountries] = useState([]);

    useEffect(() => {
        const fetchCountries = async () => {
            const result = await dispatch(getCountryNames()).unwrap();
            setCountries(result);
        }
        fetchCountries();
    }, [dispatch])


  return (
    <div className='p-4 m-4 border'>
        {showTitle && <h5>{isEditing ? "Edit Address" : "Add new Address"}</h5>}
         <Form.Group className='mb-2'>
            <Form.Label>Street:</Form.Label>
            <Form.Control type='text' name='street' placeholder='street' value={address.street} required onChange={onChange}></Form.Control>
        </Form.Group>
        <Form.Group className='mb-2'>
            <Form.Label>City:</Form.Label>
            <Form.Control type='text' name='city' placeholder='city' value={address.city} required onChange={onChange}></Form.Control>
        </Form.Group>
        <Form.Group className='mb-2'>
            <Form.Label>State:</Form.Label>
            <Form.Control type='text' name='state' placeholder='state' value={address.state} required onChange={onChange}></Form.Control>
        </Form.Group>
        <Row>
            <Col md={6}>
                <Form.Group className='mb-2'>
                    <Form.Label>Country:</Form.Label>
                    <Form.Control as='select' name='country' value={address.country} required onChange={onChange}>
                        <option value="">Select a Country</option>
                        {countries.map((country, index) => (
                            <option value={country.code} key={index}>{country.name}</option>
                        ))}
                    </Form.Control>
                </Form.Group>
            </Col>
            <Col md={6}>
                <Form.Group className='mb-2'>
                    <Form.Label>Phone Number:</Form.Label>
                    <Form.Control type='text' name='phoneNumber' placeholder='Phone Number' value={address.phoneNumber} required onChange={onChange}></Form.Control>
                </Form.Group>        
            </Col>
        </Row>
        <Form.Group className='mb-2'>
            <Form.Label>Address Type:</Form.Label>
            <Form.Control as='select' name='addressType' value={address.addressType} onChange={onChange}>
                <option value="HOME">Home</option>
                <option value="WORK">Work</option>
                <option value="OTHER">Other</option>
            </Form.Control>
        </Form.Group>
        {showButtons && (
            <div className='d-flex gap-4 mt-3'>
                {showCheck && (
                    <div onClick={onSubmit} style={{cursor: "pointer", color: "green"}}>
                        <FaCheck size={24} title={isEditing ? "Update Address" : "Add Address"}/>
                    </div>
                )}
                <div onClick={onCancel} style={{cursor: "pointer", color: "red"}}>
                    <FaTimes size={24} title='Cancel'/>
                </div>
            </div>
        )}
        
    </div>
  )
}

export default AddressForm