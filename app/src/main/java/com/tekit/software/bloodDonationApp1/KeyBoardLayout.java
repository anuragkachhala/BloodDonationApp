package com.tekit.software.bloodDonationApp1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class KeyBoardLayout extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_NUMBER = 0;
    private static final int VIEW_TYPE_DELETE = 1;
    private Context context;
    private OnNumberClickListener mOnNumberClickListener;
    private OnDeleteClickListener mOnDeleteClickListener;
    private int mPinLength = 0;
    private int[] mKeyValues;

    public KeyBoardLayout(Context context, OTPActivity onNumberClickListener, OnDeleteClickListener onDeleteClickListener) {
        this.context = context;
        this.mOnNumberClickListener = onNumberClickListener;
        this.mOnDeleteClickListener = onDeleteClickListener;
        this.mKeyValues = getAdjustKeyValues(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0});


    }



    public void setPinLength(int pinLength) {
        this.mPinLength = pinLength;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_NUMBER) {
            View view = inflater.inflate(R.layout.grid_number_layout, parent, false);
            viewHolder = new NumberViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.grid_delete_layout, parent, false);
            viewHolder = new DeleteViewHolder(view);
        }
        return viewHolder;


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_NUMBER) {
            NumberViewHolder vh1 = (NumberViewHolder) holder;
            configureNumberButtonHolder(vh1, position);
        } else if (holder.getItemViewType() == VIEW_TYPE_DELETE) {
            DeleteViewHolder vh2 = (DeleteViewHolder) holder;
            configureDeleteButtonHolder(vh2);
        }
    }


    private void configureNumberButtonHolder(NumberViewHolder holder, int position) {
        if (holder != null) {
            if (position == 9) {
                holder.mNumberButton.setVisibility(View.GONE);
            } else {
                holder.mNumberButton.setText(String.valueOf(mKeyValues[position]));
                holder.mNumberButton.setVisibility(View.VISIBLE);
                holder.mNumberButton.setTag(mKeyValues[position]);
            }


        }
    }

    private void configureDeleteButtonHolder(DeleteViewHolder holder) {
        if (mPinLength == 0) {
            holder.mButtonImage.setVisibility(View.GONE);
        } else {
            /*holder.mButtonImage.setColorFilter(R.color.colorEtBg,
                    PorterDuff.Mode.SRC_ATOP);*/
            holder.mButtonImage.setVisibility(View.VISIBLE);
            holder.mButtonImage.setColorFilter(ContextCompat.getColor(context, R.color.buttonTextColor), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return VIEW_TYPE_DELETE;
        }
        return VIEW_TYPE_NUMBER;
    }


    private int[] getAdjustKeyValues(int[] keyValues) {
        int[] adjustedKeyValues = new int[keyValues.length + 1];
        for (int i = 0; i < keyValues.length; i++) {
            if (i < 9) {
                adjustedKeyValues[i] = keyValues[i];
            } else {
                adjustedKeyValues[i] = -1;
                adjustedKeyValues[i + 1] = keyValues[i];
            }
        }
        return adjustedKeyValues;
    }

    public interface OnNumberClickListener {
        void onNumberClicked(int keyValue);
    }

    public interface OnDeleteClickListener {
        void onDeleteClicked();

        void onDeleteLongClicked();
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder {
        Button mNumberButton;

        public NumberViewHolder(final View itemView) {
            super(itemView);
            mNumberButton = (Button) itemView.findViewById(R.id.button);
            mNumberButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnNumberClickListener != null) {
                        mOnNumberClickListener.onNumberClicked((Integer) v.getTag());
                    }
                }
            });
        }
    }

    public class DeleteViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mDeleteButton;
        ImageView mButtonImage;

        public DeleteViewHolder(final View itemView) {
            super(itemView);
            mDeleteButton = (LinearLayout) itemView.findViewById(R.id.button);
            mButtonImage = (ImageView) itemView.findViewById(R.id.buttonImage);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnDeleteClickListener != null) {
                        mOnDeleteClickListener.onDeleteClicked();
                    }
                }
            });


        }
    }
}
