package adapters;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalssetting.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import entity.Goal;
import retrofit.RequestFactory;
import itemtouch.ItemTouchHelperAdapter;
import retrofit.ServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder>
    implements ItemTouchHelperAdapter
{
    private List<Goal> goalsList = new ArrayList<>();
    private String login;

    public GoalsAdapter(String login)
    {
        this.login = login;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(goalsList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(goalsList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(final int position) {
        Goal tmp = goalsList.get(position);
        Call<Object> response = ServiceFactory.getGoalsService().deleteGoal(login, tmp.getIdSprint(), tmp.getId());
        response.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.i("GoalsAdapter", "Before looking at response");
                if (response.isSuccessful()) {
                    Log.i("GoalsAdapter", "Deleted goal successfully");
                    goalsList.remove(position);
                    notifyItemRemoved(position);
                } else {
                    Log.i("GoalsAdapter", "Couldn't delete goal");
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("GoalsAdapter", "Failed request to delete goal: " + t.getMessage());
            }
        });

    }

    class GoalsViewHolder extends RecyclerView.ViewHolder{
        private EditText descriptionTextView;
        private CheckBox checkBox;

        public GoalsViewHolder(View itemView){
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
        @SuppressLint("ResourceType")
        public void bind(final Goal goal)
        {
            descriptionTextView.setText(goal.getDescription());
            checkBox.setChecked(goal.getIsDone());

            final int normalFlags = descriptionTextView.getPaintFlags();

            if (goal.getIsDone())
            {
                descriptionTextView.setPaintFlags(normalFlags| Paint.STRIKE_THRU_TEXT_FLAG);
            }

            descriptionTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus)
                    {
                        goal.setDescription(descriptionTextView.getText().toString());
                        descriptionTextView.setCursorVisible(false);

                        Call<Object> response = ServiceFactory.getGoalsService().replaceGoal(login,
                                goal.getIdSprint(), goal.getId(), goal);
                        response.enqueue(RequestFactory.getPutRequest());
                    }
                    else
                    {
                        descriptionTextView.setCursorVisible(true);
                    }
                }
            });


            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    if (checkBox.isChecked()) {
                        goal.setIsDone(true);
                        descriptionTextView.setPaintFlags(normalFlags| Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        goal.setIsDone(false);
                        descriptionTextView.setPaintFlags(normalFlags);
                        Log.i("GoalsAdapter", "Set isdone");
                    }
                    Log.i("GoalsAdapter", "Before executing a put request");
                    Call<Object> response = ServiceFactory.getGoalsService().replaceGoal(login, goal.getIdSprint(), goal.getId(),
                            goal);
                    response.enqueue(RequestFactory.getPutRequest());
                }
            });
        }
    }


    @Override
    public GoalsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goal_view, parent, false);
        return new GoalsViewHolder(view/*, new DescriptionEditTextListener()*/);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalsViewHolder holder, int position) {
        holder.bind(goalsList.get(position));
        holder.descriptionTextView.setText(goalsList.get(holder.getAdapterPosition()).getDescription());
    }

    @Override
    public int getItemCount() {
        return goalsList.size();
    }

    public void setItems(Collection<Goal> goals) {
        goalsList.addAll(goals);
        notifyDataSetChanged();
    }
    public void clearItems(){
        goalsList.clear();
        notifyDataSetChanged();
    }
    public void addItem(Goal newItem) {
        goalsList.add(newItem);
        //notifyItemInserted(getItemCount());
        notifyDataSetChanged();
    }




}
