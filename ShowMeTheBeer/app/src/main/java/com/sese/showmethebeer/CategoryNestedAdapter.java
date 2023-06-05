package com.sese.showmethebeer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.sese.showmethebeer.manager.NetworkConnectionUtil;

import java.util.List;

public class CategoryNestedAdapter extends RecyclerView.Adapter<CategoryNestedAdapter.NestedViewHolder> {

    private Context mContext;

    private String parentCategory;
    private List<NestedList> mList;

    public CategoryNestedAdapter(List<NestedList> mList, String parentCategory){
        this.parentCategory = parentCategory;
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

                    if(!NetworkConnectionUtil.isNetworkAvailable(v.getContext()))
                    {
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("네트워크 에러")
                                .setMessage("네트워크가 연결 된 이후에 재시도 해주세요.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .create()
                                .show();
                        return;
                    }

                    int position = getAdapterPosition();

                    Intent intent = new Intent(v.getContext(), BeerListActivity.class);

                    intent.putExtra(Constants.INTENT_KEY_CALLER, Constants.INTENT_VAL_CATEGORY);
                    intent.putExtra(Constants.INTENT_KEY_CATEGORY_ID, mList.get(position).id);
                    v.getContext().startActivity(intent);
                }
            }));
        }
    }
}
