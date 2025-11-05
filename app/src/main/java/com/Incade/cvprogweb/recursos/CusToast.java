package com.Incade.cvprogweb.recursos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.Incade.cvprogweb.R;

public class CusToast extends AppCompatActivity {

    public void showCustomToast(Context context, String message, int milliseconds) {

        View layout = LayoutInflater.from(context).inflate(R.layout.custom_toast,null);

        ImageView icon = layout.findViewById(R.id.toast_icon);
        TextView text = layout.findViewById(R.id.toast_text);

        icon.setImageResource(R.drawable.logoincade);
        text.setText(message);

        Toast toast = new Toast(context.getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
        new android.os.Handler().postDelayed(toast::cancel, milliseconds);
    }


}
