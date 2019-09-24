package com.nickgonzalezs.todolist;

import com.nickgonzalezs.todolist.model.PokeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PokeService {

    @GET("pokemon")
    Call<PokeResponse> getPokemons(@Query("offset") int offset, @Query("limit") int limit);

}
