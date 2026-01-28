import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { api } from "../../components/services/api"
import axios from "axios";

export const getUserById = createAsyncThunk(
    "user/getUserById",
    async (userId) => {
        const response = await api.get(`users/user/${userId}`);
        return response.data;
    }
)

export const registerUser = createAsyncThunk(
    "user/registerUser",
    async ({user, addresses}) => {
        const payload = {
            firstname: user.firstName,
            lastname: user.lastName,
            email: user.email,
            password: user.password,
            addresses: addresses
        };
        const response = await api.post("users/add", payload);
        return response.data;
    }
)

export const addNewUserAddress = createAsyncThunk(
    "user/addNewUserAddress",
    async ({userId, addressList}) => {
        const response = await api.post(`addresses/${userId}/new`, addressList);
        return response.data;
    }
)

export const updateUserAddress = createAsyncThunk(
    "user/updateUserAddress",
    async ({id, newAddress}) => {
        const response = await api.put(`addresses/${id}/update`, newAddress);
        return response.data;
    }
)

export const deleteUserAddress = createAsyncThunk(
    "user/deleteUserAddress",
    async (id) => {
        const response = await api.delete(`addresses/${id}/delete`);
        return response;
    }
)

export const updateUserPassword = createAsyncThunk(
    "user/updateUserPassword",
    async ({email, newPassword}) => {
        const response = await api.put(`users/user/change-password`, {email, newPassword});
        return response;
    }
)

export const getCountryNames = createAsyncThunk(
    "user/getCountryNames",
    async() => {
        const response = await axios.get("https://restcountries.com/v3.1/all?fields=name,cca2");
        const countryNames = response.data.map((country) => ({name: country.name.common, code: country.cca2}));
        countryNames.sort((a, b) => a.name.localeCompare(b.name));
        return countryNames;
    }
)

const initialState = {
    user: null,
    isLoading: true,
    errorMessage: null
};

const userSlice = createSlice({
    name: "user",
    initialState,
    reducers: {
        setUserAddresses(state, action) {
            state.user.addresses = action.payload;
        }
    },
    extraReducers: (builder) => {
        builder
        .addCase(getUserById.fulfilled, (state, action) => {
            state.user = action.payload.data;
            state.isLoading = false;
        }).addCase(getUserById.rejected, (state, action) => {
            state.errorMessage = action.error.message;
            state.isLoading = false;
        }).addCase(registerUser.fulfilled, (state, action) => {
            state.user = action.payload;
            state.isLoading = false;
        }).addCase(registerUser.rejected, (state, action) => {
            state.errorMessage = action.error.message;
            state.isLoading = false;
        })
    }
})

export const { setUserAddresses } = userSlice.actions; 
export default userSlice.reducer;