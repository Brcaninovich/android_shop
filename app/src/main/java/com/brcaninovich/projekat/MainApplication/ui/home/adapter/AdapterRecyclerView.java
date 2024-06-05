package com.brcaninovich.projekat.MainApplication.ui.home.adapter;
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

import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerView extends RecyclerView.Adapter<AdapterRecyclerView.ViewHolder> {

    private List<Artikal> data = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setData(List<Artikal> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artikal_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Artikal artikal = data.get(position);
        holder.bind(artikal);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView nazivTextView;
        TextView opisTextView;
        TextView cijenaTextView;
        TextView lokacijaTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.artikalImageView);
            nazivTextView = itemView.findViewById(R.id.artikalNazivTextView);
            opisTextView = itemView.findViewById(R.id.artikalOpisTextView);
            cijenaTextView = itemView.findViewById(R.id.artikalCijenaTextView);
            lokacijaTextView = itemView.findViewById(R.id.artikalLokacijaTextView);
            itemView.setOnClickListener(this);
        }

        public void bind(Artikal artikal) {
            Glide.with(imageView.getContext())
                    .load(artikal.getSlike().get(0))
                    .into(imageView);

            nazivTextView.setText(artikal.getNazivArtikla());
            opisTextView.setText(artikal.getOpisArtikla());
            cijenaTextView.setText(artikal.getCijena());
            lokacijaTextView.setText(artikal.getLokacija());
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick(data.get(position));
                }
            }
        }
    }
}


