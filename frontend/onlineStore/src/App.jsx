import { Route, RouterProvider, createBrowserRouter, createRoutesFromElements } from "react-router-dom"
import { RootLayout } from "./components/layout/RootLayout"
import Home from "./components/home/Home"
import Products from "./components/product/Products"
import ProductDetails from "./components/product/ProductDetails"
import Cart from "./components/cart/Cart"
import Order from "./components/order/Order"
import AddProduct from "./components/product/AddProduct"
import ProductUpdate from "./components/product/ProductUpdate"
import UserRegistration from "./components/user/UserRegistration"
import 'react-toastify/dist/ReactToastify.css';
import Login from "./components/auth/Login"
import ChangePassword from "./components/user/ChangePassword"
import ProtectedRoute from "./components/auth/ProtectedRoute"
import Unauthorized from "./components/Unauthorized"
import UserProfile from "./components/user/UserProfile"
import Checkout from "./components/checkout/Checkout"

function App() {

  const router = createBrowserRouter(
    createRoutesFromElements(
      <Route path="/" element={<RootLayout/>}>
        <Route index element={<Home/>}/>
        <Route path="/products" element={<Products/>}/>
        <Route path="/products/:name" element={<Products/>}/>
        <Route path="/products/category/:categoryId/products" element={<Products/>}/>
        <Route path="/product/:productId/details" element={<ProductDetails/>}/>
        <Route path="/register" element={<UserRegistration/>}/>
        <Route path="/login" element={<Login/>}/>
        <Route path="/forgot-password" element={<ChangePassword/>}/>
        <Route path="/unauthorized" element={<Unauthorized/>}/>

        <Route element={<ProtectedRoute useOutlet={true} allowedRoles={["ROLE_ADMIN", "ROLE_USER"]}/>}>
          <Route path="/user/:userId/my-cart" element={<Cart/>}/>
          <Route path="/user/:userId/my-orders" element={<Order/>}/>
          <Route path="/user-profile/:userId/profile" element={<UserProfile/>}/>
          <Route path="/checkout/:userId/checkout" element={<Checkout/>}/>
        </Route>

        <Route element={<ProtectedRoute useOutlet={true} allowedRoles={["ROLE_ADMIN"]}/>}>
          <Route path="/add-product" element={<AddProduct/>}/>
          <Route path="/update-product/:productId/update" element={<ProductUpdate/>}/>
        </Route>
      </Route>
    )
  )
  
  return (
    <>
      <RouterProvider router={router}/>
    </>
  )
}

export default App
