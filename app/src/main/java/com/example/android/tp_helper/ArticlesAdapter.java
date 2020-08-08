package com.example.android.tp_helper;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.tp_helper.data.AppDatabase;
import com.example.android.tp_helper.data.ArticleEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {

    private JSONArray mArticlesData = new JSONArray();
    private AppDatabase mDb;
    private Context mContext;

    private final ArticleAdapterOnClickHandler mClickHandler;

    public interface ArticleAdapterOnClickHandler{
        void onClick(int finalId);
        void onLongClick(int finalId);
    }

    public ArticlesAdapter(ArticleAdapterOnClickHandler clickHandler, Context context){
        mClickHandler = clickHandler;
        mDb = AppDatabase.getInstance(context);
        mContext = context;
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
                mClickHandler.onClick(finalId);
            }
        });

        articleViewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
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

    private class RemoveArticleTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            int position = integers[0];

            ArticleEntry articleEntry = mDb.articleDao().readArticleById(position);
            mDb.articleDao().deleteArticle(articleEntry);
            return null;
        }
    }

    public void removeItem(final int position) {
        new RemoveArticleTask().execute(position);
        notifyItemRemoved(position);

        // Fetch all articles to get its size in the list
        final LiveData<List<ArticleEntry>> task = mDb.articleDao().loadAllArticles();
        task.observe((LifecycleOwner) mContext, new Observer<List<ArticleEntry>>() {
            @Override
            public void onChanged(List<ArticleEntry> articleEntries) {
                List<ArticleEntry> list = null;
                    list = articleEntries;
                notifyItemRangeChanged(position, list.size());
                notifyDataSetChanged();
            }
        });
    }
}
