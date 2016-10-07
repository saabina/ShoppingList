package fi.jamk.shoppinglist;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity implements AddProductDialogFragment.DialogListener {

    private MyListFragment fragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new MyListFragment();
        fragmentTransaction.add(R.id.list_fragment, fragment);
        fragmentTransaction.commit();
    }


    public void OnAddButtonClick(View view) {
        AddProductDialogFragment eDialog = new AddProductDialogFragment();
        eDialog.show(getFragmentManager(), "Add a new product");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String product, int count, float price) {

        fragment.addItem(product,count,price);

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // TODO Auto-generated method stub

    }
}
