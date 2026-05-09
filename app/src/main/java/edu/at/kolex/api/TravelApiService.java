package edu.at.kolex.api;

import java.time.LocalDateTime;
import java.util.List;
import edu.at.kolex.model.Route;
import edu.at.kolex.model.Station;
import edu.at.kolex.model.TravelStop;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TravelApiService {
    @GET("api/search/trains")
    Call<List<Route>> searchTrains(
            @Query("fromId") Long fromId,
            @Query("toId") Long toId,
            @Query("departureTime" )LocalDateTime dateAndTimeSearchTrain
            );

    @GET("api/metadata/stations")
    Call<List<Station>> getStations();

    @GET("api/search/stations")
    Call<List<TravelStop>> getStopsByTravelId(
            @Query("travelId") Long travelId
    );
}
