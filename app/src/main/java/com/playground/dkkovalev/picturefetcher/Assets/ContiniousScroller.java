package com.playground.dkkovalev.picturefetcher.Assets;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by d.kovalev on 13.04.2016.
 */
public abstract class ContiniousScroller extends RecyclerView.OnScrollListener {

    private LinearLayoutManager layoutManager;

    private int scrollingThreshold = 20;
    private int firstPage = 0;
    private boolean isStillLoading = true;
    private int currentPage = 1;
    private int previousTotalItemCount = 0;

    public ContiniousScroller(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int itemCount = recyclerView.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.firstPage;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.isStillLoading = true;
            }
        }

        if (isStillLoading && (totalItemCount > previousTotalItemCount)) {
            isStillLoading = false;
            previousTotalItemCount = totalItemCount;
        }

        if (!isStillLoading && (totalItemCount - itemCount) <= (firstItem + scrollingThreshold)) {
            currentPage++;
            load(currentPage);
            isStillLoading = true;
        }
    }

    public abstract void load(int page);
}
