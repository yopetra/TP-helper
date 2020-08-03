package com.example.android.tp_helper.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListOfArticles {

    private static List<String> mListOfArticles = new ArrayList<>(Arrays.asList(
            "To show",
            "articles,",
            "add a new",
            "article in the App"
    ));

    public static ArrayList<String> getData(){
        return (ArrayList<String>) mListOfArticles;
    }

    public static void setListOfArticles(List<String> articlesNames){

        mListOfArticles.clear();
        int arrSize = articlesNames.size();
        for(int i = 0; i < arrSize; i++){
            String name = articlesNames.get(i);
            mListOfArticles.add(name);
        }
    }
}
