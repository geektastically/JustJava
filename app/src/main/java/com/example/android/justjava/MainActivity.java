package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends ActionBarActivity {
    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {

        //setup for Toast Notification used as a warning
        Context context = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;

      //If Statement to prevent 100 or more coffees, displays a toast
        if (quantity == 100) {
            displayQuantity(quantity);
            text = getString(R.string.max_100_warning);
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            quantity++;
            displayQuantity(quantity);
        }


    }

    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        //setup for Toast Notification used as warning
        Context context = getApplicationContext();
        CharSequence text;
        int duration = Toast.LENGTH_SHORT;

        //If Statement to prevent negative or zero coffees which displays a toast
        if (quantity == 0) {
            displayQuantity(quantity);
            text = getString(R.string.no_negative_warning);
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            quantity--;
            displayQuantity(quantity);
        }
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrders(View view) {
        //gather status of Checkboxes, Customer Name, Order Total
        boolean hasChocolate = ((CheckBox) findViewById(R.id.chocolate_checkbox)).isChecked();
        boolean hasWhippedCream = ((CheckBox) findViewById(R.id.whipped_cream_checkbox)).isChecked();
        EditText customerNameEditText = (EditText) findViewById(R.id.customer_name_view);
        String customerName = customerNameEditText.getText().toString();
        int orderTotal = calculatePrice(hasChocolate,hasWhippedCream);

        //Set the Subject and Intent to call Email
        String subject =  getString(R.string.order_summary_email_subject);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this

        //adding extras for email (Subject and Body of Email
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, (createOrderSummary(orderTotal, hasWhippedCream, hasChocolate, customerName)));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    /**
     * Calculates the price of the order.
     */
    public int calculatePrice(boolean Chocolate, boolean WhippedCream) {
        //get status of checkboxes and setup initial cupPrice
        int cupPrice = 5;

        // Chocolate adds 2 dollars to cupPrice
        if (Chocolate) {
            cupPrice += 2;

        }

        // Whipped Cream adds 1 dollar to cupPrice
        if (WhippedCream) {
            cupPrice += 1;

        }

        return quantity * cupPrice;

    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }


    /**
     * @param orderTotal is the order total
     *                   This method combines the variables and summary
     */
    private String createOrderSummary(int orderTotal, boolean WhippedCream, boolean chocolate, String customer) {
        String message = getString(R.string.order_summary_name);
        message += "\n" + getString(R.string.order_summary_whipped_cream) + WhippedCream;
        message += "\n" + getString(R.string.order_summary_chocolate) + chocolate;
        message += "\n" + getString(R.string.order_summary_quantity) + quantity;
        message += "\n" + getString(R.string.order_summary_price);
        message += "\n" + getString(R.string.thank_you);
        return message;


    }

}
