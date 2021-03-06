package anuja.project.finalproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import anuja.project.finalproject.R;
import anuja.project.finalproject.data.NewsContract;
import anuja.project.finalproject.fragment.DetailFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by USER on 02-12-2017.
 */

public class NewsAdapter
        extends RecyclerView.Adapter<NewsAdapter.MyViewHolder>
{
    String TAG = NewsAdapter.class.getSimpleName();
    CursorAdapter cursorAdapter;
    Context mContext;


    public NewsAdapter(Context context, Cursor c){
        mContext=context;
        cursorAdapter = new CursorAdapter(context,c,0) {

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
                NewsAdapter.MyViewHolder myviewHolder = new NewsAdapter.MyViewHolder(v,mContext,cursorAdapter);
                v.setTag(myviewHolder);
                return v;
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {

                NewsAdapter.MyViewHolder viewHolder = (NewsAdapter.MyViewHolder) view.getTag();
                viewHolder.item_name.setText(cursor.getString(cursor.getColumnIndex(
                        NewsContract.NewsEntry.COLUMN_TITLE)));
                viewHolder.siteName.setText(cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_SITE)));

                if(cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_IMAGE))!= null &&
                        cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_IMAGE)).length()>=1){
                    Picasso.with(context).load(cursor.getString(cursor.getColumnIndex(
                            NewsContract.NewsEntry.COLUMN_IMAGE
                    ))).fit().into(viewHolder.imageView);
                }else{
                    viewHolder.imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.sale_default));
                }
            }
        };
    }

    public void swap(Cursor c){
        cursorAdapter.swapCursor(c);
        notifyDataSetChanged();
    }

    @Override
    public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = cursorAdapter.newView(mContext,cursorAdapter.getCursor(),parent);
        return new NewsAdapter.MyViewHolder(view,mContext,cursorAdapter);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.MyViewHolder holder, int position) {

        cursorAdapter.getCursor().moveToPosition(position);
        cursorAdapter.bindView(holder.itemView,mContext,cursorAdapter.getCursor());

    }

    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.image_view)
        ImageView imageView;
        @BindView(R.id.item_name)
        TextView item_name;
        @BindView(R.id.site_name)
        TextView siteName;

        CursorAdapter mCursorAdapter;
        Context mContext;

        public MyViewHolder(View itemView) {
            super(itemView);
        }

        public MyViewHolder(View itemView, Context con, CursorAdapter cursor) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            mCursorAdapter=cursor;
            mContext = con;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor cursor =(Cursor) mCursorAdapter.getItem(getAdapterPosition());
                    long index = cursor.getLong(cursor.getColumnIndex(NewsContract.NewsEntry._ID));
                    Uri selectedUri= NewsContract.NewsEntry.getUriWithId(index);
                    Intent intent = new Intent(mContext, DetailFragment.class);
                    ((NewsAdapter.CallBack)mContext).ItemSelected(selectedUri);
                }
            });

        }

    }

    public interface CallBack {
        void ItemSelected(Uri selectedUri);
    }

}