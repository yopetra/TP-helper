package com.example.android.tp_helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {

    private JSONArray mArticlesData = new JSONArray();

    private final ArticleAdapterOnClickHandler mClickHandler;
//    private final ArticleAdapterOnLongClickHandler mLongClickHandler;

    public interface ArticleAdapterOnClickHandler{
        void onClick(JSONObject articleItem);
    }

//    public interface ArticleAdapterOnLongClickHandler{
//        void onLongClick(JSONObject articleItem);
//    }

    public ArticlesAdapter(ArticleAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

//    public ArticlesAdapter(ArticleAdapterOnLongClickHandler longClickHandler){
//        mLongClickHandler = longClickHandler;
//    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.article_item_in_main;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldBeAttachToParent = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldBeAttachToParent);
        ArticleViewHolder viewHolder = new ArticleViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder articleViewHolder, int position) {
        String currentArticleName = null;
        try {
            JSONObject currentArticle = (JSONObject) mArticlesData.get(position);
            currentArticleName = currentArticle.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        articleViewHolder.articleItemTextView.setText(currentArticleName);
    }

    @Override
    public int getItemCount() {
        if(mArticlesData == null){return 0;}

        return mArticlesData.length();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

//        ImageView articleItemImageView;1
        TextView articleItemTextView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            articleItemTextView = itemView.findViewById(R.id.tv_article_block_item);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            JSONObject articleItem = null;
            try {
                articleItem = (JSONObject) mArticlesData.get(adapterPosition);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mClickHandler.onClick(articleItem);
        }

//        @Override
//        public boolean onLongClick(View view) {
//            int adapterPosition = getAdapterPosition();
//            JSONObject articleItem = null;
//            try {
//                articleItem = (JSONObject) mArticlesData.get(adapterPosition);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            mLongClickHandler.onLongClick(articleItem);
//            return true;
//        }
    }



    public void setArticlesData(JSONArray articlesData){
        int arraySize = articlesData.length();
        for(int i = 0; i < arraySize; i++){
            try {
                JSONObject currentJsonItem = (JSONObject) articlesData.getJSONObject(i);
                mArticlesData.put(currentJsonItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        notifyDataSetChanged();
    }

    public void clearData(){
        while(mArticlesData.length() > 0){
            mArticlesData.remove(0);
        }
    }
}
