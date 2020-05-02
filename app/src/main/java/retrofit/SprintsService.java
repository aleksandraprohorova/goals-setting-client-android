package retrofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import entity.Sprint;


public interface SprintsService {
    @GET("users/{login}/sprints/{idSprint}")
    Call<Sprint> getSprintById(@Path("login") String login, @Path("idSprint") Long idSprint);

    @GET("users/{login}/sprints")
    Call<Iterable<Sprint>> getAllSprints(@Path("login") String login);

    @POST("users/{login}/sprints")
    Call<Object> addSprint(@Path("login") String login, @Body Sprint sprint);

    @DELETE("users/{login}/sprints/{idSprint}")
    Call<Object> deleteSprint(@Path("login") String login, @Path("idSprint") Long idSprint);

    @PUT("users/{login}/sprints/{idSprint}")
    Call<Object> replaceSprint(@Path("login") String login, @Path("idSprint") Long idSprint, @Body Sprint newSprint);
}
