package retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ServiceFactory {
    private static String login;
    private static String password;
    private static Retrofit retrofit;
    /*private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build();*/

    public static void setAuthorizeParameters(String l, String p) { login = l;
        password = p;
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(login, password))
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public static GoalsService getGoalsService() {
        return retrofit.create(GoalsService.class);
    }
    public static SprintsService getSprintsService() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(login, password))
                .build();
        return  new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(SprintsService.class);
    }
    public static RegistrationService getRegistrationService() {
        /*OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new BasicAuthInterceptor(login, password))
                .build();
        return  new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(RegistrationService.class);*/
        return retrofit.create(RegistrationService.class);
    }
}
