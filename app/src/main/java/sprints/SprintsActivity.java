package sprints;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalssetting.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import database.DBHelper;
import goals.GoalsActivity;

public class SprintsActivity extends AppCompatActivity {

    private RecyclerView viewOfSprints;
    private SprintAdapter sprintAdapter;

    private ArrayList<Sprint> sprints;
    private DBHelper dbHelper;

    private String login;


    private void launchGoalsActivity(long id)
    {
        Intent intent = new Intent(this, GoalsActivity.class);
        intent.putExtra("idSprint", Long.toString(id));
        intent.putExtra("login", login);
        startActivity(intent);
    }
    private void loadSprints()
    {
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("sprints", null, null, null, null, null, null);
        sprints = new ArrayList<>();
        if (c.moveToFirst())
        {
            long id = c.getColumnIndex("id");
            Sprint sprint = new Sprint(id);
            sprints.add(sprint);
            while (c.moveToNext())
            {
                id = c.getColumnIndex("id");
                sprint = new Sprint(id);
                sprints.add(sprint);
            }
        }
        c.close();
        db.close();
        sprintAdapter.setItems(sprints);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sprints);

        viewOfSprints = findViewById(R.id.viewOfSprints);
        viewOfSprints.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);

        login = "second";

        SprintAdapter.OnSprintClickListener onSprintClickListener = new SprintAdapter.OnSprintClickListener() {
            @Override
            public void onSprintClick(Sprint sprint) {
                Toast.makeText(SprintsActivity.this, "Sprint clicked", Toast.LENGTH_SHORT).show();
                launchGoalsActivity(sprint.getId());
            }
        };
        sprintAdapter = new SprintAdapter(onSprintClickListener);
        viewOfSprints.setAdapter(sprintAdapter);

        new GetSprintsRequest().execute();
        //new AddSprintsRequest().execute(new Sprint());

        Button addSprintButton = findViewById(R.id.addSprintButton);
        addSprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SQLiteDatabase db = dbHelper.getWritableDatabase();
                //long rowId = db.insert("sprints", null, null);
                Sprint tmp = new Sprint();
                //sprintAdapter.addItem(tmp);
                new AddSprintsRequest().execute(new Sprint());


            }
        });
        dbHelper.close();
    }

    private class GetSprintsRequest extends AsyncTask<Void, Void, List<Sprint>> {
        @Override
        protected List<Sprint> doInBackground(Void... params) {
            try {
                final String url = "http://10.0.2.2:8080/users/" + login + "/sprints";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(url,Object[].class);

                Log.i("SprintsActivity", Arrays.toString(responseEntity.getBody()));

                ResponseEntity<Sprint[]> sprintsEntity = restTemplate.getForEntity(url, Sprint[].class);
                if (sprintsEntity.getStatusCode() == HttpStatus.OK) {
                    Sprint[] sprints = sprintsEntity.getBody();
                    return Arrays.asList(sprints);
                }

            } catch (Exception e) {

                Log.e("SprintsActivity", e.getMessage(), e);
            }

            return null;
        }
        @Override
        protected void onPostExecute(List<Sprint> sprints) {
            if (sprints != null)
            {
                sprintAdapter.setItems(sprints);
                Toast.makeText(SprintsActivity.this, "Set all sprints", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(SprintsActivity.this, "Sprints are null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AddSprintsRequest extends AsyncTask<Sprint, Void, Sprint> {
        @Override
        protected Sprint doInBackground(Sprint... params) {
            try {
                final String url = "http://10.0.2.2:8080/users/" + login + "/sprints";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                ObjectMapper mapper = new MappingJackson2HttpMessageConverter().getObjectMapper();
                String jsonInString = mapper.writeValueAsString(params[0]);

                Log.i("SprintsActivity", jsonInString);

                HttpEntity<Sprint> request = new HttpEntity<>(params[0]);
                ResponseEntity<Sprint> tmp = restTemplate.postForEntity(url, request, Sprint.class);

                if (tmp.getStatusCode() == HttpStatus.CREATED) {
                    return tmp.getBody();
                }
            } catch (Exception e) {
                //Toast.makeText(SprintsActivity.this, "Exception", Toast.LENGTH_LONG).show();
                Log.e("SprintsActivity", e.getMessage(), e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Sprint sprint) {
            if (sprint != null)
            {
                sprintAdapter.addItem(sprint);
                launchGoalsActivity(sprint.getId());
            }
            else
            {
                Toast.makeText(SprintsActivity.this, "Couldn't create sprint", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
