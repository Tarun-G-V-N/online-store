import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { api } from "../../components/services/api"

export const getAllProductCategories = createAsyncThunk(
  "categories/getAllProductCategories",
  async () => {
    const response = await api.get("/categories/all");
    return response.data.data;
  }
)

const initialState = {
  categories: [],
//   isLoading: false,
  errorMessage: null,
};

const categorySlice = createSlice({
  name: "category",
  initialState,
  reducers: {
    addCategory: (state, action) => {
        const newId = state.categories.length > 0 ? Math.max(...state.categories.map(c => c.id)) + 1 : 1;
        const newCategory = { id: newId, name: action.payload };
        state.categories.push(newCategory);
        console.log("on Category Slice:", state.categories);
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(getAllProductCategories.fulfilled, (state, action) => {
        state.categories = action.payload;
        state.errorMessage = null;
        // state.isLoading = false;
      })
      .addCase(getAllProductCategories.rejected, (state, action) => {
        state.errorMessage = action.error.message;
        // state.isLoading = false;
      })
    //   .addCase(getAllProductCategories.pending, (state) => {
    //     state.errorMessage = null;
    //     state.isLoading = true;
    //   });
  }
})


export const { addCategory } = categorySlice.actions;
export default categorySlice.reducer;