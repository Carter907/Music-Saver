package com.example.dbtestactual;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackHolder> {

    private List<Track> trackList;
    private TrackHolder highlighted;

    public TrackAdapter() {
        trackList = new ArrayList<>();
    }

    @Override
    public TrackHolder onCreateViewHolder(ViewGroup container, int viewType) {

        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.track_element, container, false);

        TrackHolder holder = new TrackHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(TrackHolder holder, int position) {

        TextView title = holder.itemView.findViewById(R.id.title);
        TextView artist = holder.itemView.findViewById(R.id.artist);

        holder.track = trackList.get(position);

        title.setText(trackList.get(position).getTitle());
        artist.setText("by " + trackList.get(position).getArtist());
    }


    public class TrackHolder extends RecyclerView.ViewHolder {
        private Track track;

        public TrackHolder(@NonNull View view) {
            super(view);
            view.setOnClickListener(e -> {

                if (highlighted != this) {
                    if (highlighted != null) {
                        highlighted.itemView.setBackground(AppCompatResources.getDrawable(
                                highlighted.itemView.getContext(), R.drawable.track_element_background));
                    }
                    view.setBackground(AppCompatResources.getDrawable(
                            view.getContext(), R.drawable.highlighted_background));
                    highlighted = this;
                } else if (highlighted == this) {
                    view.setBackground(AppCompatResources.getDrawable(
                            view.getContext(), R.drawable.track_element_background));
                    highlighted = null;
                }

            });
        }

        public Track getTrack() {
            return track;
        }
    }

    public List<Track> getTrackList() {
        return this.trackList;
    }

    public TrackHolder getHighlighted() {
        return highlighted;
    }

    public void setHighlighted(TrackHolder highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }
}
