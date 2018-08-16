package my.edu.tarc.smartlighting.Model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.time.Period;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import my.edu.tarc.smartlighting.R;

/**
 * Created by ken_0 on 17/8/2018.
 */

public class ConnectionAdapter extends BaseAdapter {
    Context context;
    List<Connection> connectionList;

    public ConnectionAdapter(Context context, List<Connection> connectionList){
        this.context = context;
        this.connectionList = connectionList;
    }


    @Override
    public int getCount() {
        return connectionList.size();
    }

    @Override
    public Object getItem(int i) {
        return connectionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return connectionList.indexOf(getItem(i));
    }

    private class ViewHolder{
        TextView textViewConnectedTime;
        TextView textViewDisconnectedTime;
        TextView textViewUpTime;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null){
            view = inflater.inflate(R.layout.connection_record, null);
            holder = new ViewHolder();

            holder.textViewConnectedTime = (TextView) view.findViewById(R.id.textViewConnectedTime);
            holder.textViewDisconnectedTime = (TextView) view.findViewById(R.id.textViewDisconnectedTime);
            holder.textViewUpTime = (TextView) view.findViewById(R.id.textViewUpTime);

            Calendar connectedTime, disconnectedTime;
            long upTimeInSeconds = 0;
            Connection connection = connectionList.get(i);
            connectedTime = connection.getConnectedTime();
            disconnectedTime = connection.getDisconnectedTime();
            holder.textViewConnectedTime.
                    setText(String.format("%02d/%02d/%04d %02d:%02d:%02d",
                            connectedTime.get(Calendar.DAY_OF_MONTH),
                            (connectedTime.get(Calendar.MONTH)+1),
                            connectedTime.get(Calendar.YEAR),
                            connectedTime.get(Calendar.HOUR_OF_DAY),
                            connectedTime.get(Calendar.MINUTE),
                            connectedTime.get(Calendar.SECOND)));
            if (disconnectedTime != null){
                upTimeInSeconds = (disconnectedTime.getTimeInMillis() - connectedTime.getTimeInMillis()) / 1000;
                holder.textViewDisconnectedTime.
                        setText(String.format("%02d/%02d/%04d %02d:%02d:%02d",
                                disconnectedTime.get(Calendar.DAY_OF_MONTH),
                                (disconnectedTime.get(Calendar.MONTH)+1),
                                disconnectedTime.get(Calendar.YEAR),
                                disconnectedTime.get(Calendar.HOUR_OF_DAY),
                                disconnectedTime.get(Calendar.MINUTE),
                                disconnectedTime.get(Calendar.SECOND)));
            } else {
                holder.textViewDisconnectedTime.setText("-");
                if (i == 0){
                    disconnectedTime = new GregorianCalendar();
                    upTimeInSeconds = (disconnectedTime.getTimeInMillis() - connectedTime.getTimeInMillis()) / 1000;
                }
            }

            int seconds, minutes, hours ;
            minutes = (int) upTimeInSeconds / 60;
            seconds = (int) upTimeInSeconds % 60;
            hours = (int) minutes / 60;
            minutes = (int) minutes % 60;
            if (hours == 0){
                if (minutes == 0){
                    if (seconds == 0){
                        holder.textViewUpTime.setText("-");
                    } else {
                        holder.textViewUpTime.setText(seconds + " seconds");
                    }
                } else {
                    holder.textViewUpTime.setText(minutes + " minutes " + seconds + " seconds");
                }
            } else {
                holder.textViewUpTime.setText(hours + " hour " + minutes + " minutes " + seconds + " seconds");
            }
            view.setTag(holder);
        }
        return view;
    }
}
