package goals;

import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RequestFactory {
    AsyncTask<Goal, Void, Void> getPutRequest()
    {
        return new UpdateGoalRequest();
    }
    private class UpdateGoalRequest extends AsyncTask<Goal, Void, Void> {
        @Override
        protected Void doInBackground(Goal... params) {
            try {
                final String url = "http://10.0.2.2:8080/goals/" + params[0].getId();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                ObjectMapper mapper = new MappingJackson2HttpMessageConverter().getObjectMapper();
                String jsonInString = mapper.writeValueAsString(params[0]);
                Log.i("GoalsActivity", jsonInString);

                HttpEntity<Goal> request = new HttpEntity<>(params[0]);
                restTemplate.put(url, request);
                restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
            } catch (Exception e) {
                Log.e("GoalsActivity", e.getMessage(), e);
            }
            return null;
        }
    }
}
