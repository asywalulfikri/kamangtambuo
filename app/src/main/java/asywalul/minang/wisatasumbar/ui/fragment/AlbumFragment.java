package asywalul.minang.wisatasumbar.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.ArrayList;

import asywalul.minang.wisatasumbar.R;
import asywalul.minang.wisatasumbar.adapter.GalleryAdapter;
import asywalul.minang.wisatasumbar.image.CropPhotoActivity;
import asywalul.minang.wisatasumbar.util.camera.AppConstants;
import asywalul.minang.wisatasumbar.util.camera.CameraManager;
import asywalul.minang.wisatasumbar.util.camera.ImageUtils;
import asywalul.minang.wisatasumbar.util.camera.model.PhotoItem;

import static android.app.Activity.RESULT_OK;

/**
 * @author tongqian.ni
 */
public class AlbumFragment extends BaseFragment {
    private ArrayList<PhotoItem> photos = new ArrayList<PhotoItem>();

    public AlbumFragment() {
        super();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public static Fragment newInstance(ArrayList<PhotoItem> photos) {
        Fragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putSerializable("photos", photos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_album, null);
        photos = (ArrayList<PhotoItem>) getArguments().getSerializable("photos");
        albums = (GridView) root.findViewById(R.id.albums);
        albums.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                PhotoItem photo = photos.get(arg2);
                processPhotoItem(photo);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent result) {
        //  if (requestCode == AppConstants.REQUEST_CROP && resultCode == RESULT_OK) {
        String uri = result.getStringExtra("path");
        Log.d("pathalbum",uri);
        if(!uri.equals(null)|| !uri.equals("")){
            Intent newIntent = new Intent();
            newIntent.putExtra("path",uri);
            getActivity().setResult(RESULT_OK,newIntent);
            getActivity().finish();
        }
    }

    public void processPhotoItem(PhotoItem photo) {
        Uri uri = photo.getImageUri().startsWith("file:") ? Uri.parse(photo.getImageUri()) : Uri.parse("file://" + photo.getImageUri());
        if (ImageUtils.isSquare(photo.getImageUri())) {
            Intent newIntent = new Intent();
            newIntent.putExtra("path",uri.getPath());
            getActivity().setResult(RESULT_OK,newIntent);
            getActivity().finish();

        } else {
            Intent newIntent = new Intent(getActivity(), CropPhotoActivity.class);
            newIntent.setData(uri);
            startActivityForResult(newIntent, AppConstants.REQUEST_CROP);
        }
    }





    @Override
    public void onResume() {
        super.onResume();
        albums.setAdapter(new GalleryAdapter(getActivity(), photos));
    }

    private GridView albums;
}
