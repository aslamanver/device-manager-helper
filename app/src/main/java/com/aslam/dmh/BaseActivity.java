package com.aslam.dmh;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    private ViewDataBinding binding;

    protected void setBinding(int layoutResID) {
        binding = DataBindingUtil.setContentView(this, layoutResID);
    }

    protected T getBinding() {
        return (T) binding;
    }
}
