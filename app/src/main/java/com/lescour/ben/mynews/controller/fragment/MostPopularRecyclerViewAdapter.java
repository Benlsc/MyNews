package com.lescour.ben.mynews.controller.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.lescour.ben.mynews.R;
import com.lescour.ben.mynews.model.Article;
import com.lescour.ben.mynews.model.MediaMetadatum;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MostPopularRecyclerViewAdapter extends RecyclerView.Adapter<MostPopularRecyclerViewAdapter.ViewHolder> {

    private final List<Article> articles;
    private final BaseFragment.OnListFragmentInteractionListener mListener;
    private RequestManager glide;
    private String imgUrl;

    public MostPopularRecyclerViewAdapter(List<Article> articles, BaseFragment.OnListFragmentInteractionListener listener, RequestManager glide) {
        this.articles = articles;
        mListener = listener;
        this.glide = glide;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_article, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.article = articles.get(position);
        if (holder.article.getMedia().get(0).getMediaMetadata().isEmpty()) {
            holder.articleImg.setImageResource(R.drawable.ic_launcher_background);
        } else {
            findUrlOfImgArticle(holder.article);
            holder.showArticleImg(imgUrl, this.glide);
        }
        holder.articleSectionSubsection.setText(holder.article.getSection());
        holder.articleDate.setText(getDateWhitNewFormat(holder.article));
        holder.articleTitle.setText(holder.article.getTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected
                    mListener.onListFragmentInteraction(holder.article);
                }
            }
        });
    }

    private void findUrlOfImgArticle(Article article) {
        boolean imgFound = false;
        int i = 0;
        do {
            MediaMetadatum temporaryMediaMetadatum = article.getMedia().get(0).getMediaMetadata().get(i);
            if (temporaryMediaMetadatum.getFormat().equals("Standard Thumbnail")) {
                imgUrl = article.getMedia().get(0).getMediaMetadata().get(i).getUrl();
                imgFound = true;
            }
            i++;
        } while (!imgFound);
    }

    private String getDateWhitNewFormat(Article article) {
        String rawDate = article.getPublishedDate();
        String year = rawDate.substring(0,4);
        String month = rawDate.substring(5,7);
        String day = rawDate.substring(8,10);
        return day + "/" + month + "/" + year;
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @BindView(R.id.img_article) ImageView articleImg;
        @BindView(R.id.article_sectionSubsection) TextView articleSectionSubsection;
        @BindView(R.id.article_date) TextView articleDate;
        @BindView(R.id.article_title) TextView articleTitle;
        public Article article;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        public void showArticleImg(String imgUrl, RequestManager glide) {
            glide.load(imgUrl).into(articleImg);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + articleDate.getText() + "'";
        }
    }
}
