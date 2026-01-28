import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import { api, securedApi } from "../../components/services/api"

export const addNewProduct = createAsyncThunk(
  "product/addNewProduct",
  async (productData) => {
    const response = await securedApi.post("/products/add", productData);
    return response.data;
  }
)

export const updateProduct = createAsyncThunk(
  "product/updateProduct",
  async ({productId, updatedProduct}) => {
    console.log(productId);
    console.log(updatedProduct);
    const response = await securedApi.put(`/products/product/${productId}/update`, updatedProduct);
    return response.data;
  }
)

export const deleteProduct = createAsyncThunk(
  "product/deleteProduct",
  async (productId) => {
    const response = await securedApi.delete(`/products/product/${productId}/delete`);
    return response.data;
  }
)

export const getAllProducts = createAsyncThunk(
  "product/getAllProducts",
  async () => {
    const response = await api.get("/products/all");
    return response.data.data;
  }
)

export const getDistinctProductsByName = createAsyncThunk(
  "product/getDistinctProductsByName",
  async () => {
    const response = await api.get('/products/distinct/products');
    return response.data.data;
  }
)

export const getProductById = createAsyncThunk(
  "product/getProductById",
  async (productId) => {
    const response = await api.get(`/products/product/${productId}/product`);
    return response.data;
  }
)

export const getProductsByCategory = createAsyncThunk(
  "product/getProductsByCategory",
  async (categoryId) => {
    const response = await api.get(`/products/category/${categoryId}/products`);
    return response.data.data;
  }
)

export const getAllBrands = createAsyncThunk(
  "product/getAllBrands",
  async () => {
    const response = await api.get("/products/distinct/brands");
    return response.data.data;
  }
)

const initialState = {
  products: [],
  product: null,
  quantity: 1,
  distinctProducts: [],
  brands: [],
  selectedBrands: [],
  errorMessage: null,
  isLoading: true,
};

const productSlice = createSlice({
  name: "product",
  initialState,
  reducers: {
    filterByBrand: (state, action) => {
      const {brand, isChecked} = action.payload;
      if (isChecked) {
        state.selectedBrands.push(brand);
      } else {
        state.selectedBrands = state.selectedBrands.filter(b => b !== brand);
      }
    },
    setQuantity: (state, action) => {
      state.quantity = action.payload;
    },
    addBrand: (state, action) => {
      state.brands.push(action.payload);
      console.log("on Product Slice:", state.brands);
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(getAllProducts.fulfilled, (state, action) => {
        state.products = action.payload;
        state.errorMessage = null;
        state.isLoading = false;
      })
      .addCase(getAllProducts.rejected, (state, action) => {
        state.errorMessage = action.error.message;
      })
      .addCase(getAllBrands.fulfilled, (state, action) => {
        state.brands = action.payload;
        state.isLoading = false;
      })
      .addCase(getDistinctProductsByName.fulfilled, (state, action) => {
        state.distinctProducts = action.payload;
        state.isLoading = false;
      })
      .addCase(getProductById.fulfilled, (state, action) => {
        state.product = action.payload.data;
        state.isLoading = false;
      })
      .addCase(getProductsByCategory.fulfilled, (state, action) => {
        state.products = action.payload;
        state.isLoading = false;
      })
      .addCase(addNewProduct.fulfilled, (state, action) => {
        state.products.push(action.payload);
        state.errorMessage = null;
      })
      .addCase(updateProduct.fulfilled, (state, action) => {
        state.product = action.payload.data;
        state.errorMessage = null;
      }).addCase(deleteProduct.fulfilled, (state, action) => {
        state.products = state.products.filter((product) => product.id !== action.payload.data);
      });
  }
})

export const { filterByBrand, setQuantity, addBrand } = productSlice.actions;
export default productSlice.reducer;