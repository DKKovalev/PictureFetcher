package com.playground.dkkovalev.picturefetcher.Assets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.playground.dkkovalev.picturefetcher.Model.FlickrPhotoObject;
import com.playground.dkkovalev.picturefetcher.R;
import com.playground.dkkovalev.picturefetcher.Tasks.ImageLoaderTask;

import java.util.ArrayList;

/**
 * Created by d.kovalev on 12.04.2016.
 */

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder> {

    private static final String LOGTAG = "CustomAdapter TAG";
    private ArrayList<FlickrPhotoObject> galleryItems;
    private CacheingHandler cacheingHandler;

    private Context context;

    //private OnRecyclerItemClick onRecyclerItemClick;

    public CustomRecyclerAdapter(Context context, ArrayList<FlickrPhotoObject> galleryItems) {
        super();

        this.context = context;
        this.galleryItems = galleryItems;
        cacheingHandler = new CacheingHandler();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, null);
        CustomViewHolder customViewHolder = new CustomViewHolder(view);
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {

        //TODO Main fetching goes there

        FlickrPhotoObject galleryItem = galleryItems.get(position);
        holder.photoView.setTag(galleryItem.getUrl());

        holder.photoView.setImageResource(R.drawable.placeholder);

        Bitmap photo = cacheingHandler.loadBitmapFromLru(galleryItems.get(position).getUrl());

        if (photo != null) {
            holder.photoView.setImageBitmap(photo);
        } else {
            new ImageLoaderTask(holder.photoView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, galleryItem.getUrl());
        }

        /*try {
                Bitmap bitmap = cacheingHandler.retrieveBitmapFromExternalStorage(fromPath.getAbsolutePath());
                if (bitmap != null) {
                    holder.photoView.setImageBitmap(bitmap);
            }
             else {
                Log.i(LOGTAG, "List is empty");
                //
                    new ImageLoaderTask(holder.photoView, context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, galleryItem.getUrl());
            }
            //
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }*/
    }

    /*public void setOnRecyclerItemClick(OnRecyclerItemClick onRecyclerItemClick) {
        this.onRecyclerItemClick = onRecyclerItemClick;
    }*/

    @Override
    public int getItemCount() {
        return galleryItems.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView photoView;

        public CustomViewHolder(View itemView) {
            super(itemView);

            photoView = (ImageView) itemView.findViewById(R.id.iv_photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();

            Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog);
            ImageView imageView = (ImageView)dialog.findViewById(R.id.iv_big_photo);
            imageView.setTag(galleryItems.get(pos).getUrl());
            new ImageLoaderTask(imageView).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, galleryItems.get(pos).getUrl());
            dialog.show();
        }
    }
}
