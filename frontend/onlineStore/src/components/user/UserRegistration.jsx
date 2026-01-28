import React, { useEffect, useState } from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {getCountryNames, registerUser} from '../../store/features/userSlice'
import { Container, Form, Row, Col, Button } from 'react-bootstrap'
import {toast, ToastContainer} from 'react-toastify'
import { Link, useNavigate } from 'react-router-dom'
import AddressForm from '../common/AddressForm'

const UserRegistration = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const [user, setUser] = useState({
        firstName: "", lastName: "", email: "", password: ""
    });

    const [addresses, setAddresses] = useState([
        {street: "", city: "", state: "", country: "", addressType: "HOME", phoneNumber: ""}
    ]);

    const handleUserChange = (e) => {
        const {name, value} = e.target;
        setUser({...user, [name]: value});
    }

    const handleAddressChange = (e, index) => {
        const {name, value} = e.target;
        const updatedAddresses = [...addresses];
        updatedAddresses[index] = {...updatedAddresses[index], [name]: value};
        setAddresses(updatedAddresses);
    }

    const addAddress = () => {
        setAddresses([...addresses, {street: "", city: "", state: "", country: "", addressType: "HOME", phoneNumber: ""}])
    }

    const deleteAddress = (index) => {
        const updatedAddresses = addresses.filter((_, i) => i != index);
        setAddresses(updatedAddresses);
    }

    const resetForm = () => {
        setUser({firstName: "", lastName: "", email: "", password: ""});
        setAddresses([{street: "", city: "", state: "", country: "", addressType: "HOME"}]);
    }

    const handleUserRegistration = async (e) => {
        e.preventDefault();
        try {
            const result = await dispatch(registerUser({user, addresses})).unwrap();
            resetForm();
            toast.success(result.message);
            setTimeout(() => {navigate('/login')}, 3000)
        } catch (error) {
            toast.error(error.message);
        }
    }

  return (
    <Container className='d-flex justify-content-center align-items-center mt-5 mb-5'>
        <ToastContainer/>
        <Form className='border rounded shadow p-4' style={{width: "100%", maxWidth: "600px"}} onSubmit={handleUserRegistration}>
            <h3 className='text-center mb-4'>User Registration</h3>
            <Row>
                <Col md={6}>
                    <Form.Group controlId='firstname'>
                        <Form.Label>First Name:</Form.Label>
                        <Form.Control className='form-control' type='text' name='firstName' value={user.firstName} onChange={handleUserChange}></Form.Control>
                    </Form.Group>
                </Col>
                <Col md={6}>
                    <Form.Group controlId='lastname'>
                        <Form.Label>Last Name:</Form.Label>
                        <Form.Control className='form-control' type='text' name='lastName' value={user.lastName} onChange={handleUserChange}></Form.Control>
                    </Form.Group>
                </Col>
            </Row>
            <Form.Group controlId='email'>
                <Form.Label>Email:</Form.Label>
                <Form.Control className='form-control' type='email' name='email' value={user.email} onChange={handleUserChange}></Form.Control>
            </Form.Group>
            <Form.Group controlId='password'>
                <Form.Label>Password:</Form.Label>
                <Form.Control className='form-control' type='password' name='password' value={user.password} onChange={handleUserChange}></Form.Control>
            </Form.Group>

            <h4 className='mt-4'>Addresses</h4>
            {addresses.map((address, index) => (
                <div className='border rounded p-3 mb-3' key={index}>
                    <h4>Address {index + 1}:</h4>
                    <AddressForm address={address} onChange={(e) => handleAddressChange(e, index)} onCancel={() => deleteAddress(index)} showButtons={true}/>
                </div>
            ))}
            <div className='d-flex gap-4 mt-2 mb-2'>
                <Button variant='primary' size='sm' onClick={addAddress}>Add Address</Button>
                <Button variant='success' size='sm' type='submit'>Register</Button>
            </div>
            <div className='text-center mt-4 mb-4'>
                Have an account already?{" "}<Link to={"/login"} style={{textDecoration: "none"}}>Login Here</Link>
            </div>
        </Form>
    </Container>
  )
}

export default UserRegistration