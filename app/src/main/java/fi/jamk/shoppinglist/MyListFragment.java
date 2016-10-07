package fi.jamk.shoppinglist;

import android.app.ListFragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MyListFragment extends ListFragment {

    private final String DATABASE_TABLE = "products";
    private final int DELETE_ID = 0;
    private SQLiteDatabase db;
    private Cursor cursor;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.list_item, container, false);

        setRetainInstance(true);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get database instance
        db = (new DatabaseOpenHelper(getActivity())).getWritableDatabase();
        // get data with own made queryData method
        queryData();
    }

    // query data from database
    public void queryData() {
        //cursor = db.rawQuery("SELECT _id, name, score FROM highscores ORDER BY score DESC", null);
        // get data with query
        String[] resultColumns = new String[]{"_id","product","count","price"};
        cursor = db.query(DATABASE_TABLE,resultColumns,null,null,null,null,null,null);

        // add data to adapter
        ListAdapter adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.row, cursor,
                new String[] {"product", "count", "price"},      // from
                new int[] {R.id.prod, R.id.count, R.id.price}    // to
                ,0);  // flags

        // show data in listView
        getListView().setAdapter(adapter);
        showTotalPrice();
    }

    public void showTotalPrice() {
        float wholePrice = 0f;
        if (cursor.moveToFirst()) {
            do {
                float price = cursor.getInt(2) * cursor.getFloat(3); // columnIndex
                wholePrice += price;
            } while (cursor.moveToNext());
            Toast.makeText(getActivity().getBaseContext(), "Total price: " + wholePrice, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //long press on item
        registerForContextMenu(getListView());
    }

    public void addItem(String product, int count, float price) {
        ContentValues values=new ContentValues(2);
        values.put("product", product);
        values.put("count", count);
        values.put("price", price);
        db.insert("products", null, values);
        // get data again
        queryData();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                String[] args = {String.valueOf(info.id)};
                db.delete(DATABASE_TABLE, "_id=?", args);
                queryData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // close cursor and db connection
        cursor.close();
        db.close();
    }
}
