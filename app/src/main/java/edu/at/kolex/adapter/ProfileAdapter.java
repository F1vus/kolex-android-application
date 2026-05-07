package edu.at.kolex.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.at.kolex.R;
import edu.at.kolex.model.Profile;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private final List<Profile> profiles;
    private final OnProfileClickListener listener;

    public interface OnProfileClickListener {
        void onProfileClick(Profile profile);
    }

    public ProfileAdapter(List<Profile> profiles, OnProfileClickListener listener) {
        this.profiles = profiles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profiles.get(position);
        holder.tvProfileName.setText(profile.getFirstName() + " " + profile.getLastName());
        holder.itemView.setOnClickListener(v -> listener.onProfileClick(profile));
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public void updateProfiles(List<Profile> newProfiles) {
        this.profiles.clear();
        this.profiles.addAll(newProfiles);
        notifyDataSetChanged();
    }

    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView tvProfileName;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProfileName = itemView.findViewById(R.id.tvProfileName);
        }
    }
}
