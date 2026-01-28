import React, { useEffect, useState } from 'react'
import {useDispatch, useSelector} from 'react-redux'
import {useLocation, useNavigate, Link} from 'react-router-dom'
import {toast, ToastContainer} from 'react-toastify'
import {Container, Row, Col, Card, Form, InputGroup, Button, Modal} from 'react-bootstrap'
import {BsLockFill, BsPersonFill} from 'react-icons/bs'
import {login} from '../../store/features/authSlice'

const Login = () => {
    const[credentials, setCredentials] = useState({email: "", password: ""});
    const [errorMessage, setErrorMessage] =  useState(null);
    const [show, setShow] = useState(false);
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const location = useLocation();
    const isAuthenticated = useSelector((state) => state.auth.isAuthenticated);
    // const errorMessage = useSelector((state) => state.auth.errorMessage);
    const from = location.state?.from?.pathname || "/";

    useEffect(() => {
        if(isAuthenticated) {
            navigate(from, {replace: true})
            window.location.reload();
        }
    }, [isAuthenticated, navigate, from]);

    useEffect(() => {
        setShow(true);
    }, []);

    const handleClosePopUp = () => {
        setShow(false);
    }

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setCredentials((prevState) => ({
            ...prevState,
            [name]: value
        }))
    }

    const handleLogin = async (e) => {
        e.preventDefault();
        if(!credentials.email || !credentials.password) {
            toast.error("Please provide your email and password.");
            setErrorMessage("Invalid email or password");
            return;
        }
        try {
            const result = await dispatch(login(credentials)).unwrap();
        } catch (error) {
            toast.error(error);
        }
    }

  return (
    <>
        <Container className='mt-5 mb-5'>
            <ToastContainer/>
            <Row className='justify-content-center'>
                <Col xs={12} sm={10} md={8} lg={6} xl={6}>
                    <Card>
                        <Card.Body>
                            <Card.Title className='text-center mb-4'>Login</Card.Title>
                            <Form onSubmit={handleLogin}>
                                <Form.Group className='mb-3' controlId='username'>
                                    <Form.Label>Email:</Form.Label>
                                    <InputGroup>
                                        <InputGroup.Text><BsPersonFill/></InputGroup.Text>
                                        <Form.Control type='text' name='email' onChange={handleInputChange} value={credentials.email} placeholder='Enter your email address' isInvalid={!!errorMessage}/>
                                    </InputGroup>
                                </Form.Group>
                                <Form.Group className='mb-3' controlId='password'>
                                    <Form.Label>Password:</Form.Label>
                                    <InputGroup>
                                        <InputGroup.Text><BsLockFill/></InputGroup.Text>
                                        <Form.Control type='password' name='password' onChange={handleInputChange} value={credentials.password} placeholder='Enter your password' isInvalid={!!errorMessage}/>
                                    </InputGroup>
                                </Form.Group>
                                <Button variant='outline-primary' type='submit' className='w-100'>Login</Button>
                            </Form>
                            <div className='text-center mt-4 mb-4'>
                                Don't have an account yet? {" "} <Link to={"/register"} style={{textDecoration: "none"}}>Register Here</Link>
                            </div>
                            <div className='text-center mt-4 mb-4'>
                                Forgot your password? {" "} <Link to={"/forgot-password"} style={{textDecoration: "none"}}>Reset Here</Link>
                            </div>
                        </Card.Body>
                    </Card>
                </Col>
            </Row>
        </Container>

      <Modal show={show} onHide={handleClosePopUp} centered>
        <Modal.Header closeButton>
          <Modal.Title>Welcome!</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>Please login using below admin credentials if you want to update the Product Inventory. Else, please register yourself as a normal user to shop the products. </p>
          <p>Happy Shopping!!</p>
          <p>Email: admin1@gmail.com</p>
          <p>Password: 123456</p>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClosePopUp}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  )
}

export default Login