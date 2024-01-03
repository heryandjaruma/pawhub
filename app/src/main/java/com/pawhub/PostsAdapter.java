package com.pawhub;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pawhub.implementation.PostRepositoryImpl;
import com.pawhub.implementation.UserRepositoryImpl;
import com.pawhub.model.Post;
import com.pawhub.model.User;
import com.pawhub.repository.PostRepository;
import com.pawhub.repository.UserRepository;
import com.pawhub.utils.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private ArrayList<Post> posts = new ArrayList<>();
    private UserRepository userRepository = new UserRepositoryImpl();
    private PostRepository postRepository = new PostRepositoryImpl();

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
                holder.like_count.setText(post.getLike_count().toString());
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
        private ImageView pp, contentImage, like_tap;
        private TextView postUsername, like_count;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            pp = itemView.findViewById(R.id.ppImage);
            contentImage = itemView.findViewById(R.id.content_image);
            postUsername = itemView.findViewById(R.id.postUsername);
            like_count = itemView.findViewById(R.id.like_count);
            like_tap = itemView.findViewById(R.id.likeTap);

            like_tap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Post post = posts.get(position);
                        postRepository.updateLikeCount(post.getPost_id(), new Callback<Void>() {
                            @Override
                            public void onSuccess(Void result) {
                                int updatedLikes = post.getLike_count() + 1;
                                post.setLike_count(updatedLikes); // Update the model
                                like_count.setText(String.valueOf(updatedLikes));
                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition();
            notifyDataSetChanged();
        }
    }
}
