package com.example.bluetoothapplication;

import java.util.List;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;


public class MyAdapter extends BaseExpandableListAdapter{
    private List<ExpandInfo> list;
    private Context ctx;

    public MyAdapter(Context ctx,List<ExpandInfo> list) {
        this.ctx=ctx;
        this.list=list;
    }
    //组数
    @Override
    public int getGroupCount() {
        return list.size();
    }
    //子数
    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).childList == null ? 0 : list.get(groupPosition).childList.size();
    }
    //组的对象
    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }
    //获得子的对象
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).childList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }
    //当子条目ID相同时是否复用
    @Override
    public boolean hasStableIds() {
        return true;
    }
    //isExpanded:展开
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView=View.inflate(ctx,R.layout.group_item, null);
        TextView groupTv=(TextView) convertView.findViewById(R.id.group_text);
        groupTv.setText(list.get(groupPosition).title);
        //组是否展开   如果展开，组变颜色
        if(isExpanded){
            groupTv.setTextColor(Color.BLUE);
        }else{
            groupTv.setTextColor(Color.BLACK);
        }
        return convertView;
    }
    //isLastChild:是否是该组最后子条目
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(ctx, R.layout.child_item, null);
            holder.bluetoothName=(TextView) convertView.findViewById(R.id.listview_bluetooth_name);
            holder.bluetoothAddress=(TextView) convertView.findViewById(R.id.listview_bluetooth_address);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder) convertView.getTag();
        }
       // holder.img.setImageResource(list.get(groupPosition).childList.get(childPosition).headID);
        holder.bluetoothName.setText(list.get(groupPosition).childList
                .get(childPosition).bluetoothName);
        holder.bluetoothAddress.setText(list.get(groupPosition).childList
                .get(childPosition).bluetoothAddress);
        //如果是最后一条，最后最后一条变色
        if(isLastChild){
            holder.bluetoothName.setTextColor(Color.GREEN);
        }else{
            holder.bluetoothName.setTextColor(Color.BLACK);
        }
        return convertView;
    }
    //子条目是否可以被点击/选中/选择
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }
    private class ViewHolder{
        private TextView bluetoothName;
        private TextView bluetoothAddress;
    }
}
