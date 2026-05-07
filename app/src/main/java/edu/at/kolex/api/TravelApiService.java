package edu.at.kolex.api;

import java.time.LocalDateTime;
import java.util.List;
import edu.at.kolex.model.Route;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TravelApiService {
    @GET("api/v1/search/trains")
    Call<List<Route>> searchTrains(
            @Query("fromId") Long fromId,
            @Query("toId") Long toId,
            @Query("departureTime" )LocalDateTime dateAndTimeSearchTrain
            );
}
