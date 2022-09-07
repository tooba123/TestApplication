package com.example.testapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testapplication.R;
import com.example.testapplication.data.db.models.AppInfo;

import java.util.ArrayList;


public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private ArrayList<AppInfo> _appInfoList;
    private LayoutInflater _layoutInflator;


    public AppListAdapter(ArrayList<AppInfo> app_info_list, Context context){
        _layoutInflator = LayoutInflater.from(context);
        _appInfoList = app_info_list;

        if(_appInfoList == null){
            _appInfoList = new ArrayList<AppInfo>();
        }
    }

    @NonNull
    @Override
    public AppListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = _layoutInflator.inflate(R.layout.layout_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppListAdapter.ViewHolder holder, int position) {
        holder.tvPackageName.setText(_appInfoList.get(position).getPackageName());
        holder.tvAppName.setText(_appInfoList.get(position).getAppName());
    }

    public ArrayList<AppInfo> getItems(){
        return _appInfoList;
    }

    @Override
    public int getItemCount() {
        return _appInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvAppName;
        TextView tvPackageName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView){
            tvAppName = itemView.findViewById(R.id.tv_app_name);
            tvPackageName = itemView.findViewById(R.id.tv_package_name);
        }

    }
}
