package com.example.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.Adapters.MessegeAdapter;
import com.example.chatapplication.Model.Messege;
import com.example.chatapplication.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessegeActivity extends AppCompatActivity {

    CircleImageView imageView;
    TextView username;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;
    ImageView btn_send;
    EditText text_send;

    MessegeAdapter messegeAdapter;
    RecyclerView recyclerView;

    ValueEventListener seenListener;

    List<Messege> msgs;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messege);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessegeActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });
        imageView  = findViewById(R.id.profile_image);
        username   = findViewById(R.id.username);
        btn_send   = findViewById(R.id.btn_send);
        text_send   = findViewById(R.id.text_send);
        intent     = getIntent();
        userid = intent.getStringExtra("userid");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messege = text_send.getText().toString();
                if(!messege.equals("")){
                    sendMessege(firebaseUser.getUid(),userid,messege);
                }
                text_send.setText("");
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference    = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    imageView.setImageResource(R.drawable.dp);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(imageView);
                }
                readMessege(firebaseUser.getUid(),userid,user.imageURL);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seenMessege(userid);

    }


    private void seenMessege(final String userId){
        final String myid = firebaseUser.getUid();
        String uniqUid;
        if(myid.compareTo(userid)>0){
            uniqUid = userid+myid;
        }else{
            uniqUid=  myid+userid;
        }

        reference = FirebaseDatabase.getInstance().getReference("Messeges").child(uniqUid);
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Messege msg = snapshot.getValue(Messege.class);
                    if (msg.getSender().equals(userId)) {
                        if (msg.getReceiver().equals(myid)) {
                            if(!msg.isseen) {
                                HashMap<String, Object> mymap = new HashMap<>();

                                snapshot.getRef().updateChildren(mymap);
                            }
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void  sendMessege(final String sender, final String receiver, final String messege){

        reference = FirebaseDatabase.getInstance().getReference();
        Messege messege1 = new Messege(sender,receiver,messege,false);
        String uniqUid;
        if(receiver.compareTo(sender)>0){
            uniqUid = sender+receiver;
        }else{
            uniqUid=  receiver+sender;
        }
        reference.child("Messeges").child(uniqUid).push().setValue(messege1);

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(sender)
                .child(receiver);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(receiver)
                .child(sender);

        chatRef2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef2.child("id").setValue(sender);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readMessege(final String myid, final String userid, final String imageUrl){
        msgs = new ArrayList<>();
        String uniqUid;
        if(myid.compareTo(userid)>0){
            uniqUid = userid+myid;
        }else{
            uniqUid=  myid+userid;
        }
        Log.v("uuid ",uniqUid);
        reference = FirebaseDatabase.getInstance().getReference("Messeges").child(uniqUid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                msgs.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Messege msg = snapshot.getValue(Messege.class);

                    if(msg.getReceiver().equals(myid) && msg.getSender().equals(userid) ||
                        msg.getReceiver().equals(userid) && msg.getSender().equals(myid)){

                        msgs.add(msg);
                    }

                }
                messegeAdapter  = new MessegeAdapter(MessegeActivity.this,msgs,imageUrl);
                recyclerView.setAdapter(messegeAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(userid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }
}
