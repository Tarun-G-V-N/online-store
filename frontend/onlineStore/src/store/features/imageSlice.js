import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { api } from "../../components/services/api"

export const uploadImages = createAsyncThunk(
    "images/uploadImage",
    async ({productId, files}) => {
        const formData = new FormData();
        if(Array.isArray(files)) {
                files.forEach(file => {
                formData.append("files", file);
            });
        }
        else {
            formData.append("files", files);
        }
        formData.append("productId", productId);
        const response = await api.post(`/images/upload`, formData);
        return response.data;
    }
);

export const updateProductImages = createAsyncThunk(
    "images/updateProductImages",
    async ({imageId, productId, file}) => {
        const formData = new FormData();
        formData.append("file", file);
        const response = await api.put(`/images/image/${imageId}/update?productId=${productId}`, formData);
        return response.data;
    } 
)

export const deleteProductImage = createAsyncThunk(
    "images/deleteProductImages",
    async (imageId) => {
        const response = await api.delete(`/images/image/${imageId}/delete`);
        return response.data;
    }
)

const initialState = {
    uploadedImages: []
};                                                          

const imageSlice = createSlice({
  name: "image",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder.addCase(uploadImages.fulfilled, (state, action) => {
        state.uploadedImages = [...state.uploadedImages, ...action.payload.data];
    }).addCase(updateProductImages.fulfilled, (state, action) => {
        state.uploadedImages = [...state.uploadedImages, action.payload.data];
    });
  }
});


export default imageSlice.reducer;