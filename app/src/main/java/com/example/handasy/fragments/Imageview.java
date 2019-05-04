package com.example.handasy.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.handasy.R;
import com.example.handasy.view.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by ahmed on 3/25/2017.
 */

public class Imageview extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image, container, false);
        final TouchImageView image=(TouchImageView)view.findViewById(R.id.imageview);
        final Bundle bundle = getArguments();
        Uri myUri = Uri.parse(bundle.getString("imageUri"));
        Picasso.with(getContext()).load(myUri).into(image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
            Picasso.with(getContext()).load(bundle.getString("imageUri")).into(image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    {
                        BitmapFactory.Options options = null;
                        options = new BitmapFactory.Options();
                        options.inSampleSize = 8;
                        String path = Uri.parse(bundle.getString("imageUri")).getPath();
                        Bitmap bitmap = BitmapFactory.decodeFile(path,
                                options);
                       image.setImageBitmap(bitmap);
                    }
                }
            });
            }
        });
        return view;
    }
}
