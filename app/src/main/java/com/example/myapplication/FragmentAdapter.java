package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new FragSecond();
            case 2:
                return new FragThird();
        }
        return new FragFirst();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
