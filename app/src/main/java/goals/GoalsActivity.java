package goals;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalssetting.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import database.DBHelper;
import sprints.Sprint;

public class GoalsActivity extends AppCompatActivity {

    LinearLayout mainLayout;

    private Calendar startDate = Calendar.getInstance();
    private Calendar endDate = Calendar.getInstance();
    private Button viewStartDate;
    private Button viewEndDate;
    private EditText sprintTime;

    private RecyclerView viewOfGoals;
    private GoalsAdapter goalsAdapter;

    private Collection<Goal> goals;

    private DBHelper dbHelper;


    Sprint sprint;

    private Button addGoalButton;
    private View addGoalView;

    private String login;
    private long idSprint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        mainLayout = findViewById(R.id.mainLayout);

        viewStartDate = findViewById(R.id.startTime);
        viewEndDate = findViewById(R.id.endTime);

        sprintTime = findViewById(R.id.sprintTime);
        sprintTime.setEnabled(false);

        viewOfGoals = findViewById(R.id.viewOfGoals);
        viewOfGoals.setLayoutManager(new LinearLayoutManager(this));
        goalsAdapter = new GoalsAdapter(new RequestFactory());
        viewOfGoals.setAdapter(goalsAdapter);

        //dbHelper = new DBHelper(this);
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
        Intent intent = getIntent();
        idSprint = Long.valueOf(intent.getStringExtra("idSprint"));
        login = intent.getStringExtra("login");


        setInitialDateTime();
        //new GetSprintRequest().execute();

        new GetGoalsRequest().execute();

        //addGoalButton = new Button(this);
        addGoalButton = findViewById(R.id.addGoalButton);
        addGoalButton.setText("Add goal");

        addGoalButton.setEms(10);
        //addGoalButton.setBackgroundResource(R.color.colorButton);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addGoalView = inflater.inflate(R.layout.add_goal, null);



        //mainLayout.addView(addGoalButton);

        addGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainLayout.removeView(addGoalButton);
                mainLayout.addView(addGoalView);

                Button saveGoalButton = findViewById(R.id.saveGoalButton);
                Button cancelButton = findViewById(R.id.cancelButton);
                saveGoalButton.setEms(10);
                cancelButton.setEms(10);

                final EditText newGoalEditText = findViewById(R.id.newGoalEditText);
                saveGoalButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //goalsAdapter.addItem(new Goal(newGoalEditText.getText().toString()));
                        Goal tmp = new Goal(idSprint, newGoalEditText.getText().toString());
                        new AddGoalsRequest().execute(tmp);
                    }
                });
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mainLayout.removeView(addGoalView);
                        mainLayout.addView(addGoalButton);
                    }
                });
            }
        });

    }

    public void setStartDate(View v) {
        new DatePickerDialog(GoalsActivity.this, startDateSetListener,
                startDate.get(Calendar.YEAR),
                startDate.get(Calendar.MONTH),
                startDate.get(Calendar.DAY_OF_MONTH))
                .show();
    }
    public void setEndDate(View v) {
        new DatePickerDialog(GoalsActivity.this, endDateSetListener,
                endDate.get(Calendar.YEAR),
                endDate.get(Calendar.MONTH),
                endDate.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    DatePickerDialog.OnDateSetListener startDateSetListener=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            startDate.set(Calendar.YEAR, year);
            startDate.set(Calendar.MONTH, monthOfYear);
            startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //UpdateSprintRequest.execute();
            setInitialDateTime();
        }
    };
    DatePickerDialog.OnDateSetListener endDateSetListener=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            endDate.set(Calendar.YEAR, year);
            endDate.set(Calendar.MONTH, monthOfYear);
            endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };
    private void setInitialDateTime() {

        viewStartDate.setText(DateUtils.formatDateTime(this,
                startDate.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

        viewEndDate.setText(DateUtils.formatDateTime(this,
                endDate.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));

        long diff = endDate.getTimeInMillis() - startDate.getTimeInMillis();


        sprintTime.setText(String.format("%s days", String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))));
    }

    private class GetSprintRequest extends AsyncTask<Void, Void, ResponseEntity<Sprint>> {
        @Override
        protected ResponseEntity<Sprint> doInBackground(Void... params) {
            try {
                //final String url = "http://rest-service.guides.spring.io/greeting";
                final String url = "http://10.0.2.2:8080/users/" + login + "/sprints/" + idSprint;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                ResponseEntity<Sprint> sprintEntity = restTemplate.getForEntity(url, Sprint.class);
                return sprintEntity;
            } catch (Exception e) {
                //Toast.makeText(SprintsActivity.this, "Exception", Toast.LENGTH_LONG).show();
                Log.e("GoalsActivity", e.getMessage(), e);
            }

            return null;
        }
        @Override
        protected void onPostExecute(ResponseEntity<Sprint> sprintResponseEntity) {
            if (sprintResponseEntity.getStatusCode() == HttpStatus.OK)
            {
                sprint = sprintResponseEntity.getBody();
                startDate = sprint.getStartDate();
                endDate = sprint.getEndDate();
                setInitialDateTime();
            }
            else
            {
                Toast.makeText(GoalsActivity.this, "Goals are null", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class UpdateSprintRequest extends AsyncTask<Sprint, Void, Void> {
        @Override
        protected Void doInBackground(Sprint... params) {
            try {
                final String url = "http://10.0.2.2:8080/goals/" + params[0].getId();
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                ObjectMapper mapper = new MappingJackson2HttpMessageConverter().getObjectMapper();
                String jsonInString = mapper.writeValueAsString(params[0]);
                Log.i("GoalsActivity", jsonInString);

                HttpEntity<Sprint> request = new HttpEntity<>(params[0]);
                restTemplate.put(url, request);
                restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
            } catch (Exception e) {
                Log.e("GoalsActivity", e.getMessage(), e);
            }
            return null;
        }
    }


    private class GetGoalsRequest extends AsyncTask<Void, Void, ResponseEntity<Goal[]>> {
        @Override
        protected ResponseEntity<Goal[]> doInBackground(Void... params) {
            final String url = "http://10.0.2.2:8080/users/" + login + "/sprints/" + idSprint + "/goals";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            return restTemplate.getForEntity(url, Goal[].class);

        }
        @Override
        protected void onPostExecute(ResponseEntity<Goal[]> goalsEntity) {
            if (goalsEntity.getStatusCode() == HttpStatus.OK)
            {
                goals = Arrays.asList(goalsEntity.getBody());
                viewOfGoals.setHasFixedSize(true);
                goalsAdapter.setItems(goals);
                viewOfGoals.smoothScrollToPosition(viewOfGoals.getAdapter().getItemCount());

                Toast.makeText(GoalsActivity.this, "Set all goals", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(GoalsActivity.this, "Goals are null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AddGoalsRequest extends AsyncTask<Goal, Void, ResponseEntity<Goal>> {
        @Override
        protected ResponseEntity<Goal> doInBackground(Goal... params) {
            final String url = "http://10.0.2.2:8080/users/" + login + "/sprints/" + idSprint + "/goals";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            HttpEntity<Goal> request = new HttpEntity<>(params[0]);
            return restTemplate.postForEntity(url, request, Goal.class);
        }
        @Override
        protected void onPostExecute(ResponseEntity<Goal> goalResponseEntity) {
            if (goalResponseEntity.getStatusCode() == HttpStatus.CREATED)
            {
                goalsAdapter.addItem(goalResponseEntity.getBody());
                mainLayout.removeView(addGoalView);
                mainLayout.addView(addGoalButton);
            }
            else
            {
                Toast.makeText(GoalsActivity.this, "Couldn't create sprint", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
