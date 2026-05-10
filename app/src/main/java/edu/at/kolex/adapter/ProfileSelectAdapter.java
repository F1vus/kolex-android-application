package edu.at.kolex.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.radiobutton.MaterialRadioButton;

import java.util.ArrayList;
import java.util.List;

import edu.at.kolex.R;
import edu.at.kolex.model.Profile;

public class ProfileSelectAdapter extends RecyclerView.Adapter<ProfileSelectAdapter.ProfileVH> {

    public interface OnProfileSelectedListener {
        void onProfileSelected(Profile profile);
    }

    private final List<Profile> profiles = new ArrayList<>();
    private final OnProfileSelectedListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public ProfileSelectAdapter(OnProfileSelectedListener listener) {
        this.listener = listener;
    }

    public void setProfiles(List<Profile> newProfiles) {
        profiles.clear();
        if (newProfiles != null) {
            profiles.addAll(newProfiles);
        }

        if (!profiles.isEmpty()) {
            selectedPosition = 0;
            if (listener != null) {
                listener.onProfileSelected(profiles.get(0));
            }
        } else {
            selectedPosition = RecyclerView.NO_POSITION;
        }

        notifyDataSetChanged();
    }

    public Profile getSelectedProfile() {
        if (selectedPosition >= 0 && selectedPosition < profiles.size()) {
            return profiles.get(selectedPosition);
        }
        return null;
    }

    @NonNull
    @Override
    public ProfileVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_select, parent, false);
        return new ProfileVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileVH holder, int position) {
        Profile profile = profiles.get(position);

        String fullName = profile.getFirstName() + " " + profile.getLastName();
        holder.tvProfileName.setText(fullName);

        holder.rbProfile.setChecked(position == selectedPosition);

        View.OnClickListener selectListener = v -> {
            int oldPos = selectedPosition;
            selectedPosition = holder.getBindingAdapterPosition();

            if (oldPos != RecyclerView.NO_POSITION) {
                notifyItemChanged(oldPos);
            }
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onProfileSelected(profiles.get(selectedPosition));
                Log.v("ProfileSelectedAdapter", "Profile selected: " + profiles.get(selectedPosition));
            }
        };

        holder.itemView.setOnClickListener(selectListener);
        holder.rbProfile.setOnClickListener(selectListener);
    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    static class ProfileVH extends RecyclerView.ViewHolder {
        TextView tvProfileName;
        MaterialRadioButton rbProfile;

        public ProfileVH(@NonNull View itemView) {
            super(itemView);
            tvProfileName = itemView.findViewById(R.id.tvProfileName);
            rbProfile = itemView.findViewById(R.id.rbProfile);
        }
    }
}
