import React, { useState } from 'react'
import {useDispatch} from 'react-redux'
import {toast, ToastContainer} from 'react-toastify'
import { Container, Form, Button } from 'react-bootstrap'
import { Link } from 'react-router-dom'
import { updateUserPassword } from '../../store/features/userSlice'

const ChangePassword = () => {

    const dispatch = useDispatch();
    const [errorMessage, setErrorMessage] =  useState(null);
    const [user, setUser] = useState({
        email: "", newPassword: "", confirmNewPassword: ""
    });

    const handleChange = (e) => {
        const {name, value} = e.target;
        setUser({...user, [name]: value});
        // Clear error message when user starts typing
        if(errorMessage) {
            setErrorMessage(null);
        }
    }

    const handlePasswordUpdate = async (e) => {
        e.preventDefault();
        if(!user.email || !user.newPassword || !user.confirmNewPassword) {
            toast.error("Please fill all the fields.");
            setErrorMessage("Invalid email or password");
            return;
        }

        if(user.newPassword !== user.confirmNewPassword) {
            toast.error("New Password and Confirm New Password do not match.");
            setErrorMessage("Password mismatch");
            return;
        }

        try {
            const result = await dispatch(updateUserPassword(user)).unwrap();
        } catch (error) {
            toast.error(error);
        }

        toast.success("Password updated successfully. Please login with your new password.");
        setUser({email: "", newPassword: "", confirmNewPassword: ""});
        setErrorMessage(null);
    }

  return (
    <Container className='d-flex justify-content-center align-items-center mt-5 mb-5'>
        <ToastContainer/>
        <Form className='border rounded shadow p-4' style={{width: "100%", maxWidth: "600px"}} onSubmit={handlePasswordUpdate}>
            <h3 className='text-center mb-4'>Change Password</h3>
            <Form.Group controlId='email'>
                <Form.Label>Email:</Form.Label>
                <Form.Control className='form-control' type='email' name='email' value={user.email} onChange={handleChange} placeholder='Enter your email' isInvalid={!!errorMessage}></Form.Control>
            </Form.Group>
            <Form.Group controlId='newpassword'>
                <Form.Label>New Password:</Form.Label>
                <Form.Control className='form-control' type='password' name='newPassword' value={user.newPassword} onChange={handleChange} placeholder='Enter new Password' isInvalid={!!errorMessage}></Form.Control>
            </Form.Group>
            <Form.Group controlId='confirmnewpassword'>
                <Form.Label>Confirm New Password:</Form.Label>
                <Form.Control className='form-control' type='password' name='confirmNewPassword' value={user.confirmNewPassword} onChange={handleChange} placeholder='Confirm your password' isInvalid={!!errorMessage}></Form.Control>
            </Form.Group>
            <Button variant='outline-primary' type='submit' className='w-100 mt-3'>Update Password</Button>
            <div className='text-center mt-4 mb-4'>
                Updated your password?{" "}<Link to={"/login"} style={{textDecoration: "none"}}>Login Here</Link>
            </div>
        </Form>
    </Container>
  )
}

export default ChangePassword