package com.example.thelibrary.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.braintreepayments.cardform.view.CardForm;
import com.example.thelibrary.R;
import com.example.thelibrary.fireBase.model.mAuthUser;

public class CreditCardActivity extends AppCompatActivity {

    CardForm cardForm;
    Button buy;
    double amount;
    AlertDialog.Builder alertBuilder;
    mAuthUser auth = new mAuthUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        Intent intent = getIntent();
        amount= intent.getExtras().getDouble("amount");
        cardForm = findViewById(R.id.card_form);
        buy = findViewById(R.id.btnBuy);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .setup(CreditCardActivity.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {
                    alertBuilder = new AlertDialog.Builder(CreditCardActivity.this);
                    alertBuilder.setTitle("אשר את פרטי התשלום");
                    alertBuilder.setMessage("סכום לתשלום: " +  "₪" +amount +"\n" +
                            "מספר כרטיס: " + cardForm.getCardNumber() + "\n" +
                            "תוקף: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                            "קוד CVV: " +cardForm.getCvv() + "\n" +
                            "מיקוד: " + cardForm.getPostalCode() + "\n" +
                            "מספר נייד: " + cardForm.getMobileNumber());
                    alertBuilder.setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            if(intent.getExtras().getString("type").equals("order")) {
                                MyOrderActivity.finishOrder();
                                Toast.makeText(CreditCardActivity.this, "ההזמנה הושלמה והועבר לטיפול", Toast.LENGTH_LONG).show();
                                Intent orIntnet = new Intent(CreditCardActivity.this, MenuUserActivity.class);
                                startActivity(orIntnet);
                            }
                                if(intent.getExtras().getString("type").equals("register")) {
                                    auth.registerUserToDB(intent.getExtras().getString("tz"),
                                            intent.getExtras().getString("fName"),
                                            intent.getExtras().getString("lName"),
                                            intent.getExtras().getString("email"),
                                            intent.getExtras().getString("password"),
                                            intent.getExtras().getString("address"),
                                            intent.getExtras().getString("phone"),
                                            intent.getExtras().getString("sub"));

                                }
                        }
                    });
                    alertBuilder.setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                 if(intent.getExtras().getString("type").equals("register")) {
                                     Toast.makeText(getApplicationContext(), "התשלום נכשל, ההרשמה לא הושלמה", Toast.LENGTH_LONG).show();
                                     Intent logIntnet = new Intent(CreditCardActivity.this, LoginUserActivity.class);
                                  startActivity(logIntnet);
                                 }
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();

                } else {
                    Toast.makeText(CreditCardActivity.this, "בבקשה מלא את כל הפרטים", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}