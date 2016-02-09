package com.zircon.app.ui.test;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zircon.app.ui.common.AbsBaseNormalActionBarNavActivity;
import com.zircon.app.ui.common.AbsFragment;

/**
 * Created by jikoobaruah on 03/02/16.
 */
public class TestNavActivity extends AbsBaseNormalActionBarNavActivity {


    @Override
    protected AbsFragment getFragment() {
        Bundle args = new Bundle();
        args.putString(AssetCalendarFragment.ARGS.ASSET_ID , 100+"");
        return (AbsFragment) Fragment.instantiate(this,AssetCalendarFragment.class.getName(),args);
    }
}
