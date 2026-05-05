package edu.at.kolex.api;

import java.util.List;

import edu.at.kolex.model.Ticket;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TicketApiService {
    
    /**
     * Get all tickets for the current user
     */
    @GET("api/tickets")
    Call<List<Ticket>> getUserTickets();
    
    /**
     * Get a specific ticket by ID
     */
    @GET("api/tickets/{ticketId}")
    Call<Ticket> getTicketById(@Path("ticketId") Long ticketId);
    
    /**
     * Get tickets for a specific user profile
     */
    @GET("api/tickets/profile/{profileId}")
    Call<List<Ticket>> getTicketsByProfile(@Path("profileId") Long profileId);
}
