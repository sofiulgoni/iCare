package com.extraterristrial.healthmanagementsystem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.ViewFlipper;

public class HelpPageFragment extends Fragment {
    Toolbar toolbar;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ViewFlipper viewFlipper;
    private final GestureDetector detector=new GestureDetector(new SwipeGestureDetector());
    int image[]={R.mipmap.camera,R.mipmap.file,R.mipmap.save};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.help_page_layout,container,false);
        toolbar=(Toolbar)view.findViewById(R.id.toolbar);
        toolbar.setTitle("Help");
        viewFlipper=(ViewFlipper)view.findViewById(R.id.viewflipper);
        for (int anImage : image) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(anImage);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        switch (getArguments().getString("type"))
        {
            case "automatic":
            {
                toolbar.inflateMenu(R.menu.add_menu);
                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId()==R.id.add_profile)
                        {
                            Fragment createProfileFragment=new CreateProfileFragment();
                            FragmentManager fm=getActivity().getSupportFragmentManager();
                            FragmentTransaction ft=fm.beginTransaction().replace(R.id.home_layout,createProfileFragment);
                            ft.commit();
                        }
                        return true;
                    }
                });
                viewFlipper.setAutoStart(true);
                viewFlipper.setFlipInterval(3000);
                viewFlipper.startFlipping();
            }break;
            case "manual":
            {
                toolbar.inflateMenu(R.menu.add_cancel_menu);
                toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.cross) {
                            Intent intent = new Intent(getActivity(), HomePageActivity.class);
                            startActivity(intent);
                        }
                        if (item.getItemId()==R.id.add_profile)
                        {
                            Fragment createProfileFragment=new CreateProfileFragment();
                            Bundle bundle=new Bundle();
                            bundle.putString("origin","add");
                            createProfileFragment.setArguments(bundle);
                            FragmentManager fm=getActivity().getSupportFragmentManager();
                            FragmentTransaction ft=fm.beginTransaction().replace(R.id.home_layout,createProfileFragment);
                            ft.commit();
                        }
                        return true;
                    }
                });
                viewFlipper.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        detector.onTouchEvent(event);
                        return true;
                    }
                });
            }break;
        }
        return view;
    }
    class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                  //  viewFlipper.setInAnimation(AnimationUtils.loadAnimation(R.anim.left_in));
                  //  viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(R.anim.left_out));
                    viewFlipper.showNext();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                   // viewFlipper.setInAnimation(AnimationUtils.loadAnimation(R.anim.right_in));
                   // viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(R.anim.right_out));
                    viewFlipper.showPrevious();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}


