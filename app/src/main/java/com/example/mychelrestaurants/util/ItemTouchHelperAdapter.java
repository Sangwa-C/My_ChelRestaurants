package com.example.mychelrestaurants.util;

import com.example.mychelrestaurants.adapters.FirebaseRestaurantViewHolder;
import com.example.mychelrestaurants.models.Restaurant;

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);

    void onBindViewHolder(FirebaseRestaurantViewHolder viewHolder, Restaurant model, int position);
}