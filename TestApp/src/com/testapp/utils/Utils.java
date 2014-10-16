package com.testapp.utils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;

public class Utils {
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static int generateViewId() {
	    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
	        for (;;) {
	            final int result = sNextGeneratedId.get();
	            int newValue = result + 1;
	            if (newValue > 0x00FFFFFF) newValue = 1;
	            if (sNextGeneratedId.compareAndSet(result, newValue)) {
	                return result;
	            }
	        }
	    } else {
	        return View.generateViewId();
	    }
	}
	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
	
	public static String toSQL(String satiment)
	{
		Pattern pattern = Pattern.compile("[a-zA-Z]+");
		Matcher matcher = pattern.matcher(satiment);
		if (matcher.find())
		{
			return matcher.group(0);
		}
		else return String.valueOf(satiment.hashCode());
	}

}
