import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { api, securedApi } from "../../components/services/api"

export const placeOrder = createAsyncThunk(
    "orders/placeOrder",
    async (userId) => {
        const response = await api.post(`/orders/user/${userId}/place-order`);
        return response.data;
    }
); 

export const getUserOrders = createAsyncThunk(
    "orders/getUserOrders",
    async (userId) => {
        const response = await api.get(`/orders/user/${userId}/orders`);
        return response.data;
    }
); 

export const createPaymentIntent = createAsyncThunk(
    "orders/createPaymentIntent",
    async ({amount, currency}) => {
        const response = await securedApi.post("/orders/create-payment-intent", {amount, currency});
        return response.data;
    }
)


const initialState = {
    orders: [],
    successMessage: null,
    errorMessage: null,
    loading: false,
};

const orderSlice = createSlice({
    name: "order",
    initialState,
    reducers: {},
    extraReducers: (builder) => {builder
        .addCase(placeOrder.fulfilled, (state, action) => {
            state.loading = false;
            state.successMessage = action.payload.message;
            state.orders.push(action.payload.data);
        }).addCase(getUserOrders.fulfilled, (state, action) => {
            state.loading = false;
            state.orders = action.payload.data;
        });
    }
});

export default orderSlice.reducer;