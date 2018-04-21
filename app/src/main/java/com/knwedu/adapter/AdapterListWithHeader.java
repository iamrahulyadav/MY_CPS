package com.knwedu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.knwedu.calcuttapublicschool.R;
import com.knwedu.constant.StaticConstant;
import com.knwedu.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class AdapterListWithHeader extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MenuItem> items = new ArrayList<>();

    private Context ctx;
    private MenuItem header;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private OnItemClickListener mOnItemClickListener;
    private OnTutorialClickListener mOnTutorialClickListener;
    private boolean clicked = false;

    public interface OnItemClickListener {
        void onItemClick(View view, MenuItem obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public interface OnTutorialClickListener {
        void onTutorialClick(View view, MenuItem obj, int position);
    }

    public void setOnTutorialClickListener(final OnTutorialClickListener mTutorialClickListener) {
        this.mOnTutorialClickListener = mTutorialClickListener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterListWithHeader(Context context, MenuItem header, List<com.knwedu.model.MenuItem> items) {
        this.items = items;
        //this.header = header;
        ctx = context;
        for (int i = 0; i < items.size(); i++)
            Log.e("Items", "Item: " + items.get(i).getName());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView short_content;
        public TextView date;
        public ImageView image;
        public LinearLayout lyt_parent;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            // short_content = (TextView) v.findViewById(R.id.short_content);
            // date = (TextView) v.findViewById(R.id.date);
            image = (ImageView) v.findViewById(R.id.image);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
        }
    }

    public class ViewHolderCard extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView title;
        public TextView short_content;
        public TextView date;
        public ImageView image;
        public LinearLayout lyt_parent;
        public ImageButton ib_tutorial;

        public ViewHolderCard(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.text_name);
            short_content = (TextView) v.findViewById(R.id.text_content);
            date = (TextView) v.findViewById(R.id.text_date);
            image = (ImageView) v.findViewById(R.id.photo);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
            ib_tutorial = (ImageButton) v.findViewById(R.id.ib_tutorial);
        }
    }

    class ViewHolderHeader extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView date;
        public ImageView image;
        public LinearLayout lyt_parent;

        public ViewHolderHeader(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            //  date = (TextView) v.findViewById(R.id.date);
            image = (ImageView) v.findViewById(R.id.image);
            lyt_parent = (LinearLayout) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_subcat, parent, false);
        return new ViewHolderCard(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {

            Log.e("TAG", "Instance of view Holder");
            final MenuItem c = items.get(position);
            ViewHolder vItem = (ViewHolder) holder;
            vItem.title.setText(c.getCaption());
            vItem.short_content.setText(c.getDescription());
            vItem.date.setText(c.getDescription());
            //Picasso.with(ctx).load(c.getImage()).into(vItem.image);
            /*if ( position % 2 == 0 ){
                Picasso.with(ctx).load(c.getImage()).into(vItem.image);
                vItem.image.setImageDrawable(ctx.getResources().getDrawable(R.drawable.news_img_sc1));
            }*/
            vItem.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!clicked && mOnItemClickListener != null) {
                        clicked = true;
                        mOnItemClickListener.onItemClick(view, c, position);
                    }
                }
            });
        } else if (holder instanceof ViewHolderCard) {
            Log.e("TAG", "Instance of view Card holder");
            final MenuItem c = items.get(position);
            Log.e("TAG", "Role: " + c.getRole());
            Log.e("TAG", "ROLE_TEACHER: " + StaticConstant.ROLE_TEACHER);
            ViewHolderCard vItem = (ViewHolderCard) holder;
            vItem.title.setText(c.getCaption());
            vItem.short_content.setText(c.getDescription());
            vItem.date.setText(c.getDescription());
            // vItem.date.setText(c.getDate());
            if (c.getImage() != 0) {
                vItem.image.setImageDrawable(ctx.getResources().getDrawable(c.getImage()));
            }
            if (c.getRole().equalsIgnoreCase(StaticConstant.ROLE_TEACHER)) {
                Log.e("TAG", "Teacher");
                switch (c.getName()) {
                    case StaticConstant.ASSIGNMENT:
                        vItem.ib_tutorial.setVisibility(View.VISIBLE);
                        vItem.ib_tutorial.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnTutorialClickListener.onTutorialClick(v, c, position);
                            }
                        });
                        break;
                    case StaticConstant.TEST:
                        vItem.ib_tutorial.setVisibility(View.VISIBLE);
                        vItem.ib_tutorial.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnTutorialClickListener.onTutorialClick(v, c, position);
                            }
                        });
                        break;
                    case StaticConstant.CLASSWORK:
                        vItem.ib_tutorial.setVisibility(View.VISIBLE);
                        vItem.ib_tutorial.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnTutorialClickListener.onTutorialClick(v, c, position);
                            }
                        });
                        break;
                    case StaticConstant.ANNOUNCEMENT:
                        vItem.ib_tutorial.setVisibility(View.VISIBLE);
                        vItem.ib_tutorial.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnTutorialClickListener.onTutorialClick(v, c, position);
                            }
                        });
                        break;
                    case StaticConstant.ATTENDANCE:
                        vItem.ib_tutorial.setVisibility(View.VISIBLE);
                        vItem.ib_tutorial.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnTutorialClickListener.onTutorialClick(v, c, position);
                            }
                        });
                        break;
                    case StaticConstant.BEHAVIOUR:
                        vItem.ib_tutorial.setVisibility(View.VISIBLE);
                        vItem.ib_tutorial.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnTutorialClickListener.onTutorialClick(v, c, position);
                            }
                        });
                        break;
                    case StaticConstant.DAILY_DIARY:
                        vItem.ib_tutorial.setVisibility(View.VISIBLE);
                        vItem.ib_tutorial.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnTutorialClickListener.onTutorialClick(v, c, position);
                            }
                        });
                        break;
                }
            }
            if (c.getRole().equalsIgnoreCase(StaticConstant.ROLE_PARENT)) {
                Log.e("TAG", "Parent");
                switch (c.getName()) {
                    case StaticConstant.REQUESTS:
                        vItem.ib_tutorial.setVisibility(View.VISIBLE);
                        vItem.ib_tutorial.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnTutorialClickListener.onTutorialClick(v, c, position);
                            }
                        });
                        break;
                    case StaticConstant.SCHOOLREQUEST:
                        vItem.ib_tutorial.setVisibility(View.VISIBLE);
                        vItem.ib_tutorial.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mOnTutorialClickListener.onTutorialClick(v, c, position);
                            }
                        });
                        break;
                }
            }
            vItem.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clicked = true;
                    mOnItemClickListener.onItemClick(view, c, position);
                }
            });

        }
        clicked = false;
    }

    //    need to override this method
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public MenuItem getItem(int position) {
        return items.get(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

}