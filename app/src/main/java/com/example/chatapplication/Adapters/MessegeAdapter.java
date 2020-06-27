package com.example.chatapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.Model.Messege;
import com.example.chatapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessegeAdapter extends RecyclerView.Adapter<MessegeAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Messege> mMsg;
    private String imageURL;

    private FirebaseUser firebaseUser;

    public MessegeAdapter(Context context,List<Messege> msg,String imageURL){
        this.mContext = context;
        this.mMsg = msg;
        this.imageURL = imageURL;
    }

    @NonNull
    @Override
    public MessegeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessegeAdapter.ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessegeAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessegeAdapter.ViewHolder holder, int position) {

        Messege msg = mMsg.get(position);
        holder.show_msg.setText(msg.getMessege());
        if(imageURL.equals("default")){
            holder.profile_image.setImageResource(R.drawable.dp);
        } else {
            Glide.with(mContext).load(imageURL).into(holder.profile_image);
        }

        if(position==mMsg.size()-1){
            if (msg.isseen){
                holder.seen.setText("Seen");
            }else{
                holder.seen.setText("Delievered");
            }
        }else{
            holder.seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mMsg.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_msg;
        public CircleImageView profile_image;
        TextView seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_msg      = itemView.findViewById(R.id.show_messege);
            profile_image = itemView.findViewById(R.id.profile_image);
            seen = itemView.findViewById(R.id.seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mMsg.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}

