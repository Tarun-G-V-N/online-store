import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { api } from "../../components/services/api";

export const searchByImage = createAsyncThunk(
  "search/searchByImage",
  async (imageFile) => {
    const formData = new FormData();
    formData.append("image", imageFile);
    const respone = await api.post("products/search-by-image", formData);
    return respone.data;
  }
);

const initialState = {
  searchQuery: "",
  selectedCategory: "all",
  imageSearch: null,
  imageSearchResults: []
};

const searchSlice = createSlice({
  name: "search",
  initialState,
    reducers: {
    setSearchQuery: (state, action) => {
      state.searchQuery = action.payload;
    },
    setSelectedCategory: (state, action) => {
      state.selectedCategory = action.payload;
    },
    clearFilters: (state) => {
      state.searchQuery = "";
      state.selectedCategory = "all";
      state.imageSearch = null;
      state.imageSearchResults = [];
    },
    setInitialSearchQuery: (state, action) => {
      state.searchQuery = action.payload;
    },
    setImageSearch: (state, action) => {
      state.imageSearch = action.payload;
    }
  },
  extraReducers: (builder) => {
    builder.addCase(searchByImage.fulfilled, (state, action) => {
      state.imageSearchResults = action.payload.data;
    });
  }
});

export const { setSearchQuery, setSelectedCategory, clearFilters, setInitialSearchQuery, setImageSearch } = searchSlice.actions;

export default searchSlice.reducer;