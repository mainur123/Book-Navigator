package com.example.hp.minted2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class BookListPhy extends AppCompatActivity {

    LinearLayoutManager mLayoutManager; //for sorting
    SharedPreferences mSharedPref; //for saving sort settings
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_phy);
        //actionbar
        ActionBar actionBar = getSupportActionBar();

        //title
        actionBar.setTitle("Books");

        mSharedPref = getSharedPreferences("SortSettings", MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort", "newest"); /*where if no settings is selected newest will
                                                                                   be default*/

        //since default value is newest. so for first time it will display newest books first


        if(mSorting.equals("newest")){
            mLayoutManager = new LinearLayoutManager(this);
            //this will load the items from bottom means newest first
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
        }
        else if(mSorting.equals("oldest")){
            mLayoutManager = new LinearLayoutManager(this);
            //this will load the items from bottom means oldest first
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false);
        }

        //RecyclerView
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        //set layout as linear layout
        mRecyclerView.setLayoutManager(mLayoutManager);

        //send Query to Database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Phy");
    }

    //search data
    private void firebaseSearch(String searchText){
        Query firebaseSearchQuery = mRef.orderByChild("title").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerAdapter<Books, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Books, ViewHolder>(
                        Books.class,
                        R.layout.row,
                        ViewHolder.class,
                        firebaseSearchQuery

                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Books model, int position) {

                        viewHolder.setDetails(getApplicationContext(), model.getTitle(),model.getDescription(),model.getImage());

                    }


                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //get data from firebase at the position clicked
                                String mTitle = getItem(position).getTitle();
                                String mDesc = getItem(position).getDescription();
                                String mImage = getItem(position).getImage();

                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                                intent.putExtra("title", mTitle); //put title
                                intent.putExtra("description", mDesc); //put description
                                intent.putExtra("image", mImage); //put image url
                                startActivity(intent); //start activity


                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //TODO do tour own implementation on long item click

                            }
                        });

                        return viewHolder;
                    }



                };

        //set adapter to RecyclerView
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    //load data into RecyclerView OnStart

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Books, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Books, ViewHolder>(
                        Books.class,
                        R.layout.row,       //row layout
                        ViewHolder.class,   //view holder class
                        mRef                //ref
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Books model, int position) {

                        viewHolder.setDetails(getApplicationContext(), model.getTitle(),model.getDescription(),model.getImage());

                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                //get data from firebase at the position clicked
                                String mTitle = getItem(position).getTitle();
                                String mDesc = getItem(position).getDescription();
                                String mImage = getItem(position).getImage();

                                //pass this data to new activity
                                Intent intent = new Intent(view.getContext(), PostDetailActivity.class);
                                intent.putExtra("title", mTitle); //put title
                                intent.putExtra("description", mDesc); //put description
                                intent.putExtra("image", mImage); //put image url
                                startActivity(intent); //start activity


                            }

                            @Override
                            public void onItemLongClick(View view, int position) {
                                //for long item click not used for book navigator

                            }
                        });

                        return viewHolder;
                    }

                };

        //set adapter to RecyclerView
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu; this adds items to the action bar if it present
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //filter
                firebaseSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //handle other action bar item clicks here
        if(id == R.id.action_sort){
            //display alert dialog to choose sorting
            showSortDialog();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {
        //options to display in dialog
        String[] sortOptions = {"Newest", "Oldest"};
        //create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort by")
                .setIcon(R.drawable.ic_action_name) //set icon
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //The 'which' argument contains the index position of the selected item
                        // 0 means "Newest" and 1 means "oldest"
                        if(which == 0){
                            //sort by newest
                            //edit out shared preferences
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort", "newest"); //where 'Sort' is key and 'newest' is value
                            editor.apply(); //apply or save the value in our shared preferences
                            recreate(); //restart activity to take effect
                        }
                        else if(which == 1){{
                            //sort by oldest
                            //edit out shared preferences
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort", "oldest"); //where 'Sort' is key and 'oldest' is value
                            editor.apply(); //apply or save the value in our shared preferences
                            recreate(); //restart activity to take effect
                        }}
                    }
                });
        builder.show();
    }

}
