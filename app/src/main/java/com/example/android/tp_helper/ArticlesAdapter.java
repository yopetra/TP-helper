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

    public interface ArticleAdapterOnClickHandler{
        void onClick(int finalId);
        void onLongClick(int finalId);
    }

    public ArticlesAdapter(ArticleAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

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
    public void onBindViewHolder(@NonNull ArticleViewHolder articleViewHolder, final int position) {

        String currentArticleName = null;
        JSONObject currentArticle = null;
        try {
            currentArticle = (JSONObject) mArticlesData.get(position);
            currentArticleName = currentArticle.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        articleViewHolder.articleItemTextView.setText(currentArticleName);

        int id = -1;
        try {
            id = currentArticle.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final int finalId = id;
        articleViewHolder.view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("OnClick ---" + finalId);

                mClickHandler.onClick(finalId);
            }
        });

        articleViewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                System.out.println("OnLongClick ---");
                mClickHandler.onLongClick(finalId);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mArticlesData == null){return 0;}

        return mArticlesData.length();
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder{

        public View view;
        TextView articleItemTextView;

        public ArticleViewHolder(View view){
            super(view);
            this.view = view;
            articleItemTextView = itemView.findViewById(R.id.tv_article_block_item);
        }
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
