import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import{ getUserById } from '../../store/features/userSlice'
import { useParams } from 'react-router-dom';
import { Container, Row, Col, Card, ListGroup } from 'react-bootstrap'
import { Link } from 'react-router-dom';
import { FaTrash, FaEdit, FaPlus } from 'react-icons/fa'
import LoadSpinner from '../common/LoadSpinner';
import placeHolder from '../../assets/images/placeholder.jpeg'
import { nanoid } from 'nanoid';
import { toast, ToastContainer } from 'react-toastify'
import AddressForm from '../common/AddressForm';
import { addNewUserAddress, updateUserAddress, deleteUserAddress, setUserAddresses } from '../../store/features/userSlice';

const UserProfile = () => {

  const dispatch = useDispatch();
  const {userId} = useParams();
  const user = useSelector((state) => state.user.user);
  const [isEditing, setIsEditing] = useState(false);
  const [editingAddressId, setEditingAddressId] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [newAddress, setNewAddress] = useState([
          {street: "", city: "", state: "", country: "", addressType: "", phoneNumber: ""}
      ]);

  const handleInputChange = (e) => {
    const {name, value} = e.target;
    setNewAddress((prevState) => ({
      ...prevState,
      [name]: value
    }))
  }

  const resetForm = () => {
      setNewAddress([{street: "", city: "", state: "", country: "", addressType: "", phoneNumber: ""}]);
      setShowForm(false);
      setIsEditing(false);
      setEditingAddressId(null);
    }

  const handleEditClick = (address) => {
    setIsEditing(true);
    setEditingAddressId(address.id);
    setShowForm(true);
    setNewAddress(address);
  }

  const handleAddAddress = async () => {
    const updatedAddresses = [...user.addresses, {...newAddress, id: nanoid()}];
    dispatch(setUserAddresses(updatedAddresses));
    try {
      const response = await dispatch(addNewUserAddress({userId, addressList: [newAddress]})).unwrap();
      toast.success(response.message);
      resetForm();
    } catch (error) {
      toast.error(error.message);
      dispatch(setUserAddresses(user.addresses));
    }
  }

  const handleUpdateAddress = async (id) => {
    const updatedAddresses = user.addresses.map((address) => address.id === id ? {...newAddress, id} : address);
    dispatch(setUserAddresses(updatedAddresses));
    try {
      const response = await dispatch(updateUserAddress({id, newAddress})).unwrap();
      toast.success(response.message);
      resetForm();
    } catch (error) {
      toast.error(error.message);
      dispatch(setUserAddresses(user.addresses));
    }
  }

  const handleDeleteAddress = async (id) => {
    const updatedAddresses = user.addresses.filter((address) => address.id !== id);
    dispatch(setUserAddresses(updatedAddresses));
    try {
      const response = await dispatch(deleteUserAddress(id)).unwrap();
      toast.success(response.message);
    } catch (error) {
      toast.error(error.message);
      dispatch(setUserAddresses(user.addresses));
    }
  }

  useEffect(() => {
    if(userId) {
      const fetchUser = async () => {
        try {
          await dispatch(getUserById(userId)).unwrap();
        } catch (error) {
          toast.error(error.message);
        }
      }
      fetchUser();
    }
  }, [dispatch, userId]);

  return (
    <Container className='mt-5 mb-5'>
      <ToastContainer />
      <h2 className='cart-title'>User Dashboard</h2>
      {user ? (
        <>
          <Row>
            <Col md={4}>
              <Card>
                <Card.Header>User Information</Card.Header>
                <Card.Body className='text-center'>
                  <div className='mb-3'>
                    <img src={/*user.photo || */placeHolder} alt="User Photo" style={{height: "100px", width: "100px"}} className='image-fluid rounded-circle'/>
                  </div>
                  <Card.Text>{" "}<strong>Full Name: </strong>{user.firstname}{" "}{user.lastname}</Card.Text>
                  <Card.Text>{" "}<strong>Email: </strong>{user.email}</Card.Text>
                </Card.Body>
              </Card>
            </Col>

            <Col md={8}>
              <Card className='mb-4'>
                <Card.Header>User Addresses</Card.Header>
                <ListGroup variant='flush'>
                  {user.addresses && user.addresses.length > 0 ? (
                    user.addresses.map((address) => (
                      <ListGroup.Item key={address.id}>
                        <Card className='p-2 mb-2 shadow'>
                          <Card.Body>
                            <Card.Text>{address.addressType} ADDRESS:{" "}</Card.Text>
                            <hr />
                            <Card.Text>{address.street}, {address.city}, {" "}{address.state}, {address.country}</Card.Text>
                          </Card.Body>
                          <div className='d-flex gap-4'>
                            <Link onClick={() => {handleDeleteAddress(address.id)}}><span className='text-danger'><FaTrash/></span></Link>
                            <Link variant='primary'><span className='text-info' onClick={() => handleEditClick(address)}><FaEdit/></span></Link>
                          </div>
                        </Card>
                      </ListGroup.Item>
                    ))
                  ) : (
                    <p>No Addresses Found</p>
                  )}
                </ListGroup>
                <Link className='ms-2 mb-2' variant='success' onClick={() => {setShowForm(true); setIsEditing(false);}}><FaPlus/></Link>
                {showForm && <AddressForm address={newAddress} onChange={handleInputChange} onSubmit={isEditing ? () => handleUpdateAddress(editingAddressId) : handleAddAddress} isEditing={isEditing} onCancel={resetForm} showButtons={true} showCheck={true} showTitle={true}/>}
              </Card>
            </Col>
          </Row>
        </>
      ) : (
        <LoadSpinner/>
      )}
    </Container>
  )
}

export default UserProfile