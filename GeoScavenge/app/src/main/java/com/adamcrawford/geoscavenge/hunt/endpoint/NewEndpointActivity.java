package com.adamcrawford.geoscavenge.hunt.endpoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.adamcrawford.geoscavenge.LocationSync;
import com.adamcrawford.geoscavenge.MainActivity;
import com.adamcrawford.geoscavenge.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewEndpointActivity extends Activity implements NewEndpointFragment.OnNewEnd {
    LocationSync lSync;
    NewEndpointFragment nef;
    String TAG = "NEA";
    Uri fileUri = null;
    Uri imageUri = null;
    File photoFile = null;
    String endCity = null;
    String endState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_endpoint);
        nef = (NewEndpointFragment) getFragmentManager().findFragmentById(R.id.endFrag);
        lSync = LocationSync.getInstance();
        lSync.init(this);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyMMdd_HHmmss").format(new Date());
        Log.e(TAG, timeStamp);
        String imageFileName = "Geo_" + timeStamp;
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        return new File(storageDir, imageFileName + ".jpg");
    }

    private void getImage() {
        try {
            photoFile = createImageFile();
            fileUri = Uri.fromFile(photoFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Intent> cams = new ArrayList<Intent>();
        Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager manager = getPackageManager();
        List<ResolveInfo> listCam = manager.queryIntentActivities(capture, 0);
        for(ResolveInfo res : listCam) {
            String packageName = res.activityInfo.packageName;
            Intent intent = new Intent(capture);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            cams.add(intent);
        }

        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_PICK);

        Intent chooser = Intent.createChooser(gallery, "Select Source");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, cams.toArray(new Parcelable[cams.size()]));
        startActivityForResult(chooser, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            if(requestCode == 0) {
                boolean isCamera;
                if(data == null) {
                    isCamera = true;
                } else {
                    String action = data.getAction();
                    isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
                }

                if(isCamera) {
                    imageUri = fileUri;
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(fileUri);
                    this.sendBroadcast(mediaScanIntent);
                } else {
                    imageUri = data.getData();
                }
                Log.e(TAG, imageUri.toString());
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.getCurrent:{
                nef.endLon.setText(String.valueOf(lSync.getCurrentLoc().getLongitude()));
                nef.endLat.setText(String.valueOf(lSync.getCurrentLoc().getLatitude()));
                lSync.quit();
                break;
            }
            case R.id.searchAddress:{
                if (Geocoder.isPresent()) {
                    lSync.init(this);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    LayoutInflater inflater = this.getLayoutInflater();
                    View v = inflater.inflate(R.layout.fragment_private_entrance, null);
                    final EditText eT = (EditText) v.findViewById(R.id.searchInput);
                    eT.setHint(R.string.addressHint);
                    eT.setInputType(InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS);
                    builder.setView(v)
                            .setPositiveButton(R.string.search, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String query = eT.getText().toString();
                                    Address location = lSync.getLocationFromAddress(query, getApplicationContext());
                                    if (location != null) {
                                        Log.e(TAG, location.toString());
                                        nef.endLat.setText(String.valueOf(location.getLatitude()));
                                        nef.endLon.setText(String.valueOf(location.getLongitude()));
                                        endCity = location.getLocality();
                                        endState = location.getAdminArea();
                                        Log.e(TAG, "City: " + endCity + ", " + endState);

                                    } else {
                                        MainActivity.printToast(getString(R.string.notAvail));
                                    }
                                    lSync.quit();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    lSync.quit();
                                    dialogInterface.cancel();
                                }
                            }).setTitle(R.string.address);
                    builder.show();
                } else {
                    MainActivity.printToast(getString(R.string.notAvail));
                }
                break;
            }
            case R.id.addPic:{
                getImage();
                break;
            }
            default:{
                break;
            }
        }

    }

    @Override
    public void saveEnd() {
        Address address = lSync.getLocation(Double.parseDouble(nef.endLat.getText().toString()),
                Double.parseDouble(nef.endLon.getText().toString()), this);
        EndItem endItem = new EndItem();
        endItem.setGuesses(Integer.parseInt(nef.endGuesses.getText().toString()));
        if (address != null) {
            if (address.getLocality()==null){
                endItem.setEndCity(getString(R.string.unknown));
            } else {
                endItem.setEndCity(address.getLocality());
            }
        } else {
            endItem.setEndCity(getString(R.string.unknown));
        }
        Log.i(TAG, endItem.getEndCity());
        if (address != null) {
            if (address.getAdminArea()==null){
                endItem.setEndState(getString(R.string.unknown));
            } else {
                endItem.setEndState(address.getAdminArea());
            }
        } else {
            endItem.setEndCity(getString(R.string.unknown));
        }
        endItem.setEndDesc(nef.endDesc.getText().toString());
        endItem.setEndLat(Double.parseDouble(nef.endLat.getText().toString()));
        endItem.setEndLon(Double.parseDouble(nef.endLon.getText().toString()));
//        if (imageUri != null){
//            endItem.setEndImgStr(imageUri.toString());
//        }
        Intent eIntent = new Intent();
        eIntent.putExtra("item", endItem);
        setResult(RESULT_OK, eIntent);
        finish();
    }
}
