package com.sese.showmethebeer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryNestedAdapter extends RecyclerView.Adapter<CategoryNestedAdapter.NestedViewHolder> {

    private Context mContext;
    private List<NestedList> mList;

    public CategoryNestedAdapter(List<NestedList> mList){
        this.mList = mList;
    }
    @NonNull
    @Override
    public NestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_nested_item, parent , false);
        return new NestedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NestedViewHolder holder, int position) {
        holder.mTv.setText(mList.get(position).nestedName);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class NestedViewHolder extends RecyclerView.ViewHolder{
        private TextView mTv;
        public NestedViewHolder(@NonNull View itemView) {
            super(itemView);
            mTv = itemView.findViewById(R.id.nestedItemTv);


            itemView.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Log.d("hojae", "position = " + position + " mText:" + mList.get(position).nestedName);

                    Intent intent = new Intent(v.getContext(), BeerListActivity.class);
                    v.getContext().startActivity(intent);

                    //if(mContext instanceof BeerListActivity){
                    //    Intent intent = new Intent(mContext, BeerListActivity.class);
                    //    ((BeerListActivity)mContext).startActivity(intent);
                    //}


                    //Intent intent = new Intent(mContext, BeerListActivity.class);
                    //startActivity(intent);

                    //if(mListener != null)
                    //{
                    //    mListener.onItemClick(v, position);
                    //}
                }
            }));



            /*
            itemView.setOn(new View.OnClickListener(){
                @Override
                public void onClick(View view)
                {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){

                    }
                }
            });

             */
        }
    }
    private OnItemClickListener mListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
}
