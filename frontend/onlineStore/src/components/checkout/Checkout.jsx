import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { useParams, useNavigate } from 'react-router-dom';
import { useStripe, useElements, CardElement } from '@stripe/react-stripe-js'
import { getUserCart, clearCart } from '../../store/features/cartSlice'
import { createPaymentIntent, placeOrder } from '../../store/features/orderSlice'
import { toast, ToastContainer } from 'react-toastify'
import { Container, Row, Col, Form, FormGroup, Card, Button } from 'react-bootstrap';
import AddressForm from '../common/AddressForm'
import { cardElementOptions } from '../utils/cardElementOptions';
import { ClipLoader } from 'react-spinners';

const Checkout = () => {

    const {userId} = useParams();
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const cart = useSelector((state) => state.cart);
    const [userInfo, setUserInfo] = useState({firstName: "", lastName: "", email: ""});
    const [billingAddress, setBillingAddress] = useState({street: "", city: "", state: "", country: "", postalCode: ""});
    const stripe = useStripe();
    const elements = useElements();
    const [cardError, setCardError] = useState("");
    const [isLoading, setIsLoading] = useState(false);

    const handleInputChange = (event) => {
        const {name, value} = event.target;
        setUserInfo({...userInfo, [name]: value});
    }

    const handleAddressChange = (event) => {
        const {name, value} = event.target;
        setBillingAddress({...billingAddress, [name]: value});
    }

    const handlePaymentAndOrder = async (e) => {
      e.preventDefault();
      setIsLoading(true);
      //check the stripe presence
      if(!stripe || !elements) {
        toast.error("Loading..Please try again after sometime");
        return;
      }
      console.log("Initiating Payment.");
      const cardElement = elements.getElement(CardElement);
      try {
        //create payment intent through backend
        const {clientSecret} = await dispatch(createPaymentIntent({amount: cart.totalAmount, currency: "inr"})).unwrap();
        console.log("Received the Client Secret.");
        //confirm the payment intent with card details
        const { error, paymentIntent } = await stripe.confirmCardPayment(
          clientSecret,
          {
            payment_method: {
              card: cardElement,
              billing_details: {
                name: `${userInfo.firstName} ${userInfo.lastName}`,
                email: userInfo.email,
                address: {
                  line1: billingAddress.street,
                  city: billingAddress.city,
                  state: billingAddress.state,
                  country: billingAddress.country,
                  postal_code: billingAddress.postalCode,
                }
              }
            }
          }
        )
        console.log("Payment Completed.");
        //place the order after successful payment
        if(error) {
          toast.error(error.message); return;
        }
        if(paymentIntent.status === 'succeeded') {
          const result = await dispatch(placeOrder(userId)).unwrap();
          console.log(result);
          toast.success("Payment Successful!! Your order has been placed.");
          setTimeout(() => {
            window.location.href = `/user/${userId}/my-orders`;
          }, 5000);
        }

      } catch (error) {
        toast.error("Error while processing payment: ", error.message);
      } finally {
        setIsLoading(false);
      }
    }

    useEffect(() => {
        dispatch(getUserCart(userId));
    }, [dispatch, userId])

  return (
    <Container className='mt-5 mb-5'>
      <ToastContainer />
      <div className='d-flex justify-content-center'>
        <Row>
          <Col md={8}>
            <Form className='p-4 border rounded shadow-sm'>
              <Row>
                <Col md={6}>
                  <FormGroup>
                    <label htmlFor="firstname">FirstName: </label>
                    <input type="text" name='firstName' id='name' className='form-control mb-2' value={userInfo.firstName} onChange={handleInputChange} />
                  </FormGroup>
                </Col>
                <Col md={6}>
                  <FormGroup>
                    <label htmlFor="lastname">LastName: </label>
                    <input type="text" name='lastName' id='name' className='form-control mb-2' value={userInfo.lastName} onChange={handleInputChange} />
                  </FormGroup>
                </Col>
              </Row>
              <FormGroup>
                <label htmlFor="email">Email: </label>
                <input type="email" name='email' id='email' className='form-control mb-2' value={userInfo.email} onChange={handleInputChange} />
              </FormGroup>
              <div>
                <h6>Enter Billing Address</h6>
                <AddressForm address={billingAddress} onChange={handleAddressChange}/>
              </div>
              <div className='form-group'>
                <label htmlFor="card-element" className='form-label'><h6>Debit or Credit card</h6></label>
                <div id='card-element' className='form-control'>
                  <CardElement options={cardElementOptions} onChange={(event) => {setCardError(event.error ? event.error.message : "")}}/>
                  {cardError && <div className='text-danger'>{cardError}</div>}
                </div>
              </div>
            </Form>
          </Col>
          <Col md={4}>
            <h6 className='mt-6 text-center cart-title'>Your Order Summary</h6>
            <hr />
            <Card style={{background: "whiteSmoke"}}>
              <Card.Body>
                <Card.Title className='mt-2 text-muted text-success'>Total Amount: ₹{cart.totalAmount.toFixed(2)}</Card.Title>
              </Card.Body>
              <Button type='submit' className='btn btn-warning mt-3' disabled={!stripe} onClick={(e) => handlePaymentAndOrder(e)}>
                {isLoading ? <ClipLoader size={20} color='' /> : "Pay Now"}
              </Button>
            </Card>
          </Col>
        </Row>
      </div>
    </Container>
  )
}

export default Checkout