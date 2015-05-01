package cat.santi.onedot.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import cat.santi.onedot.R;
import cat.santi.onedot.fragments.MainFragment;

/**
 *
 */
public class MainActivity extends ActionBarActivity {

    private static final String FRAGMENT_MAIN = "FRAGMENT_MAIN";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.od__activity_main);

        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
            .add(R.id.od__main__container, MainFragment.newInstance(), FRAGMENT_MAIN)
            .commit();
    }
}
