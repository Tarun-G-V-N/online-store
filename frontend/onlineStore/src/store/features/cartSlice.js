import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { api, securedApi } from "../../components/services/api";

export const addToCart = createAsyncThunk(
  "cart/addToCart",
  async ({productId, quantity}) => {
    console.log("Product ID: ", productId);
    console.log("Quantity: ", quantity);
    const response = await securedApi.post(`/cartItems/item/add?productId=${productId}&quantity=${quantity}`);
    return response.data;
  })

export const getUserCart = createAsyncThunk(
  "cart/getUserCart",
  async (userId, {rejectWithValue}) => {
    try {
      const response = await api.get(`/carts/user/${userId}/cart`);
      return response.data;
    } catch (error) {
      const status = error.response?.status;
      const message = error.response?.data || "Failed to fetch Cart";
      return rejectWithValue({status, message});
    }
  })

  export const updateQuantity = createAsyncThunk(
  "cart/updateQuantity",
  async ({cartId, productId, newQuantity}) => {
    await api.put(`/cartItems/cart/${cartId}/item/${productId}/update?quantity=${newQuantity}`);
    return {productId, newQuantity};
  })

  export const removeFromCart = createAsyncThunk(
    "cart/removeFromCart",
    async ({cartId, productId}) => {
      await api.delete(`/cartItems/cart/${cartId}/item/${productId}/remove`);
      return productId;
    })

const initialState = {
  cartId: null,
  items: [],
  totalAmount: 0,
  isLoading: true,
  successMessage: null,
  errorMessage: null,
};

const cartSlice = createSlice({
    name: "cart",
    initialState,
    reducers: {
      clearCart : (state) => {
        state.items = [];
        state.totalAmount = 0;
      },
      clearError: (state) => {
        state.errorMessage = null;
      }
    },
    extraReducers: (builder) => {
        builder.addCase(addToCart.fulfilled, (state, action) => {
            state.items.push(action.payload.data);
            state.successMessage = action.payload.message;
        }).addCase(addToCart.rejected, (state, action) => {
            state.errorMessage = action.error.message;
        }).addCase(getUserCart.fulfilled, (state, action) => {
            state.cartId = action.payload.data.id;
            state.items = action.payload.data.cartItems;
            state.totalAmount = action.payload.data.totalAmount;
            state.isLoading = false;
            state.errorMessage = null;
        }).addCase(getUserCart.rejected, (state, action) => {
            const {status, message} = action.payload;
            if(status === 404) {
              state.errorMessage = message;
            }
            else {
              state.errorMessage = message;
              state.isLoading = false;
            }
        }).addCase(updateQuantity.fulfilled, (state, action) => {
            const { productId, newQuantity } = action.payload;
            const item = state.items.find(item => item.product.id === productId);
            if (item) {
                item.quantity = newQuantity;
                item.totalPrice = item.product.price * newQuantity;
                state.totalAmount = state.items.reduce((total, item) => total + (item.product.price * item.quantity), 0);
            }
        }).addCase(removeFromCart.fulfilled, (state, action) => {
            const productId = action.payload;
            state.items = state.items.filter(item => item.product.id !== productId);
            state.totalAmount = state.items.reduce((total, item) => total + (item.product.price * item.quantity), 0);
        });
     }
});

export const {clearCart, clearError} = cartSlice.actions;
export default cartSlice.reducer;