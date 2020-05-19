package activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.goalssetting.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import entity.Sprint;
import retrofit.ServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiagramActivity extends AppCompatActivity {
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagram_layout);
        WebView webView = findViewById(R.id.webWiew);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");



        webView.loadUrl("file:///android_res/raw/gantt.js");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Call<Iterable<Sprint>> response = ServiceFactory.getSprintsService().getAllSprints();
                response.enqueue(new Callback<Iterable<Sprint>>() {
                    @Override
                    public void onResponse(Call<Iterable<Sprint>> call, Response<Iterable<Sprint>> response) {
                        if (response.isSuccessful()) {
                            Log.i("GANTT", "response successfull");
                            ArrayList<Sprint> sprints = (ArrayList<Sprint>) response.body();
                            ObjectMapper mapper = new ObjectMapper();
                            int id = 1;
                            StringBuilder stringBuilder = new StringBuilder("[");
                            for (Sprint sprint: sprints) {
                                JsonNode rootNode = mapper.createObjectNode();
                                ((ObjectNode) rootNode).put("id", id);
                                ((ObjectNode) rootNode).put("name", "sprint " + id);

                                ((ObjectNode) rootNode).put("actualStart",
                                        formatter.format(sprint.getStartDate().getTime()));
                                ((ObjectNode) rootNode).put("actualEnd",
                                        formatter.format(sprint.getEndDate().getTime()));
                                try {
                                    if (id != 1) stringBuilder.append(",");

                                    stringBuilder.append(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode));
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                                id++;
                            }
                            stringBuilder.append("]");
                            Log.i("GANTT", stringBuilder.toString());
                            if (url.equals("file:///android_res/raw/gantt.js")) {
                                webView.loadUrl("javascript:plot(" + stringBuilder.toString() + ");");
                            }
                        } else {
                            Log.i("GANTT", "Response is not successful");
                        }
                    }
                    @Override
                    public void onFailure(Call<Iterable<Sprint>> call, Throwable t) {
                        Log.i("GANTT", t.getMessage());
                    }
                });

            }
        });


    }
}
