package com.dealwala.main.dealwala.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dealwala.main.dealwala.R;
import com.dealwala.main.dealwala.model.DealDataModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by divine on 02/05/16.
 */
public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.MyViewHolder> {

    private ArrayList<DealDataModel> dataList;
    private Activity context;

    HashMap<String,Handler> handlerHashMap = new HashMap<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtDealTitle, txtOgPrice, txtDiscPrice, txtShopName, txtDealDesc, txtDealEnd, txtDistance, txtRedeem;
        LinearLayout rootLay;

        public MyViewHolder(View view) {
            super(view);
            txtDealTitle = (TextView) view.findViewById(R.id.txtDealTitle);
            txtOgPrice = (TextView) view.findViewById(R.id.txtOriginalPrice);
            txtDiscPrice = (TextView) view.findViewById(R.id.txtDiscountPrice);
            txtDealEnd = (TextView) view.findViewById(R.id.txtDealEnd);
            txtShopName = (TextView) view.findViewById(R.id.txtShopName);
            txtDealDesc = (TextView) view.findViewById(R.id.txtDealDesc);
            txtDistance = (TextView) view.findViewById(R.id.txtShopDistance);
            txtRedeem = (TextView) view.findViewById(R.id.txtRedeem);
            rootLay = (LinearLayout) view.findViewById(R.id.rootLay);
        }
    }

    public RecyclerViewDataAdapter(Activity context, ArrayList<DealDataModel> dataList) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_product_active, parent, false);

        Log.v("Notification","On create View Holder");

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final DealDataModel data = dataList.get(position);
        holder.txtDistance.setText("1.5 km");
        holder.txtDealDesc.setText(data.getDealDesc());

        holder.txtDealEnd.setText(getDiffTime(data.getDealEndDate()));
        holder.txtShopName.setText(data.getShopName());
        holder.txtDealTitle.setText(data.getDealTitle());

        final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
        String orgPrice = context.getResources().getString(R.string.Rs) + " " + data.getDealAmount();
        holder.txtOgPrice.setText(orgPrice, TextView.BufferType.SPANNABLE);
        Spannable spannable = (Spannable) holder.txtOgPrice.getText();
        spannable.setSpan(STRIKE_THROUGH_SPAN, 0, orgPrice.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        /*String discPrice = context.getResources().getString(R.string.Rs) + " " + data.getOriginalValue();
        holder.txtDiscPrice.setText(discPrice);*/
        if(data.getDiscountType().equals("1")){
            long originalValue = Long.parseLong(data.getDealAmount());
            long discountValue = Long.parseLong(data.getDiscountValue());
            long discountPrice = originalValue - (originalValue/100 * discountValue);
            String discPrice = context.getResources().getString(R.string.Rs)+" "+discountPrice;
            holder.txtDiscPrice.setText(discPrice);
        }else{
            String discPrice = context.getResources().getString(R.string.Rs)+" "+data.getDiscountValue();
            holder.txtDiscPrice.setText(discPrice);
        }

        if (data.getIsActive().equals("0")) {
            holder.txtDealEnd.setEnabled(false);
            holder.txtDealEnd.setTextColor(context.getResources().getColor(R.color.light_grey));
            holder.txtRedeem.setEnabled(false);
            holder.txtRedeem.setTextColor(context.getResources().getColor(R.color.light_grey));
            holder.txtDealEnd.setText("Ended on " + data.getDealEndDate());
        } else {
            holder.txtDealEnd.setEnabled(true);
            holder.txtDealEnd.setTextColor(context.getResources().getColor(R.color.text_blue));
            holder.txtRedeem.setEnabled(true);
            holder.txtRedeem.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.txtDealEnd.setText(getDiffTime(data.getDealEndDate()));

            if(!handlerHashMap.containsKey(data.getDealId())){

                Handler handler = new Handler();

                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        holder.txtDealEnd.setText(getDiffTime(data.getDealEndDate()));
                        notifyDataSetChanged();
                    }
                };

                handler.postDelayed(runnable,1000);
                handlerHashMap.put(data.getDealId(),handler);

                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        handlerHashMap.get(data.getDealId()).postDelayed(runnable,1000);
                    }
                };
                timer.schedule(timerTask, 0, 1000);
            }
        }

        holder.txtRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("dealId", data.getDealId());
                context.startActivity(intent);
            }
        });

        holder.rootLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("dealId", data.getDealId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public String getDiffTime(String endDate) {

        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date fromDate = null;
        Date toDate = null;
        String dateFormat = "Ends in ";
        try {
            fromDate = new Date();
            toDate = sdf.parse(endDate);
            long diff = toDate.getTime() - fromDate.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            dateFormat +=  diffDays + ":" + diffHours + ":" + diffMinutes + ":" + diffSeconds;
            //dateFormat +=  diffDays + "days " + diffHours + "Hrs " + diffMinutes + "Min " + diffSeconds;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //System.out.println(dateFormat);
        return dateFormat;
    }
}
