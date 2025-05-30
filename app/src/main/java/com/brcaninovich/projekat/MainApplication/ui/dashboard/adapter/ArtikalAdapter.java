package com.brcaninovich.projekat.MainApplication.ui.dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brcaninovich.projekat.MainApplication.Objects.Artikal;
import com.brcaninovich.projekat.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ArtikalAdapter extends RecyclerView.Adapter<ArtikalAdapter.ArtikalViewHolder> {
    private List<Artikal> artikli;
    private final Context context;
    private OnItemClickListener onItemClickListener;

    public ArtikalAdapter(Context context, List<Artikal> artikli) {
        this.context = context;
        this.artikli = artikli != null ? artikli : new ArrayList<>();
    }


    public interface OnItemClickListener {
        void onItemClick(Artikal artikal);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ArtikalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artikal_card, parent, false);
        return new ArtikalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtikalViewHolder holder, int position) {
        Artikal artikal = artikli.get(position);
        holder.bind(artikal);
    }

    @Override
    public int getItemCount() {
        return artikli != null ? artikli.size() : 0;
    }

    public void setArtikli(List<Artikal> artikli) {
        this.artikli = artikli != null ? artikli : new ArrayList<>();
        notifyDataSetChanged();
    }

    public class ArtikalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nazivTextView;
        TextView opisTextView;
        TextView cijenaTextView;
        TextView statusTextView;
        TextView lokacijaTextView;
        ImageView slikaImageView;

        public ArtikalViewHolder(@NonNull View itemView) {
            super(itemView);
            nazivTextView = itemView.findViewById(R.id.artikalNazivTextView);
            opisTextView = itemView.findViewById(R.id.artikalOpisTextView);
            cijenaTextView = itemView.findViewById(R.id.artikalCijenaTextView);
            statusTextView = itemView.findViewById(R.id.artikalStatusTextView);
            lokacijaTextView = itemView.findViewById(R.id.artikalLokacijaTextView);
            slikaImageView = itemView.findViewById(R.id.artikalImageView);
            itemView.setOnClickListener(this);
        }

        public void bind(Artikal artikal) {
            nazivTextView.setText(artikal.getNazivArtikla());
            opisTextView.setText(artikal.getOpisArtikla());
            cijenaTextView.setText(artikal.getCijena());
            statusTextView.setText(artikal.getStanje());
            lokacijaTextView.setText(artikal.getLokacija());

            if (artikal.getSlike() != null && !artikal.getSlike().isEmpty()) {
                Glide.with(context)
                        .load(artikal.getSlike().get(0))
                        .apply(new RequestOptions().centerCrop())
                        .into(slikaImageView);
            } else {
                slikaImageView.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(artikli.get(position));
                }
            }
        }
    }
}
