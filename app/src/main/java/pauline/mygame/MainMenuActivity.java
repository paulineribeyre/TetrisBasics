package pauline.mygame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("My Game :-)");
        setContentView(R.layout.activity_main_menu);
    }

    public void openGame(View view) {
        Intent intent = new Intent(MainMenuActivity.this, TetrisActivity.class);
        startActivity(intent);
    }

}
