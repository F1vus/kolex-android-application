package edu.at.kolex.api;

import java.time.LocalDateTime;
import java.util.List;

import edu.at.kolex.model.SeatStatus;
import edu.at.kolex.model.Travel;
import edu.at.kolex.model.Station;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TravelApiService {
    @GET("api/search/trains")
    Call<List<Travel>> searchTravel(
            @Query("fromId") Long fromId,
            @Query("toId") Long toId,
            @Query("departureTime" )LocalDateTime dateAndTimeSearchTrain
            );

    @GET("api/metadata/stations")
    Call<List<Station>> getStations();

    @GET("api/search/seats")
    Call<List<SeatStatus>> getSeats(
            @Query("travelId") Long travelId,
            @Query("startStop") int startStop,
            @Query("endStop") int endStop
    );
}
