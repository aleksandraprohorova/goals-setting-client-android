package sprints;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goalssetting.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SprintAdapter extends RecyclerView.Adapter<SprintAdapter.SprintViewHolder> {
    private List<Sprint> sprintsList = new ArrayList<>();
    private View parentView;
    private OnSprintClickListener onSprintClickListener;

    public SprintAdapter()
    {

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
    public SprintAdapter(OnSprintClickListener onSprintClickListener)
    {
        this.onSprintClickListener = onSprintClickListener;
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
        notifyDataSetChanged();
    }



}
