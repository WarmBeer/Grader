package mickvd.grader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText studentID;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        studentID = (EditText)findViewById(R.id.studentID);
        button = (Button) findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signIn();
            }
        });
    }

    public void signIn()
    {
        String student_id = studentID.getText().toString();
        if (student_id.isEmpty()) {
            Toast.makeText(this, "Student ID is empty.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Signed in as: " + student_id, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("studentID",student_id);
            startActivity(intent);
        }
    }
}
