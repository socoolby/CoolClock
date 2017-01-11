package clock.socoolby.com.clock;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import clock.socoolby.com.clock.utils.FuncUnit;

/**
 * Alway zuo,never die.
 * Created by socoolby on 04/01/2017.
 */

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView version=(TextView)findViewById(R.id.version);
        version.setText(getString(R.string.version)+FuncUnit.getVersionName(FuncUnit.getBoxPackageName()));
    }
}
