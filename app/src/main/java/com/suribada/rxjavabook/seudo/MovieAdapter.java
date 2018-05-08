package com.suribada.rxjavabook.seudo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suribada.rxjavabook.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

class MovieAdapter extends ArrayAdapter<Movie> {

    private final LayoutInflater inflater;
    private ListView listView;

    interface ListSelectionListener {
        void onTitleClick(int position);
    }

    private ListSelectionListener listSelectionListener;

    MovieAdapter(Context context, ListView listView, List<Movie> movies) { // (1)
        super(context, 0, movies);
        inflater = LayoutInflater.from(context);
        this.listView = listView;
    }

    MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
        inflater = LayoutInflater.from(context);
    }

    void setListSelectionListener(ListSelectionListener listSelectionListener) { // (1)'
        this.listSelectionListener = listSelectionListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.movie_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Movie item = getItem(position);
        holder.title.setText(Html.fromHtml(item.title));
        holder.title.setOnClickListener(v -> { // (2) 시작
            //listView.setSelection(position);
            if (listSelectionListener != null) {
                listSelectionListener.onTitleClick(position);
            }
        }); // (2) 끝
        if (TextUtils.isEmpty(item.subtitle)) {
            holder.subtitle.setVisibility(View.GONE);
        } else {
            holder.subtitle.setVisibility(View.VISIBLE);
            holder.subtitle.setText(item.subtitle);
        }
        holder.director.setText(item.director);
        if (TextUtils.isEmpty(item.image)) {
            holder.thumbImage.setImageBitmap(null);
        } else {
            Picasso.get().load(item.image).into(holder.thumbImage);
        }
        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.thumb_image)
        ImageView thumbImage;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.subtitle)
        TextView subtitle;
        @BindView(R.id.director)
        TextView director;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
