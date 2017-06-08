package com.example.shaddox.mylittleshop;

/**
 * Created by Shaddox on 2017. 06. 07..
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaddox.mylittleshop.data.ItemContract;
import com.example.shaddox.mylittleshop.data.ItemContract.ItemEntry;

import static com.example.shaddox.mylittleshop.R.id.quantity;

public class ItemCursorAdapter extends CursorAdapter {

    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

//    private int mRowsAffected;
//    private Context mContext;
//    private String mQuantitySold;

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the item data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current item can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        final TextView nameTextView = (TextView) view.findViewById(R.id.name);
        final TextView priceTextView = (TextView) view.findViewById(R.id.price);
        final TextView quantityTextView = (TextView) view.findViewById(quantity);
        final int mRowId = cursor.getInt(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry._ID));

        // Find the columns of item attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(ItemEntry.COLUMN_ITEM_QUANTITY);

        // Read the item attributes from the Cursor for the current item
        String itemName = cursor.getString(nameColumnIndex);
        int itemPrice = cursor.getInt(priceColumnIndex);
        int itemQuantity = cursor.getInt(quantityColumnIndex);


        // Update the TextViews with the attributes for the current item
        nameTextView.setText(itemName);
        priceTextView.setText(String.valueOf(itemPrice));
        quantityTextView.setText(String.valueOf(itemQuantity));

        Button sellButton = (Button) view.findViewById(R.id.button_sell);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemCursorAdapter.productSale(context, quantityTextView, mRowId);
                }
        });
    }

    public static int productSale(Context context, TextView quantity, int mRowId) {
        int units = Integer.parseInt(quantity.getText().toString());
        int mRowsAffected = 0;
        if (units > 0) {
            units--;
            String mQuantitySold = Integer.toString(units);
            ContentValues values = new ContentValues();
            values.put(ItemEntry.COLUMN_ITEM_QUANTITY, mQuantitySold);
            Uri currentProductUri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, mRowId);
            mRowsAffected = context.getContentResolver().update(currentProductUri, values, null, null);
        } else {
            Toast.makeText(context, "No more product left to sell!", Toast.LENGTH_SHORT).show();
        }
        return mRowsAffected;
    }
}
