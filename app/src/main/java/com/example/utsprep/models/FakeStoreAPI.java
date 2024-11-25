package com.example.utsprep.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FakeStoreAPI {
    @GET("products")
    Call<List<Product>> getProducts();
}
