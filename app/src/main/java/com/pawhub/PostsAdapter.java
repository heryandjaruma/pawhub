package com.pawhub;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pawhub.implementation.UserRepositoryImpl;
import com.pawhub.model.Post;
import com.pawhub.model.User;
import com.pawhub.repository.UserRepository;
import com.pawhub.utils.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private ArrayList<Post> posts = new ArrayList<>();
    private UserRepository userRepository = new UserRepositoryImpl();

    public PostsAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.post_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        Log.d("PHLOG","User id " + post.getUid());
        userRepository.getUser(post.getUid(), new Callback<User>() {
            @Override
            public void onSuccess(User result) {
                holder.postUsername.setText(result.getUsername());
                Picasso.get().load(result.getProfile_picture()).into(holder.pp);
                Picasso.get().load(post.getImage()).into(holder.contentImage);
                Log.d("PHLOG", "Load profile success");
            }
            @Override
            public void onFailure(Exception e) {
                Log.e("PHLOG", "onFailure: ", e);
            }
        });



    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView pp, contentImage;
        private TextView postUsername;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            pp = itemView.findViewById(R.id.ppImage);
            contentImage = itemView.findViewById(R.id.content_image);
            postUsername = itemView.findViewById(R.id.postUsername);
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            notifyDataSetChanged();
        }
    }
}
