package com.example.handasy.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.handasy.model.Drawer;
import com.example.handasy.R;
import com.example.handasy.view.CustomButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.example.ActivityProjectAdd;
import com.example.handasy.view.CustomTextView;
import cz.msebera.android.httpclient.Header;
import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;


/**
 * Created by ahmed on 3/27/2017.
 */

public class NewProject3 extends Fragment {
    LinearLayout skBackground, bldyaBackground;
    RecyclerView skImages, bldyaImages;
    CustomTextView text2;
    ImagesAdapter skAdapter, bldyaAdapter;
    List<uriOriantation> skUri, bldyaUri;
    Uri imageUri;
    String imageKind = "";
    int counter = 0, sum = 0, imageInsert = 0;
    Dialog progress;
    public static List<ImageData> imageList;
    String encodedString;
    RequestParams params = new RequestParams();
    Bitmap bitmap;
    private boolean enter = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_project3, container, false);
        skBackground = (LinearLayout) rootView.findViewById(R.id.skBackground);
        bldyaBackground = (LinearLayout) rootView.findViewById(R.id.bldyaBackground);
        skImages = (RecyclerView) rootView.findViewById(R.id.RecyclerSk);
        bldyaImages = (RecyclerView) rootView.findViewById(R.id.RecyclerBldya);
        Drawer.title.setText("صور الصك و البلدية");
        Drawer.postionSelected = -1;
        skUri = new ArrayList<>();
        bldyaUri = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.CAMERA},
                    1);

        }

        Uri addUri = Uri.parse("android.resource://com.example.handasy/" + R.drawable.add_more);
        uriOriantation uriOriantation = new uriOriantation();
        uriOriantation.uri = addUri;
        uriOriantation.orintation = 1;
        skUri.add(uriOriantation);
        bldyaUri.add(uriOriantation);

        skAdapter = new ImagesAdapter(skUri, "sk");
        bldyaAdapter = new ImagesAdapter(bldyaUri, "bldya");

        skBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageKind = "sk";
                Captuer();
            }
        });

        bldyaBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageKind = "bldya";
                Captuer();

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), calculateNoOfColumns(getContext()));
        RecyclerView.LayoutManager mLayoutManager2 = new GridLayoutManager(getContext(), calculateNoOfColumns(getContext()));

        skImages.setLayoutManager(mLayoutManager);
        bldyaImages.setLayoutManager(mLayoutManager2);


        skImages.setAdapter(skAdapter);
        bldyaImages.setAdapter(bldyaAdapter);

        design();

        return rootView;
    }

    private void Captuer() {
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.CAMERA},
                    1);
        } else {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog_confirmation);
            CustomButton camera = (CustomButton) dialog.findViewById(R.id.yes);
            camera.setText("الكاميرا");
            CustomButton gallery = (CustomButton) dialog.findViewById(R.id.no);
            gallery.setText("معرض الصور");
            CustomTextView text = (CustomTextView) dialog.findViewById(R.id.text);
            text.setText("قم باختيار الصور");
            dialog.show();


            gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AlbumSelectActivity.class);
                    intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 10); // set limit for image selection
                    startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                    dialog.cancel();
                }
            });

            camera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    String currentDateandTime = sdf.format(new Date());
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File photo = new File(Environment.getExternalStorageDirectory(), currentDateandTime + ".png");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photo));
                    imageUri = Uri.fromFile(photo);
                    startActivityForResult(intent, 1000);
                    dialog.cancel();
                }
            });
        }
    }

    /* public Uri getImageUri(Context inContext, Bitmap inImage) {
         ByteArrayOutputStream bytes = new ByteArrayOutputStream();
         inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
         String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
         return Uri.parse(path);
     }

    /* public Uri setImageUri() {
         ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
         File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
         File file = new File(directory,System.currentTimeMillis() + ".png");
         Uri imgUri = Uri.fromFile(file);
         this.imgPath = file.getAbsolutePath();
         return imgUri;
     }

     public String getRealPathFromURI(Uri uri) {
         Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
         cursor.moveToFirst();
         int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
         return cursor.getString(idx);

     }
 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            {

                Uri uriImage = imageUri;
                ExifInterface ei = null;
                try {
                    ei = new ExifInterface(uriImage.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_UNDEFINED);
                    Log.e("Orintation:",orientation+"");
                    uriOriantation uriOriantation = new uriOriantation();
                    uriOriantation.uri = uriImage;
                    uriOriantation.orintation = orientation;
                /*
                Bundle extras = data.getExtras();
                Bitmap Bmp = (Bitmap) extras.get("data");
                Uri tempUri = getImageUri(getContext(), Bmp);
                File finalFile = new File(getRealPathFromURI(tempUri));*/
                    if (imageKind.equals("sk"))
                        skUri.add(uriOriantation);
                    else if (imageKind.equals("bldya"))
                        bldyaUri.add(uriOriantation);
                    skAdapter.notifyItemInserted(skAdapter.getItemCount() + 1);
                    bldyaAdapter.notifyItemInserted(bldyaAdapter.getItemCount() + 1);

                } catch (Exception e) {

                }
            }
        }
        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
            for (int i = 0; i < images.size(); i++) {

                try {
                    uriOriantation uriOriantation = new uriOriantation();
                    uriOriantation.uri = Uri.fromFile(new File(images.get(i).path));
                    uriOriantation.orintation = 1;
                    if (imageKind.equals("sk")) {
                        skUri.add(uriOriantation);
                        skAdapter.notifyItemInserted(skAdapter.getItemCount() + 1);
                    } else if (imageKind.equals("bldya")) {
                        bldyaUri.add(uriOriantation);
                        bldyaAdapter.notifyItemInserted(bldyaAdapter.getItemCount() + 1);

                    }

                } catch (Exception e) {

                }
            }
        }
    }

    private void design() {
        ActivityProjectAdd.indicator.setImageResource(R.drawable.indicatorproject3);
        ActivityProjectAdd.right.setVisibility(View.VISIBLE);
        ActivityProjectAdd.right.setText("السابق");
        ActivityProjectAdd.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ActivityProjectAdd.left.setVisibility(View.VISIBLE);
        ActivityProjectAdd.left.setText("ارسال طلب المشروع");
        ActivityProjectAdd.middle.setVisibility(View.INVISIBLE);

        ActivityProjectAdd.left.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                progress = new Dialog(getActivity());
                progress.setContentView(R.layout.dialog_loading);
                text2 = (CustomTextView) progress.findViewById(R.id.text);
                progress.setCancelable(false);
                try {
                    progress.show();
                } catch (Exception e) {

                }
                counter = 0;
                sum = bldyaUri.size() + skUri.size();
                imageInsert = 0;

                imageList = new ArrayList<ImageData>();
                text2.setText("جاري رفع الصور .. 0/" + (sum - 2));

                if (sum == 2) {
                    insertDB();
                }
                for (int i = 0; i < bldyaUri.size(); i++) {
                    if (i != 0) {
                        ImageData imageData = new ImageData();
                        imageData.image_id = UUID.randomUUID().toString();
                        imageData.name = getFileName(bldyaUri.get(i).uri);
                        imageData.kind = 2;
                        imageData.orintation=bldyaUri.get(i).orintation;
                        imageList.add(imageData);
                        encodeImagetoString async = new encodeImagetoString();
                        async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getFileName(bldyaUri.get(i).uri), bldyaUri.get(i).uri.getPath());
                    }
                }
                for (int i = 0; i < skUri.size(); i++) {
                    if (i != 0) {

                        ImageData imageData = new ImageData();
                        imageData.image_id = UUID.randomUUID().toString();
                        imageData.name = getFileName(skUri.get(i).uri);
                        imageData.kind = 1;
                        imageData.orintation=skUri.get(i).orintation;
                        imageList.add(imageData);
                        encodeImagetoString async = new encodeImagetoString();
                        async.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, getFileName(skUri.get(i).uri), skUri.get(i).uri.getPath());
                    }
                }
            }

        });


    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());

        if (!(result.endsWith("jpg") || result.endsWith("png") || result.endsWith("jpeg"))) {
            result += ".png";
        }

        return currentDateandTime + result;
    }

    public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.MyViewHolder> {

        private List<uriOriantation> imagesList;
        String kind;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView delete, image;
            public ProgressBar progressBar;

            public MyViewHolder(View view) {
                super(view);

                image = (ImageView) view.findViewById(R.id.image);
                delete = (ImageView) view.findViewById(R.id.delete);
                progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            }
        }


        public ImagesAdapter(List<uriOriantation> imagesList, String kind) {
            this.kind = kind;
            this.imagesList = imagesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.new_image_row, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            holder.progressBar.setVisibility(View.VISIBLE);
            if (position == 0) {
                holder.delete.setVisibility(View.INVISIBLE);
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageKind = kind;
                        Captuer();
                    }
                });
            } else {
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        Bundle bundle = new Bundle();
                        bundle.putString("imageUri", imagesList.get(position).uri.toString());
                        Imageview fragment = new Imageview();
                        fragment.setArguments(bundle);
                        ft.add(R.id.activity_main_content_fragment3, fragment);
                        ft.addToBackStack(null);
                        ft.commit();

                    }
                });
                holder.delete.setVisibility(View.VISIBLE);
            }

            if (position != 0) {
                BitmapFactory.Options options = null;
                options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                bitmap = BitmapFactory.decodeFile(imagesList.get(position).uri.getPath(),
                        options);
                try {
                    switch (imagesList.get(position).orintation) {

                        case ExifInterface.ORIENTATION_ROTATE_90:
                            bitmap = rotateImage(bitmap, 90);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_180:
                            bitmap = rotateImage(bitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            bitmap = rotateImage(bitmap, 270);
                            break;

                        case ExifInterface.ORIENTATION_NORMAL:

                        default:
                            break;
                    }
                } catch (Exception e) {

                }
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                holder.image.setBackground(drawable);
            } else {
                holder.image.setBackgroundResource(R.drawable.add_more);
            }
            holder.progressBar.setVisibility(View.INVISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeImage(position, kind);
                }
            });
        }

        public Bitmap rotateImage(Bitmap source, float angle) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        }

        @Override
        public int getItemCount() {
            if (imagesList.size() == 1) {
                if (skUri.size() == 1) {
                    skBackground.setVisibility(View.VISIBLE);
                }
                if (bldyaUri.size() == 1) {
                    bldyaBackground.setVisibility(View.VISIBLE);
                }

            } else {
                if (imageKind.equals("sk"))
                    skBackground.setVisibility(View.INVISIBLE);
                else if (imageKind.equals("bldya"))
                    bldyaBackground.setVisibility(View.INVISIBLE);
            }
            return imagesList.size();
        }
    }

    private void removeImage(int position, String kind) {
        if (kind.equals("sk")) {
            skImages.removeViewAt(position);
            skUri.remove(position);
            skAdapter.notifyItemRemoved(position);
            skAdapter.notifyItemRangeChanged(position, skUri.size());

        } else if (kind.equals("bldya")) {
            bldyaImages.removeViewAt(position);
            bldyaUri.remove(position);
            bldyaAdapter.notifyItemRemoved(position);
            bldyaAdapter.notifyItemRangeChanged(position, bldyaUri.size());
        }
    }

    // AsyncTask - To convert Image to String
    class encodeImagetoString extends AsyncTask<String, Void, ImageData> {

        @Override
        protected ImageData doInBackground(String... params) {
            BitmapFactory.Options options = null;
            options = new BitmapFactory.Options();
            options.inSampleSize = 3;

            bitmap = BitmapFactory.decodeFile(params[1],
                    options);

            Log.e("BitMap Size:", bitmap.getByteCount() + "");
            if (bitmap.getByteCount() > 500000) {
                float aspectRatio = bitmap.getWidth() /
                        (float) bitmap.getHeight();
                int width = 880;
                int height = Math.round(width / aspectRatio);

                bitmap = Bitmap.createScaledBitmap(
                        bitmap, width, height, false);
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Must compress the Image to reduce image size to make upload easy
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byte_arr = stream.toByteArray();
            // Encode Image to String
            ImageData data = new ImageData();
            if (!(params[0].endsWith("jpg") || params[0].endsWith("png") || params[0].endsWith("jpeg"))) {
                params[0] += ".png";
            }
            Log.e("imageName:", params[0]);
            data.name = params[0];
            data.encode = Base64.encodeToString(byte_arr, 0);

            return data;
        }

        @Override
        protected void onPostExecute(ImageData data) {
            // Put converted Image string into Async Http Post param
            params.put("image", data.encode);
            params.put("name", data.name);
            // Trigger Image upload
            triggerImageUpload();
        }
    }

    public void triggerImageUpload() {
        makeHTTPCall();
    }

    // Make Http call to upload Image to Php server
    public void makeHTTPCall() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setConnectTimeout(50000);
        client.setResponseTimeout(50000);
        // Don't forget to change the IP address to your LAN address. Port no as well.
        client.post("http://i-tecgroup.com/Images/Project/imageupload.php",
                params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        counter++;
                        text2.setText("جاري رفع الصور .. " + counter + "/" + (sum - 2));

                        if (counter == sum - 2) {
                            insertDB();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {

                        progress.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getActivity(),
                                    "Requested resource not found",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getActivity(),
                                    "Something went wrong at server end",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(
                                    getActivity(), "فشل فى الارسال , برجاء المحاوله مره اخرى", Toast.LENGTH_LONG)
                                    .show();
                        }

                    }

                });

    }

    private void insertDB() {


        progress.hide();
        if (enter) {
            enter = false;
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_right,
                    R.anim.slide_out_right, R.anim.translat_left,
                    R.anim.translat_left);
            NewProject4 fragment = new NewProject4();
            progress.dismiss();
            ft.replace(R.id.activity_main_content_fragment3, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }


    }


    public class ImageData {
        String name;
        String image_id;
        int kind;
        int orintation;
        String encode;
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 80);
        return noOfColumns;
    }


    @Override
    public void onStop() {
        try {
            DrawerLayout.LayoutParams params1 = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params1.setMargins(0, (int) (50 * ActivityProjectAdd.d), 0, (int) (0 * ActivityProjectAdd.d));
            ActivityProjectAdd.frameLayout.setLayoutParams(params1);
        } catch (Exception e) {
            Log.e("stop problem", e.toString());
        }
        super.onStop();
    }

    @Override
    public void onStart() {
        try {
            DrawerLayout.LayoutParams params1 = new DrawerLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params1.setMargins(0, (int) (50 * ActivityProjectAdd.d), 0, (int) (110 * ActivityProjectAdd.d));
            ActivityProjectAdd.frameLayout.setLayoutParams(params1);
        } catch (Exception e) {
            Log.e("start problem", e.toString());
        }

        super.onStart();
    }


    class uriOriantation {
        Uri uri;
        int orintation;
    }
}