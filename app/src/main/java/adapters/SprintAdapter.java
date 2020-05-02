package adapters;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalssetting.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import entity.Sprint;
import itemtouch.ItemTouchHelperAdapter;
import retrofit.ServiceFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SprintAdapter extends RecyclerView.Adapter<SprintAdapter.SprintViewHolder> implements ItemTouchHelperAdapter {
    private List<Sprint> sprintsList = new ArrayList<>();
    private View parentView;
    private OnSprintClickListener onSprintClickListener;

    private String login;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(sprintsList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(sprintsList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        if (position >= sprintsList.size())
        {
            Log.i("SprintAdapter", "Invalid position " + position);
            return;
        }
        Long idSprint = sprintsList.get(position).getId();
        sprintsList.remove(position);
        notifyItemRemoved(position);
        Call<Object> response = ServiceFactory.getSprintsService().deleteSprint(login, idSprint);
        response.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Log.i("SprintsAdapter", "Deleted sprint successfully");

                } else {
                    Log.i("SprintsAdapter", "Couldn't delete sprint");
                }
            }
            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.i("SprintsAdapter", "Failed request to delete sprint: " + t.getMessage());
            }
        });
    }

    class SprintViewHolder extends RecyclerView.ViewHolder{
        private TextView termTextView;
        private View parentView;

        public SprintViewHolder(View itemView){
            super(itemView);
            parentView = itemView;
            termTextView = itemView.findViewById(R.id.termTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Sprint sprint = sprintsList.get(getLayoutPosition());
                    onSprintClickListener.onSprintClick(sprint);
                }
            });
        }
        public void bind(Sprint sprint)
        {
            termTextView.setText(String.format("%s - %s", DateUtils.formatDateTime(parentView.getContext(),
                    sprint.getStartDate().getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR), DateUtils.formatDateTime(parentView.getContext(),
                    sprint.getEndDate().getTimeInMillis(),
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR)));
        }
    }
    public interface OnSprintClickListener {
        void onSprintClick(Sprint sprint);
    }
    public SprintAdapter(OnSprintClickListener onSprintClickListener, String login)
    {
        this.onSprintClickListener = onSprintClickListener;
        this.login = login;
    }



    @Override
    public SprintAdapter.SprintViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sprint_view, parent, false);
        parentView = parent;
        return new SprintAdapter.SprintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SprintAdapter.SprintViewHolder holder, int position) {
        holder.bind(sprintsList.get(position));

        holder.termTextView.setText(String.format("%s - %s", DateUtils.formatDateTime(parentView.getContext(),
                sprintsList.get(holder.getAdapterPosition()).getStartDate().getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR), DateUtils.formatDateTime(parentView.getContext(),
                sprintsList.get(holder.getAdapterPosition()).getEndDate().getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR)));
    }

    @Override
    public int getItemCount() {
        return sprintsList.size();
    }

    public void setItems(Collection<Sprint> sprints) {
        sprintsList.addAll(sprints);
        notifyDataSetChanged();
    }
    public void clearItems(){
        sprintsList.clear();
        notifyDataSetChanged();
    }
    public void addItem(Sprint newItem) {
        sprintsList.add(newItem);
        notifyItemInserted(getItemCount() - 1);
    }



}
