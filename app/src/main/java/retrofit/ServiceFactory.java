package retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ServiceFactory {
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();
    private static GoalsService goalsService = retrofit.create(GoalsService.class);
    private static SprintsService sprintsService = retrofit.create(SprintsService.class);

    public static GoalsService getGoalsService() {
        return goalsService;
    }
    public static SprintsService getSprintsService() {
        return sprintsService;
    }
}
