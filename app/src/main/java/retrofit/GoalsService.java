package retrofit;

import goals.Goal;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface GoalsService {
    @GET("users/{login}/sprints/{idSprint}/goals/{idGoal}")
    Call<Goal> getGoalById(@Path("login") String login, @Path("idSprint") Long idSprint, @Path("idGoal") Long idGoal);

    @GET("users/{login}/sprints/{idSprint}/goals")
    Call<Iterable<Goal>> getAllGoals(@Path("login") String login, @Path("idSprint") Long idSprint);

    @POST("users/{login}/sprints/{idSprint}/goals")
    Call<Object> addGoal(@Path("login") String login,@Path("idSprint") Long idSprint, @Body Goal goal);

    @DELETE("users/{login}/sprints/{idSprint}/goals/{idGoal}")
    Call<Object> deleteGoal(@Path("login") String login, @Path("idSprint") Long idSprint, @Path("idGoal") Long idGoal);

    @PUT("users/{login}/sprints/{idSprint}/goals/{idGoal}")
    Call<Object> replaceGoal(@Path("login") String login, @Path("idSprint") Long idSprint,
                             @Path("idGoal") Long idGoal, @Body Goal newGoal);
}
